package org.oregongoestocollege.itsaplan.support;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Calendar;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.x500.X500Principal;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;

import org.oregongoestocollege.itsaplan.Utils;

/**
 * references for code
 * https://github.com/googlesamples/android-BasicAndroidKeyStore
 * https://proandroiddev.com/secure-data-in-android-encryption-in-android-part-1-e5fd150e316f
 * https://gist.github.com/JosiasSena/3bf4ca59777f7dedcaf41a495d96d984
 * https://medium.com/@ericfu/securely-storing-secrets-in-an-android-application-501f030ae5a3
 *
 * Oregon GEAR UP App
 * Copyright © 2020 Oregon GEAR UP. All rights reserved.
 */
public class CryptoUtil
{
	private static final String LOG_TAG = "GearUp_CryptoUtil";
	private static final String AndroidKeyStore = "AndroidKeyStore";
	private static final String AES_MODE = "AES/GCM/NoPadding";
	private static final String RSA_MODE = "RSA/ECB/PKCS1Padding";
	private static final String FIXED_IV = "GearUpStatIv";
	private static final String GEARUP_KEY_ALIAS = "GearUpKey";
	private KeyStore keyStore;

	private void init(Context context)
		throws KeyStoreException, CertificateException, NoSuchAlgorithmException,
		IOException, NoSuchProviderException, InvalidAlgorithmParameterException
	{
		keyStore = KeyStore.getInstance(AndroidKeyStore);
		keyStore.load(null);

		if (!keyStore.containsAlias(GEARUP_KEY_ALIAS))
		{
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
			{
				KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, AndroidKeyStore);
				keyGenerator.init(
					new KeyGenParameterSpec.Builder(GEARUP_KEY_ALIAS,
						KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
						.setBlockModes(KeyProperties.BLOCK_MODE_GCM)
						.setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
						.setRandomizedEncryptionRequired(false)
						.build());
				keyGenerator.generateKey();
			}
			else
			{
				// Generate a key pair for encryption
				Calendar start = Calendar.getInstance();
				Calendar end = Calendar.getInstance();
				end.add(Calendar.YEAR, 10);
				KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(context)
					.setAlias(GEARUP_KEY_ALIAS)
					.setSubject(new X500Principal("CN=" + GEARUP_KEY_ALIAS))
					.setSerialNumber(BigInteger.TEN)
					.setStartDate(start.getTime())
					.setEndDate(end.getTime())
					.build();
				// using KeyProperties.KEY_ALGORITHM_RSA gives warning but per all the documentation
				// it's what we can use...
				KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", AndroidKeyStore);
				kpg.initialize(spec);
				kpg.generateKeyPair();
			}
		}
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	private byte[] rsaEncrypt(byte[] secret) throws KeyStoreException, UnrecoverableEntryException,
		NoSuchAlgorithmException, NoSuchPaddingException,
		NoSuchProviderException, InvalidKeyException, IOException
	{
		KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(GEARUP_KEY_ALIAS, null);

		// Encrypt the text
		Cipher inputCipher = Cipher.getInstance(RSA_MODE, "AndroidOpenSSL");
		inputCipher.init(Cipher.ENCRYPT_MODE, privateKeyEntry.getCertificate().getPublicKey());

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, inputCipher);
		cipherOutputStream.write(secret);
		cipherOutputStream.close();

		return outputStream.toByteArray();
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	private byte[] rsaDecrypt(byte[] encrypted) throws NoSuchAlgorithmException, NoSuchPaddingException,
		KeyStoreException, UnrecoverableEntryException, NoSuchProviderException, InvalidKeyException,
		IOException
	{
		KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(GEARUP_KEY_ALIAS, null);
		Cipher output = Cipher.getInstance(RSA_MODE, "AndroidOpenSSL");
		output.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());
		CipherInputStream cipherInputStream = new CipherInputStream(new ByteArrayInputStream(encrypted), output);
		ArrayList<Byte> values = new ArrayList<>();
		int nextByte;
		while ((nextByte = cipherInputStream.read()) != -1)
		{
			values.add((byte)nextByte);
		}

		byte[] bytes = new byte[values.size()];
		for (int i = 0; i < bytes.length; i++)
		{
			bytes[i] = values.get(i);
		}
		return bytes;
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	private String getEncryptedKey(Context context) throws KeyStoreException, UnrecoverableEntryException,
		NoSuchAlgorithmException, NoSuchPaddingException,
		NoSuchProviderException, InvalidKeyException, IOException
	{
		SharedPreferences pref = GearUpSharedPreferences.getSharedPreferences(context);
		String encryptedKeyB64 = pref.getString(GearUpSharedPreferences.PREFS_KEY_CRYPTO, null);
		if (encryptedKeyB64 == null)
		{
			byte[] key = new byte[16];
			SecureRandom secureRandom = new SecureRandom();
			secureRandom.nextBytes(key);
			byte[] encryptedKey = rsaEncrypt(key);
			encryptedKeyB64 = Base64.encodeToString(encryptedKey, Base64.DEFAULT);
			SharedPreferences.Editor edit = pref.edit();
			edit.putString(GearUpSharedPreferences.PREFS_KEY_CRYPTO, encryptedKeyB64);
			edit.apply();
		}
		return encryptedKeyB64;
	}

	private Key getSecretKey(Context context) throws KeyStoreException, CertificateException,
		NoSuchAlgorithmException, IOException, NoSuchProviderException, InvalidAlgorithmParameterException,
		UnrecoverableEntryException, NoSuchPaddingException, InvalidKeyException
	{
		// make sure we have the KeyStore setup
		init(context);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
		{
			return keyStore.getKey(GEARUP_KEY_ALIAS, null);
		}
		else
		{
			String encryptedKeyB64 = getEncryptedKey(context);

			// TODO need to check null
			byte[] encryptedKey = Base64.decode(encryptedKeyB64, Base64.DEFAULT);
			byte[] key = rsaDecrypt(encryptedKey);
			return new SecretKeySpec(key, "AES");
		}
	}

	public byte[] encrypt(Context context, @NonNull byte [] input) throws KeyStoreException, CertificateException,
		NoSuchAlgorithmException, IOException, NoSuchProviderException, InvalidAlgorithmParameterException,
		UnrecoverableEntryException, NoSuchPaddingException, InvalidKeyException,
		BadPaddingException, IllegalBlockSizeException
	{
		Cipher c = Cipher.getInstance(AES_MODE);
		c.init(Cipher.ENCRYPT_MODE, getSecretKey(context), new GCMParameterSpec(128, FIXED_IV.getBytes()));
		return c.doFinal(input);
	}

	public byte[] decrypt(Context context, @NonNull byte[] encrypted) throws KeyStoreException, CertificateException,
		NoSuchAlgorithmException, IOException, NoSuchProviderException, InvalidAlgorithmParameterException,
		UnrecoverableEntryException, NoSuchPaddingException, InvalidKeyException,
		BadPaddingException, IllegalBlockSizeException
	{
		Cipher c = Cipher.getInstance(AES_MODE);
		c.init(Cipher.DECRYPT_MODE, getSecretKey(context), new GCMParameterSpec(128, FIXED_IV.getBytes()));
		return c.doFinal(encrypted);
	}

	@Nullable
	public String safeEncrypt(@NonNull Context context, @Nullable String input)
	{
		String encryptedString = null;

		if (!TextUtils.isEmpty(input))
		{
			try
			{
				byte[] encryptedBytes = encrypt(context, input.getBytes());
				encryptedString = Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
			}
			catch (Exception e)
			{
				if (Utils.DEBUG)
					Utils.d(LOG_TAG, "safeEncrypt() message: " + e.getMessage());
			}
		}

		return encryptedString;
	}

	@Nullable
	public String safeDecrypt(@NonNull Context context, @Nullable String input)
	{
		String decryptedString = null;

		if (!TextUtils.isEmpty(input))
		{
			try
			{
				byte[] encryptedBytes = Base64.decode(input, Base64.DEFAULT);
				byte[] decryptedBytes = decrypt(context, encryptedBytes);
				if (decryptedBytes != null)
					decryptedString = new String(decryptedBytes);
			}
			catch (Exception e)
			{
				if (Utils.DEBUG)
					Utils.d(LOG_TAG, "safeDecrypt() message: " + e.getMessage());
			}
		}

		return decryptedString;
	}
}

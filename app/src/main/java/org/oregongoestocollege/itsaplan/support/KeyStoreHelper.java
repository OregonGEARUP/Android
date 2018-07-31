package org.oregongoestocollege.itsaplan.support;
/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAKeyGenParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import static java.security.spec.RSAKeyGenParameterSpec.F4;

public class KeyStoreHelper {

	public static final String TAG = "KeyStoreHelper";

	/**
	 * Creates a public and private key and stores it using the Android Key
	 * Store, so that only this application will be able to access the keys.
	 */
	public static void createKeys(Context context, String alias) throws NoSuchProviderException,
		NoSuchAlgorithmException, InvalidAlgorithmParameterException {
			createKeysM(alias, false);
	}

	@TargetApi(Build.VERSION_CODES.M)
	static void createKeysM(String alias, boolean requireAuth) {
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(
				KeyProperties.KEY_ALGORITHM_RSA, SecurityConstants.KEYSTORE_PROVIDER_ANDROID_KEYSTORE);
			keyPairGenerator.initialize(
				new KeyGenParameterSpec.Builder(
					alias,
					KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
					.setAlgorithmParameterSpec(new RSAKeyGenParameterSpec(1024, F4))
					.setBlockModes(KeyProperties.BLOCK_MODE_CBC)
					.setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
					.setDigests(KeyProperties.DIGEST_SHA256,
						KeyProperties.DIGEST_SHA384,
						KeyProperties.DIGEST_SHA512)
					// Only permit the private key to be used if the user authenticated
					// within the last five minutes.
					.setUserAuthenticationRequired(requireAuth)
					.build());
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			Log.d(TAG, "Public Key is: " + keyPair.getPublic().toString());

		} catch (NoSuchProviderException | NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
			throw new RuntimeException(e);
		}
	}

	private static KeyStore.PrivateKeyEntry getPrivateKeyEntry(String alias) {
		try {
			KeyStore ks = KeyStore
				.getInstance(SecurityConstants.KEYSTORE_PROVIDER_ANDROID_KEYSTORE);
			ks.load(null);
			KeyStore.Entry entry = ks.getEntry(alias, null);

			if (entry == null) {
				Log.w(TAG, "No key found under alias: " + alias);
				Log.w(TAG, "Exiting signData()...");
				return null;
			}

			if (!(entry instanceof KeyStore.PrivateKeyEntry)) {
				Log.w(TAG, "Not an instance of a PrivateKeyEntry");
				Log.w(TAG, "Exiting signData()...");
				return null;
			}
			return (KeyStore.PrivateKeyEntry) entry;
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		}
	}

	public static String encrypt(String alias, String plaintext) {
		if (TextUtils.isEmpty(plaintext))
			return "";

		try {
			PublicKey publicKey = getPrivateKeyEntry(alias).getCertificate().getPublicKey();
			Cipher cipher = getCipher();
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return Base64.encodeToString(cipher.doFinal(plaintext.getBytes()), Base64.NO_WRAP);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String decrypt(String alias, String ciphertext) {
		if (TextUtils.isEmpty(ciphertext))
			return "";
		try {
			PrivateKey privateKey = getPrivateKeyEntry(alias).getPrivateKey();
			Cipher cipher = getCipher();
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return new String(cipher.doFinal(Base64.decode(ciphertext, Base64.NO_WRAP)));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static Cipher getCipher() throws NoSuchPaddingException, NoSuchAlgorithmException {
		return Cipher.getInstance(
			String.format("%s/%s/%s",
				SecurityConstants.TYPE_RSA,
				SecurityConstants.BLOCKING_MODE,
				SecurityConstants.PADDING_TYPE));
	}

	public interface SecurityConstants {
		String KEYSTORE_PROVIDER_ANDROID_KEYSTORE = "AndroidKeyStore";
		String TYPE_RSA = "RSA";
		String PADDING_TYPE = "PKCS1Padding";
		String BLOCKING_MODE = "NONE";

		String SIGNATURE_SHA256withRSA = "SHA256withRSA";
		String SIGNATURE_SHA512withRSA = "SHA512withRSA";
	}

}
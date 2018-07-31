package org.oregongoestocollege.itsaplan;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;
import java.util.List;
import javax.crypto.SecretKey;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.oregongoestocollege.itsaplan.data.MyPlanRepository;
import org.oregongoestocollege.itsaplan.data.Password;
import org.oregongoestocollege.itsaplan.support.KeyStoreHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * PasswordsFragment - handles data entry for password fields. Uses Butterknife library.
 *
 * @see "https://github.com/JakeWharton/butterknife"
 * @see "http://jakewharton.github.io/butterknife/"
 *
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class PasswordsFragment extends Fragment
{
	private static final String LOG_TAG = "GearUpPasswordsFrag";

	private static final String KEYSTORE_ALIAS = "gearUpAlias";
	public static final String GEAR_UP_PREFERENCES = "gearUpPreferences";
	public static final String EDIT_SSN_KEY = "edit_ssn";
	public static final String EDIT_SSN_1_KEY = "edit_ssn1";
	public static final String EDIT_SSN_2_KEY = "edit_ssn2";
	public static final String EDIT_DRIVER_LIC_KEY = "edit_driver_lic";
	public static final String FSA_USERNAME_KEY = "fsa_username";
	public static final String FSA_PASSWORD_KEY = "fsa_password";
	public static final String SCHOLARSHIP_EMAIL_KEY = "scholarship_email";
	public static final String SCHOLARSHIP_PASSWORD_KEY = "scholarship_password";


	private OnFragmentInteractionListener mListener;
	// tracks the locked / unlocked state of the controls
	private boolean locked = false;
	// holds list of all views we need to show/hide text
	List<TextInputEditText> editableViews;
	// bind to views using Butterknife, need to unbind when used in Fragment
	private Unbinder unbinder;
	@BindView(R.id.edit_ssn)
	TextInputEditText editSsn;
	@BindView(R.id.edit_ssn1)
	TextInputEditText editSsn1;
	@BindView(R.id.edit_ssn2)
	TextInputEditText editSsn2;
	@BindView(R.id.edit_driver_lic)
	TextInputEditText editDriverLic;
	@BindView(R.id.edit_fsa_username)
	TextInputEditText editFsaUsername;
	@BindView(R.id.edit_fsa_password)
	TextInputEditText editFsaPassword;
	@BindView(R.id.edit_application_email)
	TextInputEditText editApplicationEmail;
	@BindView(R.id.edit_application_password)
	TextInputEditText editApplicationPassword;

	MyPlanRepository repository;
	LiveData<List<Password>> allPasswords;
	// TODO
	// setup controls in layout for all the fields
	// validate data and perhaps use the TextInputLayout error message to show invalid fields
	// when typing SSN number we only allow numbers, nice to format as XXX-XX-XXXX once control looses focus
	// create a manager to hold data, iOS calls it MyPlanDataManager
	// 		- this manager must persist / encrypt data
	// rotation should remember lock state / current data entry changes
	// correctly lock/unlock with pin
	// when we first come into fragment with no data entered then show fields unlocked / force PIN creating
	// support fingerprint id as iOS does
	// make sure all fields can be viewed when scrolling, with/without keyboard visible
	// other ??

	public PasswordsFragment()
	{
		// Required empty public constructor
	}

	private SecretKey secretKey = null;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// enable the action Lock/Unlock
		setHasOptionsMenu(true);

//		repository = MyPlanRepository.getInstance(MyPlanDatabase.getDatabase(getContext()));
//		allPasswords = repository.getAllPasswords();
//
//		allPasswords.observe(this, new Observer<List<Password>>()
//		{
//			@Override
//			public void onChanged(@Nullable List<Password> passwords)
//			{
//				Log.d(LOG_TAG, "Passwords has changed");
//				if (passwords != null && passwords.size() > 0)
//				{
//					for (Password password : passwords)
//					{
//						if (password.getName().equals("salt"))
//							Utils.d(LOG_TAG, "salt : %s", password.getValue());
//						else
//							Utils.d(LOG_TAG, "Encrypted value post room: %s", password.getValue());
//							Utils.d(LOG_TAG, "Password: %s : %s", password.getName(), KeyStoreHelper.decrypt("gearUpAlias", password.getValue()));
//					}
//
//				}
//			}
//		});

		try
		{

			KeyStoreHelper.createKeys(this.getContext(), "gearUpAlias");

//			String e = KeyStoreHelper.encrypt("gearUpAlias", "Hello world testing this?");
//			Utils.d(LOG_TAG, "Encrypted: %s", e);
//			String d = KeyStoreHelper.decrypt("gearUpAlias", e);
//			Utils.d(LOG_TAG, "Decrypted: %s", d);


		}
		catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | NoSuchProviderException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_passwords, container, false);
		unbinder = ButterKnife.bind(this, view);

		// keep a list of all edit controls so we can show/hide values when we lock/unlock
		editableViews = Arrays.asList(editSsn, editSsn1, editSsn2, editDriverLic, editFsaUsername,
			editFsaPassword, editApplicationEmail, editApplicationPassword);

		toggleEditableValues(true);

		toggleEditableValues(true);

		// TODO: setup controls with current values

		SharedPreferences sharedPreferences = getActivity().getSharedPreferences(GEAR_UP_PREFERENCES, Context.MODE_PRIVATE);
		String editSsnValue = sharedPreferences.getString(EDIT_SSN_KEY, "");
		String editSsn1Value = sharedPreferences.getString(EDIT_SSN_1_KEY, "");
		String editSsn2Value = sharedPreferences.getString(EDIT_SSN_2_KEY, "");
		String editDriverLicValue = sharedPreferences.getString(EDIT_DRIVER_LIC_KEY, "");
		String fsaUserNameValue = sharedPreferences.getString(FSA_USERNAME_KEY, "");
		String fsaPasswordValue = sharedPreferences.getString(FSA_PASSWORD_KEY, "");
		String scholarshipEmailValue = sharedPreferences.getString(SCHOLARSHIP_EMAIL_KEY, "");
		String scholarshipPasswordValue = sharedPreferences.getString(SCHOLARSHIP_PASSWORD_KEY, "");

		editSsn.setText(KeyStoreHelper.decrypt(KEYSTORE_ALIAS, editSsnValue));
		editSsn1.setText(KeyStoreHelper.decrypt(KEYSTORE_ALIAS, editSsn1Value));
		editSsn2.setText(KeyStoreHelper.decrypt(KEYSTORE_ALIAS, editSsn2Value));
		editDriverLic.setText(KeyStoreHelper.decrypt(KEYSTORE_ALIAS, editDriverLicValue));
		editFsaUsername.setText(KeyStoreHelper.decrypt(KEYSTORE_ALIAS, fsaUserNameValue));
		editFsaPassword.setText(KeyStoreHelper.decrypt(KEYSTORE_ALIAS, fsaPasswordValue));
		editApplicationEmail.setText(KeyStoreHelper.decrypt(KEYSTORE_ALIAS, scholarshipEmailValue));
		editApplicationPassword.setText(KeyStoreHelper.decrypt(KEYSTORE_ALIAS, scholarshipPasswordValue));

		return view;
	}

	@Override
	public void onDestroyView()
	{
		super.onDestroyView();
		unbinder.unbind();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.menu_lock, menu);

		if (locked)
			menu.getItem(0).setTitle(R.string.unlock);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.action_toggle_lock)
		{
			if (locked)
			{
				// 'unlock' so we see our values
				toggleEditableValues(true);
				locked = false;
			}
			else
			{
				// 'lock' so we hide our values
				toggleEditableValues(false);
				locked = true;

				for (TextInputEditText view : editableViews)
				{
					String encryptedValue = KeyStoreHelper.encrypt(KEYSTORE_ALIAS, view.getText().toString());
					Utils.d(LOG_TAG, "Encrypted value pre room: %s", encryptedValue);
					Utils.d(LOG_TAG, "Decrypted Value pre room: %s", KeyStoreHelper.decrypt(KEYSTORE_ALIAS, encryptedValue));

					String name = null;

					switch (view.getId())
					{
					case R.id.edit_ssn:
						name = EDIT_SSN_KEY;
						break;
					case R.id.edit_ssn1:
						name = EDIT_SSN_1_KEY;
						break;
					case R.id.edit_ssn2:
						name = EDIT_SSN_2_KEY;
						break;
					case R.id.edit_driver_lic:
						name = EDIT_DRIVER_LIC_KEY;
						break;
					case R.id.edit_fsa_username:
						name = FSA_USERNAME_KEY;
						break;
					case R.id.edit_fsa_password:
						name = FSA_PASSWORD_KEY;
						break;
					case R.id.edit_application_email:
						name = SCHOLARSHIP_EMAIL_KEY;
						break;
					case R.id.edit_application_password:
						name = SCHOLARSHIP_PASSWORD_KEY;
						break;
					}

					SharedPreferences sharedPreferences = getActivity().getSharedPreferences(GEAR_UP_PREFERENCES, Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = sharedPreferences.edit();

					if (name != null && encryptedValue != null)
					{
						editor.putString(name, encryptedValue);
						editor.apply();
					}
				}

			}

			// force update of our menu
			getActivity().invalidateOptionsMenu();

//			if (allPasswords.getValue() != null)
//			{
//				Utils.d(LOG_TAG, "entries : %d", allPasswords.getValue().size());
//				for (Password password : allPasswords.getValue())
//				{
//					Utils.d(LOG_TAG, "%s + %s", password.getName(), password.getValue());
//				}
//			}



			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onAttach(Context context)
	{
		super.onAttach(context);
		if (context instanceof OnFragmentInteractionListener)
		{
			mListener = (OnFragmentInteractionListener)context;
		}
		else
		{
			throw new RuntimeException(context.toString()
				+ " must implement OnFragmentInteractionListener");
		}

		Utils.d(LOG_TAG, "onAttach");
	}

	@Override
	public void onDetach()
	{
		super.onDetach();
		mListener = null;
	}

	private void toggleEditableValues(boolean showCharacters)
	{
		if (editableViews != null)
		{
			for (EditText editableView : editableViews)
			{
				if (showCharacters)
				{
					editableView.setTransformationMethod(null);
//					String encryptedValue = editableView.getText().toString();

//					String decryptedValue = decryptData(encryptedValue.getBytes());
//					editableView.setText(decryptedValue);
				}
				else
				{
					editableView.setTransformationMethod(new PasswordTransformationMethod());
//					String decryptedValue = editableView.getText().toString();

//					String encryptedValue = Base64.encodeToString(encryptData(decryptedValue), Base64.DEFAULT);;
//					editableView.setText(encryptedValue);
				}

			}
		}
	}

//	private void generateKeyOne(String alias)
//		throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException
//	{
//		/*
// 		 * Generate a new EC key pair entry in the Android Keystore by
//		 * using the KeyPairGenerator API. The private key can only be
//		 * used for signing or verification and only with SHA-256 or
//		 * SHA-512 as the message digest.
// 		 */
//		KeyPairGenerator kpg = KeyPairGenerator.getInstance(
//			KeyProperties.KEY_ALGORITHM_EC, "AndroidKeyStore");
//		kpg.initialize(new KeyGenParameterSpec.Builder(
//			alias,
//			KeyProperties.PURPOSE_SIGN | KeyProperties.PURPOSE_VERIFY)
//			.setDigests(KeyProperties.DIGEST_SHA256,
//				KeyProperties.DIGEST_SHA512)
//			.build());
//
//		KeyPair kp = kpg.generateKeyPair();
//	}

//	private static SecretKey generateKey(char[] passphraseOrPin, MyPlanRepository repository) throws NoSuchAlgorithmException,
//		InvalidKeySpecException, KeyStoreException
//	{
//		KeyStore ks = null;
//		SecretKey secretKey = null;
//		KeyStore.PasswordProtection protectionParameter = new KeyStore.PasswordProtection(passphraseOrPin);
//
//		try
//		{
//			ks = KeyStore.getInstance("AndroidKeyStore");
//
//			ks.load(null, null);
//
//			KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) ks.getEntry("gearUpKey", protectionParameter);
//			if (secretKeyEntry != null)
//				secretKey = secretKeyEntry.getSecretKey();
//		}
//		catch (KeyStoreException | UnrecoverableEntryException | CertificateException | IOException e)
//		{
//			e.printStackTrace();
//		}
//
//		if (secretKey == null)
//		{
//			// Number of PBKDF2 hardening rounds to use. Larger values increase
//			// computation time. You should select a value that causes computation
//			// to take >100ms.
//			final int iterations = 1000;
//
//			// Generate a 256-bit key
//			final int outputKeyLength = 256;
//
//			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
//			KeySpec keySpec = new PBEKeySpec(passphraseOrPin, generateSalt(repository), iterations, outputKeyLength);
//			secretKey = secretKeyFactory.generateSecret(keySpec);
//
//			KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry(secretKey);
//			if (ks != null)
//				ks.setEntry("gearUpKey", secretKeyEntry, protectionParameter);
//		}
//
//		return secretKey;
//	}

//
//
//	private static byte[] generateSalt(MyPlanRepository repository)
//	{
//		// First see if we already generated this
//		LiveData<List<Password>> passwordsLD = repository.getAllPasswords();
//		List<Password> passwords = passwordsLD.getValue();
//
//		if (passwords != null)
//		{
//			for (Password password : passwords)
//			{
//				if (password.getName().equals("salt"))
//				{
//					return Base64.decode(password.getValue(), Base64.DEFAULT);
//				}
//			}
//		}
//
//		// If hasn't been created than create a new value and store it for later
//
//		SecureRandom random = new SecureRandom();
//		byte bytes[] = new byte[512];
//		random.nextBytes(bytes);
//
//		repository.insertPassword("salt", Base64.encodeToString(bytes, Base64.DEFAULT));
//
//		return bytes;
//	}

//	private byte[] encryptData(String dataToEncrypt)
//	{
//		byte[] encodedBytes = null;
//
//		try
//		{
//			Cipher c = Cipher.getInstance("AES/CBC/PKCS5PADDING");
//			c.init(Cipher.ENCRYPT_MODE, secretKey);
//			encodedBytes = c.doFinal(dataToEncrypt.getBytes());
//		}
//		catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e)
//		{
//			e.printStackTrace();
//		}
//
//		return encodedBytes;
//	}
//
//	private String decryptData(byte[] dataToDecrypt)
//	{
//		byte[] decodedBytes = null;
//
//		try
//		{
//			Cipher c = Cipher.getInstance("AES/CBC/PKCS5PADDING");
//			c.init(Cipher.DECRYPT_MODE, secretKey);
//			decodedBytes = c.doFinal(dataToDecrypt);
//		}
//		catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e)
//		{
//			e.printStackTrace();
//		}
//
//		if (decodedBytes != null)
//			return new String(decodedBytes);
//
//		return null;
//	}

	public class PasswordTextWatcher implements TextWatcher {

		private View view;
		private MyPlanRepository repository;

		public PasswordTextWatcher(View v, MyPlanRepository repository)
		{
			this.view = v;
			this.repository = repository;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after)
		{

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count)
		{

		}

		@Override
		public void afterTextChanged(Editable s)
		{

		}
	}

//	public void TestEncryptData(String dataToEncrypt)
//		throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException
//	{
//		// generate a new public/private key pair to test with (note. you should only do this once and keep them!)
//		KeyPair kp = getKeyPair();
//
//		PublicKey publicKey = kp.getPublic();
//		byte[] publicKeyBytes = publicKey.getEncoded();
//		String publicKeyBytesBase64 = new String(Base64.encode(publicKeyBytes, Base64.DEFAULT));
//
//		PrivateKey privateKey = kp.getPrivate();
//		byte[] privateKeyBytes = privateKey.getEncoded();
//		String privateKeyBytesBase64 = new String(Base64.encode(privateKeyBytes, Base64.DEFAULT));
//
//		// test encryption
//		String encrypted = encryptRSAToString(dataToEncrypt, publicKeyBytesBase64);
//
//		// test decryption
//		String decrypted = decryptRSAToString(encrypted, privateKeyBytesBase64);
//	}

//	public static KeyPair getKeyPair()
//		throws InvalidAlgorithmParameterException, NoSuchProviderException, NoSuchAlgorithmException
//	{
//		/*
//		 * Generate a new EC key pair entry in the Android Keystore by
//		 * using the KeyPairGenerator API. The private key can only be
//		 * used for signing or verification and only with SHA-256 or
//		 * SHA-512 as the message digest.
//		 */
//		KeyPairGenerator kpg = KeyPairGenerator.getInstance(
//			KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore");
//		kpg.initialize(new KeyGenParameterSpec.Builder(
//			"gearUpPrivateKey",
//			KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
//			.setDigests(KeyProperties.DIGEST_SHA256,
//				KeyProperties.DIGEST_SHA512)
//			.build());
//
//		return kpg.generateKeyPair();
//	}

//	public static KeyPair getKeyPair() {
//		KeyPair kp = null;
//		try {
//			KeyPairGenerator kpg = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA);
//			kpg.initialize(2048);
//			kp = kpg.generateKeyPair();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return kp;
//	}

//	private String privateKey;
//	private String publicKey;
//
//	public void generateKeyPair()
//		throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException,
//		UnrecoverableEntryException, NoSuchProviderException, InvalidAlgorithmParameterException
//	{
//		// Get private key
//		KeyStore keyStore = null;
//
//		keyStore = KeyStore.getInstance("AndroidKeyStore");
//
//		keyStore.load(null);
//
//		KeyStore.PrivateKeyEntry privateKeyEntry =
//			(KeyStore.PrivateKeyEntry)keyStore.getEntry("gearUpPrivateKey", null);
//
//		if (privateKeyEntry != null)
//			this.privateKey = Base64.encodeToString(privateKeyEntry.getPrivateKey().getEncoded(), Base64.DEFAULT);
//
//		// get public key
//		List<Password> passwords = allPasswords.getValue();
//
//		if (passwords != null)
//			for (Password p : passwords)
//				if (p.getName().equals("publicKey"))
//					this.publicKey = p.getValue();
//
//		if (this.publicKey == null || this.privateKey == null)
//		{
//			KeyPair kp = getKeyPair();
//
//			this.publicKey = Base64.encodeToString(kp.getPublic().getEncoded(), Base64.DEFAULT);
//			this.privateKey = Base64.encodeToString(kp.getPrivate().getEncoded(), Base64.DEFAULT);
//
//			repository.insertPassword("publicKey", publicKey);
//
//			CertificateFactory certificateFactory;
//			certificateFactory = CertificateFactory.getInstance("X.509");
//			Certificate certificate = certificateFactory.generateCertificate()
//
//			KeyStore.Entry pke = new KeyStore.PrivateKeyEntry(kp.getPrivate(), certificates);
//			keyStore.setEntry("gearUpPrivateKey", pke, null);
//		}
//	}
//
//	public static String encryptRSAToString(String clearText, String publicKey) {
//		String encryptedBase64 = "";
//		try {
//			KeyFactory keyFac = KeyFactory.getInstance("RSA");
//			KeySpec keySpec = new X509EncodedKeySpec(Base64.decode(publicKey.trim().getBytes(), Base64.DEFAULT));
//			Key key = keyFac.generatePublic(keySpec);
//
//			// get an RSA cipher object and print the provider
//			final Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
//			// encrypt the plain text using the public key
//			cipher.init(Cipher.ENCRYPT_MODE, key);
//
//			byte[] encryptedBytes = cipher.doFinal(clearText.getBytes("UTF-8"));
//			encryptedBase64 = new String(Base64.encode(encryptedBytes, Base64.DEFAULT));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return encryptedBase64.replaceAll("(\\r|\\n)", "");
//	}
//
//	public static String decryptRSAToString(String encryptedBase64, String privateKey) {
//
//		String decryptedString = "";
//		try {
//			KeyFactory keyFac = KeyFactory.getInstance("RSA");
//			KeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decode(privateKey.trim().getBytes(), Base64.DEFAULT));
//			Key key = keyFac.generatePrivate(keySpec);
//
//			// get an RSA cipher object and print the provider
//			final Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
//			// encrypt the plain text using the public key
//			cipher.init(Cipher.DECRYPT_MODE, key);
//
//			byte[] encryptedBytes = Base64.decode(encryptedBase64, Base64.DEFAULT);
//			byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
//			decryptedString = new String(decryptedBytes);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return decryptedString;
//	}
}

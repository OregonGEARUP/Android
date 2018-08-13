package org.oregongoestocollege.itsaplan;

import java.util.Arrays;
import java.util.List;

import android.app.AlertDialog;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
public class PasswordsFragment extends Fragment implements CreatePinFragment.OnPinCreatedListener, EnterPinFragment.OnPinEnteredListener
{
	private static final String LOG_TAG = "GearUpPasswordsFrag";

	public static final String GEAR_UP_PREFERENCES = "gearUpSharedPreferences";
	public static final String IS_FIRST_TIME_PASSWORDS = "passwordsIsFirstTime";

	public static final String EDIT_SSN_KEY = "edit_ssn";
	public static final String EDIT_SSN_1_KEY = "edit_ssn1";
	public static final String EDIT_SSN_2_KEY = "edit_ssn2";
	public static final String EDIT_DRIVER_LIC_KEY = "edit_driver_lic";
	public static final String FSA_USERNAME_KEY = "fsa_username";
	public static final String FSA_PASSWORD_KEY = "fsa_password";
	public static final String APPLICATION_EMAIL_KEY = "scholarship_email";
	public static final String APPLICATION_PASSWORD_KEY = "scholarship_password";
	public static final String ORSAA_USERNAME_KEY = "orsaa_username";
	public static final String ORSAA_PASSWORD_KEY = "orsaa_password";
	public static final String CSS_USERNAME_KEY = "css_username";
	public static final String CSS_PASSWORD_KEY = "css_password";
	public static final String EXTRA_LOGIN_1_ORGANIZATION = "extra_1_organization";
	public static final String EXTRA_LOGIN_1_USERNAME = "extra_1_username";
	public static final String EXTRA_LOGIN_1_PASSWORD = "extra_1_password";
	public static final String EXTRA_LOGIN_2_ORGANIZATION = "extra_2_organization";
	public static final String EXTRA_LOGIN_2_USERNAME = "extra_2_username";
	public static final String EXTRA_LOGIN_2_PASSWORD = "extra_2_password";
	public static final String OSAC_USERNAME = "osac_username";
	public static final String OSAC_PASSWORD = "osac_password";

	private OnFragmentInteractionListener mListener;
	// tracks the locked / unlocked state of the controls
	private boolean locked = true;
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
	@BindView(R.id.edit_orsaa_username)
	TextInputEditText editOrsaaUsername;
	@BindView(R.id.edit_orsaa_password)
	TextInputEditText editOrsaaPassword;
	@BindView(R.id.edit_css_username)
	TextInputEditText editCssUsername;
	@BindView(R.id.edit_css_password)
	TextInputEditText editCssPassword;
	@BindView(R.id.edit_extra_login_1_organization)
	TextInputEditText editExtraLogin1Organization;
	@BindView(R.id.edit_extra_login_1_username)
	TextInputEditText editExtraLogin1Username;
	@BindView(R.id.edit_extra_login_1_password)
	TextInputEditText editExtraLogin1Password;
	@BindView(R.id.edit_extra_login_2_organization)
	TextInputEditText editExtraLogin2Organization;
	@BindView(R.id.edit_extra_login_2_username)
	TextInputEditText editExtraLogin2Username;
	@BindView(R.id.edit_extra_login_2_password)
	TextInputEditText editExtraLogin2Password;
	@BindView(R.id.edit_application_email)
	TextInputEditText editApplicationEmail;
	@BindView(R.id.edit_application_password)
	TextInputEditText editApplicationPassword;
	@BindView(R.id.edit_osac_username)
	TextInputEditText editOsacUsername;
	@BindView(R.id.edit_osac_password)
	TextInputEditText editOsacPassword;


	MyPlanRepository repository;
	LiveData<List<Password>> allPasswords;

	SharedPreferences sharedPreferences;

	boolean isFirstTime;
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

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// check if this is the first time coming to the password fragment
		sharedPreferences = getActivity().getSharedPreferences(GEAR_UP_PREFERENCES, Context.MODE_PRIVATE);
		isFirstTime = sharedPreferences.getBoolean(IS_FIRST_TIME_PASSWORDS, true);

		// set the flag so in the future we know we've been here before
		if (isFirstTime)
			sharedPreferences.edit().putBoolean(IS_FIRST_TIME_PASSWORDS, false).apply();

		// enable the action Lock/Unlock
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_passwords, container, false);
		unbinder = ButterKnife.bind(this, view);

		// keep a list of all edit controls so we can show/hide values when we lock/unlock
		editableViews = Arrays.asList(editSsn, editSsn1, editSsn2, editDriverLic, editFsaUsername, editFsaPassword,
			editApplicationEmail, editApplicationPassword, editOrsaaUsername, editOrsaaPassword, editCssUsername,
			editCssPassword, editExtraLogin1Organization, editExtraLogin1Username, editExtraLogin1Password,
			editExtraLogin2Organization, editExtraLogin2Username, editExtraLogin2Password, editOsacUsername, editOsacPassword);

		// if this is the first time we don't need to start locked
		toggleEditableValues(isFirstTime);
		locked = !isFirstTime;

		if (isFirstTime)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder
				.setMessage(R.string.setup_pin_message)
				.setPositiveButton(R.string.setup_pin_button_text, (dialog, which) ->
				{
					// todo start create a pin dialog
					FragmentManager fm = getActivity().getSupportFragmentManager();
					CreatePinFragment cpf = new CreatePinFragment();
					cpf.setCallback(PasswordsFragment.this);
					cpf.show(fm, "create_pin_fragment");
				});
			builder.create().show();


		}


		repository = MyPlanRepository.getInstance(getActivity());
		allPasswords = repository.getAllPasswords();

		allPasswords.observe(this, passwords ->
		{
			if (passwords != null)
			{
				TextInputEditText v = null;

				for (Password password : passwords)
				{
					switch (password.getName())
					{
					case EDIT_SSN_KEY:
						v = editSsn;
						break;
					case EDIT_SSN_1_KEY:
						v = editSsn1;
						break;
					case EDIT_SSN_2_KEY:
						v = editSsn2;
						break;
					case EDIT_DRIVER_LIC_KEY:
						v = editDriverLic;
						break;
					case FSA_USERNAME_KEY:
						v = editFsaUsername;
						break;
					case FSA_PASSWORD_KEY:
						v = editFsaPassword;
						break;
					case APPLICATION_EMAIL_KEY:
						v = editApplicationEmail;
						break;
					case APPLICATION_PASSWORD_KEY:
						v = editApplicationPassword;
						break;
					case ORSAA_USERNAME_KEY:
						v = editOrsaaUsername;
						break;
					case ORSAA_PASSWORD_KEY:
						v = editOrsaaPassword;
						break;
					case CSS_USERNAME_KEY:
						v = editCssUsername;
						break;
					case CSS_PASSWORD_KEY:
						v = editCssPassword;
						break;
					case EXTRA_LOGIN_1_ORGANIZATION:
						v = editExtraLogin1Organization;
						break;
					case EXTRA_LOGIN_1_USERNAME:
						v = editExtraLogin1Username;
						break;
					case EXTRA_LOGIN_1_PASSWORD:
						v = editExtraLogin1Password;
						break;
					case EXTRA_LOGIN_2_ORGANIZATION:
						v = editExtraLogin2Organization;
						break;
					case EXTRA_LOGIN_2_USERNAME:
						v = editExtraLogin2Username;
						break;
					case EXTRA_LOGIN_2_PASSWORD:
						v = editExtraLogin2Password;
						break;
					case OSAC_USERNAME:
						v = editOsacUsername;
						break;
					case OSAC_PASSWORD:
						v = editOsacPassword;
						break;
					}

					if (v != null)
						v.setText(password.getValue());
				}
			}
		});

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
				FragmentManager fm = getActivity().getSupportFragmentManager();
				EnterPinFragment epf = new EnterPinFragment();
				epf.setCallback(this);
				epf.show(fm, "enter_pin_fragment");
			}
			else
			{
				// 'lock' so we hide our values
				toggleEditableValues(false);
				locked = true;

				for (TextInputEditText view : editableViews)
				{
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
						name = APPLICATION_EMAIL_KEY;
						break;
					case R.id.edit_application_password:
						name = APPLICATION_PASSWORD_KEY;
						break;
					case R.id.edit_orsaa_username:
						name = ORSAA_USERNAME_KEY;
						break;
					case R.id.edit_orsaa_password:
						name = ORSAA_PASSWORD_KEY;
						break;
					case R.id.edit_css_username:
						name = CSS_USERNAME_KEY;
						break;
					case R.id.edit_css_password:
						name = CSS_PASSWORD_KEY;
						break;
					case R.id.edit_extra_login_1_organization:
						name = EXTRA_LOGIN_1_ORGANIZATION;
						break;
					case R.id.edit_extra_login_1_username:
						name = EXTRA_LOGIN_1_USERNAME;
						break;
					case R.id.edit_extra_login_1_password:
						name = EXTRA_LOGIN_1_PASSWORD;
						break;
					case R.id.edit_extra_login_2_organization:
						name = EXTRA_LOGIN_2_ORGANIZATION;
						break;
					case R.id.edit_extra_login_2_username:
						name = EXTRA_LOGIN_2_USERNAME;
						break;
					case R.id.edit_extra_login_2_password:
						name = EXTRA_LOGIN_2_PASSWORD;
						break;
					case R.id.edit_osac_username:
						name = OSAC_USERNAME;
						break;
					case R.id.edit_osac_password:
						name = OSAC_PASSWORD;
						break;
					}

					if (name != null)
						repository.insertPassword(name, view.getText().toString());
				}
			}

			// force update of our menu
			getActivity().invalidateOptionsMenu();

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
					editableView.setTransformationMethod(null);
				else
					editableView.setTransformationMethod(new PasswordTransformationMethod());
			}
		}
	}

	@Override
	public void onPinCreated(String pin)
	{
		sharedPreferences.edit().putString("passwords_pin", pin).apply();
	}

	@Override
	public void onPinEntered()
	{
		toggleEditableValues(true);
		locked = false;
		getActivity().invalidateOptionsMenu();
	}
}

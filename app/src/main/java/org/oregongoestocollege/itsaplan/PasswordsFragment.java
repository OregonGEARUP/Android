package org.oregongoestocollege.itsaplan;

import java.util.Arrays;
import java.util.List;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
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
public class PasswordsFragment extends Fragment
{
	private static final String LOG_TAG = "GearUpPasswordsFrag";

	public static final String EDIT_SSN_KEY = "edit_ssn";
	public static final String EDIT_SSN_1_KEY = "edit_ssn1";
	public static final String EDIT_SSN_2_KEY = "edit_ssn2";
	public static final String EDIT_DRIVER_LIC_KEY = "edit_driver_lic";
	public static final String FSA_USERNAME_KEY = "fsa_username";
	public static final String FSA_PASSWORD_KEY = "fsa_password";
	public static final String APPLICATION_EMAIL_KEY = "scholarship_email";
	public static final String APPLICATION_PASSWORD_KEY = "scholarship_password";

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

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

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
			editApplicationEmail, editApplicationPassword);

		toggleEditableValues(false);

		// TODO: setup controls with current values


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
}

package org.oregongoestocollege.itsaplan;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.oregongoestocollege.itsaplan.support.GearUpSharedPreferences;
import org.oregongoestocollege.itsaplan.viewmodel.PasswordsViewModel;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class PasswordContainerFragment extends BaseFragment
	implements OnFragmentInteractionListener, PasswordPinFragment.PasswordPinListener
{
	private static final String LOG_TAG = "GearUp_PasswordContainerFrag";
	private static final String FRAG_PASSWORD_PIN = "frag-pwd-pin";
	private boolean isPinCreated;
	private PasswordsViewModel viewModel;

	public PasswordContainerFragment()
	{
		// Required empty public constructor
	}

	private void showPasswordPin(boolean createPin)
	{
		PasswordPinFragment fragment = PasswordPinFragment.newInstance(createPin);
		fragment.setCallback(this);

		getChildFragmentManager().beginTransaction()
			.replace(R.id.frame, fragment, FRAG_PASSWORD_PIN).commit();

		// only show the back button when entering PIN (not create!)
		setHomeAsUpEnabled(!createPin);
	}

	private void showPasswords(boolean lock)
	{
		// setup the view model before showing
		viewModel.lockAll(lock);

		Fragment fragment = PasswordsFragment.newInstance();

		getChildFragmentManager().beginTransaction()
			.replace(R.id.frame, fragment).commit();

		setHomeAsUpEnabled(false);
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
		@Nullable Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.fragment_password_container, container, false);

		Utils.d(LOG_TAG, "onCreateView");

		isPinCreated = GearUpSharedPreferences.isPasswordsPinCreated(getContext());

		// scope the view model to this fragment, child fragments will do the same so it's
		// retained across pin lock/unlock, in our case we want it to save data when appropriate
		viewModel = ViewModelProviders.of(this).get(PasswordsViewModel.class);
		viewModel.init();

		// we only need to create the fragments if there is no instance state
		if (savedInstanceState == null)
		{
			if (isPinCreated)
				showPasswords(true);
			else
				showPasswordPin(true);
		}
		else
		{
			Fragment fragment = getChildFragmentManager().findFragmentByTag(FRAG_PASSWORD_PIN);
			if (fragment instanceof PasswordPinFragment)
			{
				// hookup the callback again
				PasswordPinFragment passwordPinFragment = (PasswordPinFragment)fragment;
				passwordPinFragment.setCallback(this);
				setHomeAsUpEnabled(!passwordPinFragment.isModeCreate());
			}
			else
				setHomeAsUpEnabled(false);
		}

		return v;
	}

	@Override
	public void handleTabChanged(boolean hidden)
	{
		// for now we don't need to save password data since the data is not used
		// in any of our other tabs, wait till we get an onStop() to save

		// however, we do want to lock the tab...
		if (hidden && viewModel != null)
			viewModel.lockAll(true);
	}

	@Override
	public boolean handleBackPressed()
	{
		Utils.d(LOG_TAG, "handleBackPressed");

		Fragment fragment = getChildFragmentManager().findFragmentByTag(FRAG_PASSWORD_PIN);
		if (fragment instanceof PasswordPinFragment && !((PasswordPinFragment)fragment).isModeCreate())
		{
			// show passwords again but keep it locked since user did not enter pin
			showPasswords(true);
			return true;
		}

		return false;
	}

	@Override
	public boolean canHandleBackPressed()
	{
		Utils.d(LOG_TAG, "canHandleBackPressed");

		Fragment fragment = getChildFragmentManager().findFragmentByTag(FRAG_PASSWORD_PIN);
		if (fragment instanceof PasswordPinFragment)
			return !((PasswordPinFragment)fragment).isModeCreate();

		return false;
	}

	@Override
	public void onPinCreated()
	{
		// We should only get here from inside the PasswordPinFragment which has already
		// stored the newly created PIN
		isPinCreated = true;

		// we just created the pin, show the PasswordsFragment unlocked
		showPasswords(false);
	}

	@Override
	public void onPinEntered()
	{
		// we correctly entered our pin, show the PasswordsFragment unlocked
		showPasswords(false);
	}

	public void launchUnlockFragment()
	{
		showPasswordPin(false);
	}

	@Override
	public void onStop()
	{
		super.onStop();
		Utils.d(LOG_TAG, "onStop");

		// fragment is getting destroyed, make sure all our data is saved
		if (viewModel != null)
			viewModel.saveAll();
	}
}

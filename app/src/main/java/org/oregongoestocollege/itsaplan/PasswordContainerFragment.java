package org.oregongoestocollege.itsaplan;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class PasswordContainerFragment extends Fragment implements PasswordPinFragment.PasswordPinListener
{
	public static final String GEAR_UP_PREFERENCES = "gearUpSharedPreferences";
	public static final String PIN_PREFERENCES_KEY = "passwords_pin";
	private static final String FRAG_PASSWORD_PIN = "frag-pwd-pin";
	private SharedPreferences sharedPreferences;
	private boolean hasPinSet;

	public PasswordContainerFragment()
	{
		// Required empty public constructor
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
		@Nullable Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.fragment_password_container, container, false);

		sharedPreferences = getContext().getSharedPreferences(GEAR_UP_PREFERENCES, Context.MODE_PRIVATE);
		hasPinSet = sharedPreferences.getString(PIN_PREFERENCES_KEY, null) != null;

		// we only need to create the fragments if there is no instance state
		if (savedInstanceState == null)
		{
			Fragment fragment;

			if (hasPinSet)
				fragment = PasswordsFragment.newInstance(true);
			else
			{
				PasswordPinFragment passwordPinFragment = PasswordPinFragment.newInstance(true);
				passwordPinFragment.setCallback(this);
				fragment = passwordPinFragment;
			}

			getChildFragmentManager().beginTransaction().add(R.id.frame, fragment, FRAG_PASSWORD_PIN).commit();
		}
		else
		{
			Fragment fragment = getChildFragmentManager().findFragmentByTag(FRAG_PASSWORD_PIN);
			if (fragment instanceof PasswordPinFragment)
				((PasswordPinFragment)fragment).setCallback(this);
		}

		return v;
	}

	@Override
	public void onPause()
	{
		super.onPause();
	}

	@Override
	public void onPinCreated(String pin)
	{
		// We should only get here from inside the PasswordPinFragment.
		sharedPreferences.edit().putString(PIN_PREFERENCES_KEY, pin).apply();

		Fragment fragment = PasswordsFragment.newInstance(false);

		getChildFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.frame, fragment).commit();
	}

	@Override
	public void onPinEntered()
	{
		// We've correctly entered our pin, unlock entries in PasswordsFragment
		Fragment fragment = PasswordsFragment.newInstance(false);

		getChildFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.frame, fragment).commit();
	}

	public void launchUnlockFragment()
	{
		PasswordPinFragment fragment = PasswordPinFragment.newInstance(false);
		fragment.setCallback(this);

		getChildFragmentManager().beginTransaction().replace(R.id.frame, fragment).commit();
	}
}

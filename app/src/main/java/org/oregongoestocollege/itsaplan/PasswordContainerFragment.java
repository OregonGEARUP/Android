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

public class PasswordContainerFragment extends Fragment implements CreatePinFragment.OnPinCreatedListener,
	EnterPinFragment.OnPinEnteredListener
{
	private static final String LOG_TAG = "GearUpPasswordContainerFragment";

	public static final String GEAR_UP_PREFERENCES = "gearUpSharedPreferences";
	public static final String PIN_PREFERENCES_KEY = "passwords_pin";

	private SharedPreferences sharedPreferences;
	private boolean hasPinSet;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		sharedPreferences = getActivity().getSharedPreferences(GEAR_UP_PREFERENCES, Context.MODE_PRIVATE);
		hasPinSet = sharedPreferences.getString(PIN_PREFERENCES_KEY, null) != null;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
		@Nullable Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_password_container, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		Fragment fragment;

		if (hasPinSet)
			fragment = PasswordsFragment.newInstance(true);
		else
		{
			fragment = new CreatePinFragment();
			((CreatePinFragment)fragment).setCallback(this);
		}

		getChildFragmentManager().beginTransaction().add(R.id.frame, fragment).commit();
	}

	@Override
	public void onPause()
	{
		super.onPause();
	}

	@Override
	public void onPinCreated(String pin)
	{
		// We should only get here from inside the CreatePinFragment.
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
		Fragment fragment = new EnterPinFragment();
		((EnterPinFragment)fragment).setCallback(this);

		getChildFragmentManager().beginTransaction().replace(R.id.frame, fragment).commit();
	}
}

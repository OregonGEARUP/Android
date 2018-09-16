package org.oregongoestocollege.itsaplan;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.oregongoestocollege.itsaplan.databinding.FragmentPasswordsBinding;
import org.oregongoestocollege.itsaplan.viewmodel.PasswordsViewModel;

/**
 * PasswordsFragment - handles data entry for password fields.
 *
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class PasswordsFragment extends Fragment
{
	private static final String LOG_TAG = "GearUp_PasswordsFrag";
	private static final String ARGUMENT_KEY_IS_LOCKED = "is_locked_argument_key";
	// tracks the locked / unlocked state of the controls
	private boolean locked = true;
	private PasswordsViewModel viewModel;

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
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		FragmentPasswordsBinding
			binding = DataBindingUtil.inflate(inflater, R.layout.fragment_passwords, container, false);
		View v = binding.getRoot();

		// if user just created/entered a PIN then we don't start locked
		Bundle bundle = getArguments();
		locked = bundle == null || bundle.getBoolean(ARGUMENT_KEY_IS_LOCKED, true);

		viewModel = ViewModelProviders.of(getParentFragment()).get(PasswordsViewModel.class);
		viewModel.init(locked);

		// bind to fragment
		binding.setUxContext(viewModel);

		return v;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		super.onCreateOptionsMenu(menu, inflater);

		menu.clear();
		inflater.inflate(R.menu.menu_lock, menu);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu)
	{
		super.onPrepareOptionsMenu(menu);

		MenuItem item = menu.findItem(R.id.action_toggle_lock);
		if (item != null)
		{
			if (locked)
				item.setTitle(R.string.unlock);
			else
				item.setTitle(R.string.lock);
			item.setEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (viewModel != null && item.getItemId() == R.id.action_toggle_lock)
		{
			if (locked)
			{
				Fragment fragment = getParentFragment();

				if (fragment instanceof PasswordContainerFragment)
					((PasswordContainerFragment)fragment).launchUnlockFragment();
			}
			else
			{
				// 'lock' so we hide our values
				locked = true;
				viewModel.lockSecureInfo();
				viewModel.save();
			}

			// force update of our menu
			FragmentActivity activity = getActivity();
			if (activity != null)
				getActivity().invalidateOptionsMenu();

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public static PasswordsFragment newInstance(boolean isLocked)
	{
		Bundle args = new Bundle();
		args.putBoolean(ARGUMENT_KEY_IS_LOCKED, isLocked);

		PasswordsFragment fragment = new PasswordsFragment();
		fragment.setArguments(args);
		return fragment;
	}
}

package org.oregongoestocollege.itsaplan;

import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
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
 * Copyright © 2020 Oregon GEAR UP. All rights reserved.
 */
public class PasswordsFragment extends Fragment
{
	private static final String LOG_TAG = "GearUp_PasswordsFrag";
	private PasswordsViewModel viewModel;

	// TODO
	// validate data and perhaps use the TextInputLayout error message to show invalid fields
	// when typing SSN number we only allow numbers, nice to format as XXX-XX-XXXX once control looses focus
	// lock data when user leaves app, based on timestamp?
	// support fingerprint id as iOS does

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

		// scope the view model to the parent fragment, in this case PasswordContainerFragment
		// so it's retained and we don't re-init / re-load on each pin lock / unlock
		viewModel = ViewModelProviders.of(getParentFragment()).get(PasswordsViewModel.class);

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
			if (viewModel.isLocked())
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
			if (viewModel.isLocked())
			{
				Fragment fragment = getParentFragment();

				if (fragment instanceof PasswordContainerFragment)
					((PasswordContainerFragment)fragment).launchUnlockFragment();
			}
			else
			{
				// 'lock' so we hide our values
				viewModel.lockAll(true);
			}

			// force update of our menu
			FragmentActivity activity = getActivity();
			if (activity != null)
				getActivity().invalidateOptionsMenu();

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public static PasswordsFragment newInstance()
	{
		return new PasswordsFragment();
	}
}

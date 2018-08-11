package org.oregongoestocollege.itsaplan;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.oregongoestocollege.itsaplan.viewmodel.MyPlanViewModel;

/**
 * MyPlanFragment
 * Oregon GEAR UP App
 *
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class MyPlanFragment extends Fragment implements OnFragmentInteractionListener
{
	private Fragment lastFragment;
	private String optionName;

	public MyPlanFragment()
	{
		// Required empty public constructor
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState)
	{
		super.onSaveInstanceState(outState);

		outState.putString(Utils.PARAM_OPTION_NAME, optionName);
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
		@Nullable Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_my_plan, container, false);

		if (savedInstanceState != null)
			optionName = savedInstanceState.getString(Utils.PARAM_OPTION_NAME);

		if (!TextUtils.isEmpty(optionName))
			showOption(optionName);
		else
			showAllOptions();

		return view;
	}

	private void setHomeAsUpEnabled(boolean enabled)
	{
		ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
		if (actionBar != null)
			actionBar.setDisplayHomeAsUpEnabled(enabled);
	}

	private void showAllOptions()
	{
		MyPlanOptionsFragment fragment = new MyPlanOptionsFragment();
		fragment.getClickOptionEvent().observe(this, new Observer<String>()
		{
			@Override
			public void onChanged(@Nullable String s)
			{
				showOption(s);
			}
		});

		FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.fragment_container_myplan, fragment);
		transaction.commit();

		// hide the back button
		setHomeAsUpEnabled(false);
		optionName = null;
	}

	private void showOption(String name)
	{
		if (!TextUtils.isEmpty(name))
		{
			Fragment fragment = null;

			switch (name)
			{
			case MyPlanViewModel.MY_PLAN_COLLEGES:
				fragment = new MyPlanCollegesFragment();
				break;
			case MyPlanViewModel.MY_PLAN_SCHOLARSHIPS:
				fragment = new MyPlanScholarshipsFragment();
				break;
			case MyPlanViewModel.MY_PLAN_TESTS:
				fragment = new MyPlanTestResultsFragment();
				break;
			case MyPlanViewModel.MY_PLAN_RESIDENCY:
				fragment = new MyPlanResidencyFragment();
				break;
			case MyPlanViewModel.MY_PLAN_CALENDAR:
				fragment = new MyPlanCalendarFragment();
				break;
			}

			if (fragment != null)
			{
				FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
				transaction.replace(R.id.fragment_container_myplan, fragment);
				transaction.commit();

				// show the back button
				setHomeAsUpEnabled(true);
				optionName = name;
			}
		}
	}

	@Override
	public boolean handleBackPressed()
	{
		if (!TextUtils.isEmpty(optionName))
		{
			showAllOptions();
			return true;
		}
		return false;
	}

	@Override
	public boolean canHandleBackPressed()
	{
		return (!TextUtils.isEmpty(optionName));
	}
}

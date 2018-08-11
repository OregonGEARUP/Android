package org.oregongoestocollege.itsaplan;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
	private MyPlanViewModel viewModel;

	public MyPlanFragment()
	{
		// Required empty public constructor
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		viewModel = ViewModelProviders.of(getActivity()).get(MyPlanViewModel.class);
		viewModel.getCurrentTask().observe(this, this::showTask);
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
		@Nullable Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_my_plan, container, false);

		if (savedInstanceState == null)
		{
			showTask(null);
		}

		return view;
	}

	private void showTask(String currentTask)
	{
		FragmentManager manager = getActivity().getSupportFragmentManager();
		if (manager == null)
			return;

		if (!TextUtils.isEmpty(currentTask))
		{
			Fragment fragment = null;

			switch (currentTask)
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
				FragmentTransaction transaction = manager.beginTransaction();
				transaction.replace(R.id.fragment_container_myplan, fragment);
				transaction.commit();

				// show the back button
				setHomeAsUpEnabled(true);
			}
		}
		else
		{
			MyPlanOptionsFragment fragment = new MyPlanOptionsFragment();
			FragmentTransaction transaction = manager.beginTransaction();
			transaction.replace(R.id.fragment_container_myplan, fragment);
			transaction.commit();

			// hide the back button
			setHomeAsUpEnabled(false);

			getActivity().setTitle(R.string.title_myplan);
		}
	}

	private void setHomeAsUpEnabled(boolean enabled)
	{
		ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
		if (actionBar != null)
			actionBar.setDisplayHomeAsUpEnabled(enabled);
	}

	@Override
	public boolean handleBackPressed()
	{
		return viewModel.resetTask();
	}

	@Override
	public boolean canHandleBackPressed()
	{
		return (!TextUtils.isEmpty(viewModel.getCurrentTask().getValue()));
	}
}

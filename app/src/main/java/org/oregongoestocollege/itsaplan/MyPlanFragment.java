package org.oregongoestocollege.itsaplan;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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

		Utils.d(MyPlanViewModel.LOG_TAG, "onCreate");

		viewModel = ViewModelProviders.of(getActivity()).get(MyPlanViewModel.class);
		viewModel.getCurrentTask().observe(this, this::showTask);
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
		@Nullable Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_my_plan, container, false);
	}

	private void showTask(String currentTask)
	{
		// make sure to use getChildFragmentManager versus getSupportFragmentManager
		FragmentManager manager = getChildFragmentManager();

		if (!TextUtils.isEmpty(currentTask))
		{
			// we can get here from links in the checklist steps so we want to make sure the
			// requested task isn't already displayed, MyPlanOptionsFragment should always be 0
			Fragment currentFragment = null;
			if (manager.getFragments().size() > 1)
				currentFragment = manager.getFragments().get(1);

			Fragment fragment = null;

			switch (currentTask)
			{
			case MyPlanViewModel.MY_PLAN_COLLEGES:
				if (!(currentFragment instanceof MyPlanCollegesFragment))
					fragment = new MyPlanCollegesFragment();
				break;
			case MyPlanViewModel.MY_PLAN_SCHOLARSHIPS:
				if (!(currentFragment instanceof MyPlanScholarshipsFragment))
					fragment = new MyPlanScholarshipsFragment();
				break;
			case MyPlanViewModel.MY_PLAN_TESTS:
				if (!(currentFragment instanceof MyPlanTestResultsFragment))
					fragment = new MyPlanTestResultsFragment();
				break;
			case MyPlanViewModel.MY_PLAN_RESIDENCY:
				if (!(currentFragment instanceof MyPlanResidencyFragment))
					fragment = new MyPlanResidencyFragment();
				break;
			case MyPlanViewModel.MY_PLAN_CALENDAR:
				if (!(currentFragment instanceof MyPlanCalendarFragment))
					fragment = new MyPlanCalendarFragment();
				break;
			}

			if (fragment != null)
			{
				// remove the current fragment outside of the transaction so when we pop the
				// backstack we only revert the add
				if (currentFragment != null)
					manager.popBackStack();

				FragmentTransaction transaction = manager.beginTransaction();
				transaction.add(R.id.my_plan_container, fragment);
				transaction.addToBackStack(null);
				transaction.commit();
			}

			// show the back button
			setHomeAsUpEnabled(true);

			Utils.d(MyPlanViewModel.LOG_TAG, "showTask %s",
				fragment == null ? "do nothing" : (currentFragment != null ? "pop/add" : "add only"));
		}
		else
		{
			manager.popBackStack();

			// hide the back button
			setHomeAsUpEnabled(false);

			getActivity().setTitle(R.string.title_myplan);

			Utils.d(MyPlanViewModel.LOG_TAG, "showTask pop backstack");
		}
	}

	private void setHomeAsUpEnabled(boolean enabled)
	{
		FragmentActivity activity = getActivity();
		if (activity != null)
		{
			ActionBar actionBar = ((AppCompatActivity)activity).getSupportActionBar();
			if (actionBar != null)
				actionBar.setDisplayHomeAsUpEnabled(enabled);
		}
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

package org.oregongoestocollege.itsaplan;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import org.oregongoestocollege.itsaplan.viewmodel.MyPlanNavViewModel;

/**
 * MyPlanFragment
 * Oregon GEAR UP App
 *
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class MyPlanFragment extends BaseFragment implements OnFragmentInteractionListener
{
	private static final String LOG_TAG = "GearUp_MyPlanFrag";
	private MyPlanNavViewModel viewModel;

	public MyPlanFragment()
	{
		// Required empty public constructor
	}

	void onBackStackChanged()
	{
		// see if we need to set our title from a child fragment
		Utils.updateTitleOnBackStackChanged(this, LOG_TAG);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		if (Utils.DEBUG)
			Utils.d(LOG_TAG, "onCreate");

		viewModel = ViewModelProviders.of(getActivity()).get(MyPlanNavViewModel.class);
		viewModel.getCurrentTask().observe(this, this::showTask);

		getChildFragmentManager().addOnBackStackChangedListener(this::onBackStackChanged);

		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
		@Nullable Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_my_plan, container, false);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu)
	{
		super.onPrepareOptionsMenu(menu);

		Utils.disableMenu(menu);
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
			@StringRes
			int title = 0;

			switch (currentTask)
			{
			case MyPlanNavViewModel.MY_PLAN_COLLEGES:
				if (!(currentFragment instanceof MyPlanCollegesFragment))
				{
					fragment = new MyPlanCollegesFragment();
					title = R.string.colleges;
				}
				break;
			case MyPlanNavViewModel.MY_PLAN_SCHOLARSHIPS:
				if (!(currentFragment instanceof MyPlanScholarshipsFragment))
				{
					fragment = new MyPlanScholarshipsFragment();
					title = R.string.scholarships;
				}
				break;
			case MyPlanNavViewModel.MY_PLAN_TESTS:
				if (!(currentFragment instanceof MyPlanTestResultsFragment))
				{
					fragment = new MyPlanTestResultsFragment();
					title = R.string.actsat;
				}
				break;
			case MyPlanNavViewModel.MY_PLAN_RESIDENCY:
				if (!(currentFragment instanceof MyPlanResidencyFragment))
				{
					fragment = new MyPlanResidencyFragment();
					title = R.string.residency;
				}
				break;
			case MyPlanNavViewModel.MY_PLAN_CALENDAR:
				if (!(currentFragment instanceof MyPlanCalendarFragment))
				{
					fragment = new MyPlanCalendarFragment();
					title = R.string.calendar;
				}
				break;
			}

			if (fragment != null)
			{
				// remove the current fragment outside of the transaction so when we pop the
				// back stack we only revert the add
				if (currentFragment != null)
					manager.popBackStack();

				FragmentTransaction transaction = manager.beginTransaction();
				transaction.add(R.id.my_plan_container, fragment);
				transaction.addToBackStack(null);
				transaction.setBreadCrumbTitle(title);
				transaction.commit();
			}

			// show the back button
			setHomeAsUpEnabled(true);

			if (Utils.DEBUG)
				Utils.d(LOG_TAG, "showTask %s",
					fragment == null ? "do nothing" : (currentFragment != null ? "pop/add" : "add only"));
		}
		else
		{
			manager.popBackStack();

			// hide the back button
			setHomeAsUpEnabled(false);

			if (Utils.DEBUG)
				Utils.d(LOG_TAG, "showTask pop backstack");
		}
	}

	@Override
	public void handleTabChanged(boolean hidden)
	{
		// some data is shared across tabs so see if the child fragments
		// want to save anything when tabs change
		if (hidden)
		{
			FragmentManager manager = getChildFragmentManager();
			Fragment currentFragment = null;
			if (manager.getFragments().size() > 1)
				currentFragment = manager.getFragments().get(1);

			if (currentFragment instanceof OnFragmentInteractionListener)
				((OnFragmentInteractionListener)currentFragment).handleTabChanged(true);
		}
	}

	@Override
	public boolean handleBackPressed()
	{
		if (Utils.DEBUG)
			Utils.d(LOG_TAG, "handleBackPressed");

		return viewModel.resetTask();
	}

	@Override
	public boolean canHandleBackPressed()
	{
		if (Utils.DEBUG)
			Utils.d(LOG_TAG, "canHandleBackPressed");

		return (!TextUtils.isEmpty(viewModel.getCurrentTask().getValue()));
	}
}

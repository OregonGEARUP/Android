package org.oregongoestocollege.itsaplan;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.oregongoestocollege.itsaplan.data.TestResult;
import org.oregongoestocollege.itsaplan.databinding.FragmentMyPlanTestResultsBinding;
import org.oregongoestocollege.itsaplan.viewmodel.TestResultsViewModel;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class MyPlanTestResultsFragment extends Fragment
{
	private static final String LOG_TAG = "GearUp_TestResultFrag";
	private FragmentMyPlanTestResultsBinding binding;
	private TestResultsViewModel viewModel;

	public MyPlanTestResultsFragment()
	{
		// Required empty public constructor
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
		@Nullable Bundle savedInstanceState)
	{
		// Inflate this data binding layout
		binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_plan_test_results, container, false);

		return binding.getRoot();
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		Utils.d(LOG_TAG, "onActivityCreated");

		viewModel = ViewModelProviders.of(this).get(TestResultsViewModel.class);

		binding.setViewModel(viewModel);

		// observe live data
		viewModel.getActTestResultData().removeObservers(this);
		viewModel.getActTestResultData().observe(this, new Observer<TestResult>()
		{
			@Override
			public void onChanged(@Nullable TestResult testResult)
			{
				Utils.d(LOG_TAG, "ACT TestResult changed hasData:%s",
					testResult != null ? "true" : "false");

				// could add progress - when loading can start out null followed quickly by non-null
				if (testResult != null)
					viewModel.setActTestResult(getContext(), testResult);
			}
		});

		// observe live data
		viewModel.getSatTestResultData().removeObservers(this);
		viewModel.getSatTestResultData().observe(this, new Observer<TestResult>()
		{
			@Override
			public void onChanged(@Nullable TestResult testResult)
			{
				Utils.d(LOG_TAG, "SAT TestResult changed hasData:%s",
					testResult != null ? "true" : "false");

				// could add progress - when loading can start out null followed quickly by non-null
				if (testResult != null)
					viewModel.setSatTestResult(getContext(), testResult);
			}
		});
	}

	@Override
	public void onDetach()
	{
		if (viewModel != null)
			viewModel.update();

		super.onDetach();
	}
}

package org.oregongoestocollege.itsaplan;

import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.oregongoestocollege.itsaplan.databinding.FragmentMyPlanTestResultsBinding;
import org.oregongoestocollege.itsaplan.viewmodel.TestResultsViewModel;

/**
 * Oregon GEAR UP App
 * Copyright © 2020 Oregon GEAR UP. All rights reserved.
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

		if (Utils.DEBUG)
			Utils.d(LOG_TAG, "onActivityCreated");

		viewModel = ViewModelProviders.of(this).get(TestResultsViewModel.class);

		binding.setViewModel(viewModel);

		// observe live data
		viewModel.getActTestResultData().removeObservers(this);
		viewModel.getActTestResultData().observe(getViewLifecycleOwner(), testResult ->
		{
			if (Utils.DEBUG)
				Utils.d(LOG_TAG, "ACT TestResult changed hasData:%s",
					testResult != null ? "true" : "false");

			// could add progress - when loading can start out null followed quickly by non-null
			if (testResult != null)
				viewModel.setActTestResult(getContext(), testResult);
		});

		// observe live data
		viewModel.getSatTestResultData().removeObservers(this);
		viewModel.getSatTestResultData().observe(getViewLifecycleOwner(), testResult ->
		{
			if (Utils.DEBUG)
				Utils.d(LOG_TAG, "SAT TestResult changed hasData:%s",
					testResult != null ? "true" : "false");

			// could add progress - when loading can start out null followed quickly by non-null
			if (testResult != null)
				viewModel.setSatTestResult(getContext(), testResult);
		});
	}

	@Override
	public void onDestroyView()
	{
		super.onDestroyView();

		binding = null;
	}

	@Override
	public void onStop()
	{
		super.onStop();

		if (Utils.DEBUG)
			Utils.d(LOG_TAG, "onStop");

		if (viewModel != null)
			viewModel.stop();
	}
}

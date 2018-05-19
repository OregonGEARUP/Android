package org.oregongoestocollege.itsaplan;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.oregongoestocollege.itsaplan.databinding.FragmentMyPlanTestResultsBinding;
import org.oregongoestocollege.itsaplan.viewmodel.TestResultsViewModel;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class MyPlanTestResultsFragment extends Fragment
{
	private TestResultsViewModel viewModel;

	public MyPlanTestResultsFragment()
	{
		// Required empty public constructor
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
		@Nullable Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		FragmentMyPlanTestResultsBinding binding = DataBindingUtil
			.inflate(inflater, R.layout.fragment_my_plan_test_results, container, false);
		View v = binding.getRoot();

		viewModel = ViewModelProviders.of(this).get(TestResultsViewModel.class);

		// bind to fragment to get onClick for floating actions
		binding.setViewModel(viewModel);

		return v;
	}

	@Override
	public void onDetach()
	{
//		if (viewModel != null)
//			viewModel.update();

		super.onDetach();
	}
}

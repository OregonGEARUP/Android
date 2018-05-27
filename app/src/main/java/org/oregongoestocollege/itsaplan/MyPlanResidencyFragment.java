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

import org.oregongoestocollege.itsaplan.data.Residency;
import org.oregongoestocollege.itsaplan.databinding.FragmentMyPlanResidencyBinding;
import org.oregongoestocollege.itsaplan.viewmodel.ResidencyViewModel;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class MyPlanResidencyFragment extends Fragment
{
	private FragmentMyPlanResidencyBinding binding;
	private ResidencyViewModel viewModel;

	public MyPlanResidencyFragment()
	{
		// Required empty public constructor
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
		@Nullable Bundle savedInstanceState)
	{
		// Inflate this data binding layout
		binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_plan_residency, container, false);

		return binding.getRoot();
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		Utils.d(ResidencyViewModel.LOG_TAG, "onActivityCreated");

		ResidencyViewModel.Factory factory = new ResidencyViewModel.Factory(getActivity().getApplication());

		viewModel = ViewModelProviders.of(this, factory).get(ResidencyViewModel.class);

		binding.setViewModel(viewModel);

		// observe live data
		viewModel.getResidencyData().removeObservers(this);
		viewModel.getResidencyData().observe(this, new Observer<Residency>()
		{
			@Override
			public void onChanged(@Nullable Residency residency)
			{
				Utils.d(ResidencyViewModel.LOG_TAG, String.format("Residency changed hasData:%s",
					residency != null ? "true" : "false"));

				viewModel.setResidency(getContext(), residency);
			}
		});
	}

	@Override
	public void onDetach()
	{
		Utils.d(ResidencyViewModel.LOG_TAG, "onDetach");

		// persist any user entered data
		if (viewModel != null)
			viewModel.update();

		super.onDetach();
	}
}

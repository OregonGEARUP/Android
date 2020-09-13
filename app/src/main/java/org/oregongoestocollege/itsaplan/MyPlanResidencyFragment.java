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

import org.oregongoestocollege.itsaplan.databinding.FragmentMyPlanResidencyBinding;
import org.oregongoestocollege.itsaplan.viewmodel.ResidencyViewModel;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class MyPlanResidencyFragment extends Fragment
{
	private static final String LOG_TAG = "GearUp_ResidencyFrag";
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

		if (Utils.DEBUG)
			Utils.d(LOG_TAG, "onActivityCreated");

		ResidencyViewModel.Factory factory = new ResidencyViewModel.Factory(getActivity().getApplication());

		viewModel = ViewModelProviders.of(this, factory).get(ResidencyViewModel.class);

		binding.setViewModel(viewModel);

		// observe live data
		viewModel.getResidencyData().removeObservers(this);
		viewModel.getResidencyData().observe(getViewLifecycleOwner(), residency ->
		{
			if (Utils.DEBUG)
				Utils.d(LOG_TAG, "Residency changed hasData:%s", residency != null ? "true" : "false");

			// could add progress - when loading can start out null followed quickly by non-null
			if (residency != null)
				viewModel.setResidency(getContext(), residency);
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
			viewModel.update();
	}
}

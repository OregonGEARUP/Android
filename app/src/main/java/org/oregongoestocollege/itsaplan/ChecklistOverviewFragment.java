package org.oregongoestocollege.itsaplan;

import java.util.List;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.oregongoestocollege.itsaplan.data.ChecklistState;
import org.oregongoestocollege.itsaplan.databinding.FragmentChecklistOverviewBinding;
import org.oregongoestocollege.itsaplan.support.BindingItem;
import org.oregongoestocollege.itsaplan.support.BindingItemsAdapter;
import org.oregongoestocollege.itsaplan.support.ItemClickCallback;
import org.oregongoestocollege.itsaplan.viewmodel.BlockInfoItemViewModel;
import org.oregongoestocollege.itsaplan.viewmodel.ChecklistNavViewModel;
import org.oregongoestocollege.itsaplan.viewmodel.OverviewViewModel;

/**
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class ChecklistOverviewFragment extends Fragment implements ItemClickCallback
{
	private static final String LOG_TAG = "GearUp_ChecklistOverviewFrag";
	private BindingItemsAdapter adapter;
	private OverviewViewModel viewModel;

	public ChecklistOverviewFragment()
	{
		// Required empty public constructor
	}

	private void onItemsChanged(List<BindingItem> items)
	{
		if (Utils.DEBUG)
			Utils.d(LOG_TAG, "onItemsChanged()");

		// Update the list when the data changes
		if (adapter.getItemCount() != 0)
			adapter.clear();

		if (items != null && !items.isEmpty())
			adapter.addAll(items);
	}

	private void onLoadingChanged(Boolean loading)
	{
		if (Utils.DEBUG)
			Utils.d(LOG_TAG, "onItemsChanged()");

		// Update the flag when the data changes
		viewModel.dataLoading.set(loading);
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState)
	{
		if (Utils.DEBUG)
			Utils.d(LOG_TAG, "onCreateView()");

		// Inflate the layout for this fragment
		FragmentChecklistOverviewBinding
			binding = DataBindingUtil.inflate(inflater, R.layout.fragment_checklist_overview, container, false);
		View v = binding.getRoot();

		adapter = new BindingItemsAdapter(this);

		RecyclerView recyclerView = v.findViewById(R.id.recycler_view);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

		viewModel = ViewModelProviders.of(getActivity()).get(OverviewViewModel.class);
		viewModel.getBlockInfoList().observe(this, this::onItemsChanged);
		viewModel.getListLoading().observe(this, this::onLoadingChanged);

		binding.setUxContext(viewModel);

		return v;
	}

	@Override
	public void onResume()
	{
		super.onResume();
		viewModel.start();
	}

	@Override
	public void onClick(BindingItem item)
	{
		if (!(item instanceof BlockInfoItemViewModel))
			return;

		if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED))
		{
			BlockInfoItemViewModel itemViewModel = (BlockInfoItemViewModel)item;
			if (itemViewModel.clickable())
			{
				ChecklistState state =
					new ChecklistState(itemViewModel.getBlockFileName(), itemViewModel.getBlockIndex());

				// trigger a state change to load the correct fragment
				ChecklistNavViewModel cvm = ViewModelProviders.of(getActivity()).get(ChecklistNavViewModel.class);
				cvm.setCurrentState(state);
			}
		}
	}

	/**
	 * @return A new instance of fragment ChecklistBlockFragment.
	 */
	public static ChecklistOverviewFragment newInstance()
	{
		ChecklistOverviewFragment fragment = new ChecklistOverviewFragment();
		return fragment;
	}
}

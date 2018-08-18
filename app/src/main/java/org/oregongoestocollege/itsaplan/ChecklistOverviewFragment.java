package org.oregongoestocollege.itsaplan;

import java.util.List;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.oregongoestocollege.itsaplan.databinding.FragmentChecklistOverviewBinding;
import org.oregongoestocollege.itsaplan.support.BindingItem;
import org.oregongoestocollege.itsaplan.support.BindingItemsAdapter;
import org.oregongoestocollege.itsaplan.support.ItemClickCallback;
import org.oregongoestocollege.itsaplan.viewmodel.BlockInfoItemViewModel;
import org.oregongoestocollege.itsaplan.viewmodel.OverviewViewModel;

/**
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class ChecklistOverviewFragment extends Fragment implements ItemClickCallback
{
	public static final String LOG_TAG = "GearUp_ChecklistOverviewFrag";
	private OnFragmentInteractionListener listener;
	private RecyclerView recyclerView;
	private BindingItemsAdapter adapter;
	private OverviewViewModel viewModel;

	public ChecklistOverviewFragment()
	{
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of this fragment.
	 *
	 * @return A new instance of fragment ChecklistOverviewFragment.
	 */
	public static ChecklistOverviewFragment newInstance()
	{
		return new ChecklistOverviewFragment();
	}

	private void showBlockInfoList(List<BindingItem> items)
	{
		Utils.d(LOG_TAG, "showBlockInfoList()");

		// Update the list when the data changes
		if (adapter.getItemCount() != 0)
			adapter.clear();

		if (items != null && !items.isEmpty())
			adapter.addAll(items);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState)
	{
		Utils.d(LOG_TAG, "onCreateView()");

		// Inflate the layout for this fragment
		FragmentChecklistOverviewBinding
			binding = DataBindingUtil.inflate(inflater, R.layout.fragment_checklist_overview, container, false);
		View v = binding.getRoot();

		adapter = new BindingItemsAdapter(this);

		recyclerView = v.findViewById(R.id.recycler_view);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

		viewModel = ViewModelProviders.of(getActivity()).get(OverviewViewModel.class);
		viewModel.getBlockInfoList().observe(this, this::showBlockInfoList);

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
	public void onAttach(Context context)
	{
		super.onAttach(context);

		if (context instanceof OnFragmentInteractionListener)
			listener = (OnFragmentInteractionListener)context;
		else
			throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");

		Utils.d(LOG_TAG, "onAttach");
	}

	@Override
	public void onDetach()
	{
		super.onDetach();
		listener = null;
	}

	@Override
	public void onClick(BindingItem item)
	{
		if (!(item instanceof BlockInfoItemViewModel))
			return;

		if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED) && listener != null)
		{
			BlockInfoItemViewModel vm = (BlockInfoItemViewModel)item;
			if (vm.clickable())
				listener.onShowBlock(vm.getBlockIndex(), vm.getBlockFileName());
		}
	}
}
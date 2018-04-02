package org.oregongoestocollege.itsaplan;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.oregongoestocollege.itsaplan.data.ChecklistState;
import org.oregongoestocollege.itsaplan.databinding.FragmentAllBlocksBinding;
import org.oregongoestocollege.itsaplan.support.BindingItemsAdapter;
import org.oregongoestocollege.itsaplan.viewmodel.AllBlocksViewModel;

/**
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class AllBlocksFragment extends Fragment
{
	private static final String LOG_TAG = "GearUpAllBlocksFrag";
	private OnFragmentInteractionListener listener;
	private RecyclerView recyclerView;
	private BindingItemsAdapter adapter;
	private AllBlocksViewModel allBlocksViewModel;

	public AllBlocksFragment()
	{
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of this fragment.
	 *
	 * @return A new instance of fragment AllBlocksFragment.
	 */
	public static AllBlocksFragment newInstance()
	{
		return new AllBlocksFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		FragmentAllBlocksBinding
			binding = DataBindingUtil.inflate(inflater, R.layout.fragment_all_blocks, container, false);
		View v = binding.getRoot();

		adapter = new BindingItemsAdapter();

		recyclerView = v.findViewById(R.id.recycler_view);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

		ViewModelFactory factory = ViewModelFactory.getInstance(getActivity().getApplication());
		allBlocksViewModel = ViewModelProviders.of(this, factory).get(AllBlocksViewModel.class);
		binding.setUxContext(allBlocksViewModel);

		allBlocksViewModel.getUpdateListEvent().observe(this, new Observer<Void>()
		{
			@Override
			public void onChanged(@Nullable Void aVoid)
			{
				if (adapter.getItemCount() != 0)
					adapter.clear();

				adapter.addAll(allBlocksViewModel.getItems());

				getActivity().setTitle(R.string.app_name);
			}
		});

		allBlocksViewModel.getOpenBlockEvent().observe(this, new Observer<ChecklistState>()
		{
			@Override
			public void onChanged(@Nullable ChecklistState state)
			{
				if (listener != null && state != null)
					listener.onShowBlock(state.blockIndex, state.blockFileName);
			}
		});

		return v;
	}

	@Override
	public void onResume()
	{
		super.onResume();
		allBlocksViewModel.start();
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
}

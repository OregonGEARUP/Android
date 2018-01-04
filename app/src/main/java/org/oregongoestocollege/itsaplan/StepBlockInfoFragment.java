package org.oregongoestocollege.itsaplan;

import java.lang.ref.WeakReference;

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
import org.oregongoestocollege.itsaplan.databinding.FragmentStepBlockInfoBinding;
import org.oregongoestocollege.itsaplan.support.BindingItemsAdapter;
import org.oregongoestocollege.itsaplan.viewmodel.BlockInfoListViewModel;

/**
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class StepBlockInfoFragment extends Fragment
{
	private WeakReference<OnChecklistInteraction> listener;
	private RecyclerView recyclerView;
	private BindingItemsAdapter adapter;
	private BlockInfoListViewModel blockInfoListViewModel;

	public StepBlockInfoFragment()
	{
		// Required empty public constructor
	}

	public void init(OnChecklistInteraction listener)
	{
		this.listener = new WeakReference<>(listener);
	}

	/**
	 * Use this factory method to create a new instance of this fragment.
	 *
	 * @return A new instance of fragment StepBlockInfoFragment.
	 */
	public static StepBlockInfoFragment newInstance()
	{
		return new StepBlockInfoFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		FragmentStepBlockInfoBinding
			binding = DataBindingUtil.inflate(inflater, R.layout.fragment_step_block_info, container, false);
		View v = binding.getRoot();

		adapter = new BindingItemsAdapter();

		recyclerView = v.findViewById(R.id.recycler_view);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

		ViewModelFactory factory = ViewModelFactory.getInstance(getActivity().getApplication());
		blockInfoListViewModel = ViewModelProviders.of(this, factory).get(BlockInfoListViewModel.class);
		binding.setUxContext(blockInfoListViewModel);

		blockInfoListViewModel.getUpdateListEvent().observe(this, new Observer<Void>()
		{
			@Override
			public void onChanged(@Nullable Void aVoid)
			{
				if (adapter.getItemCount() != 0)
					adapter.clear();

				adapter.addAll(blockInfoListViewModel.getItems());

				getActivity().setTitle(R.string.app_name);
			}
		});

		blockInfoListViewModel.getOpenBlockEvent().observe(this, new Observer<ChecklistState>()
		{
			@Override
			public void onChanged(@Nullable ChecklistState state)
			{
				if (listener != null && state != null)
					listener.get().onShowBlock(state.blockIndex, state.blockFileName);
			}
		});

		return v;
	}

	@Override
	public void onResume()
	{
		super.onResume();
		blockInfoListViewModel.start();
	}

	@Override
	public void onAttach(Context context)
	{
		super.onAttach(context);
	}

	@Override
	public void onDetach()
	{
		super.onDetach();
	}
}

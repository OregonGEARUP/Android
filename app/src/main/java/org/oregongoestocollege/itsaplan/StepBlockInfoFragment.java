package org.oregongoestocollege.itsaplan;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.arch.lifecycle.Observer;
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

import org.oregongoestocollege.itsaplan.data.BlockInfo;
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
		this.listener = new WeakReference<OnChecklistInteraction>(listener);
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

		blockInfoListViewModel = obtainViewModel(getActivity());
		binding.setUxContext(blockInfoListViewModel);

		blockInfoListViewModel.getUpdateListEvent().observe(this, new Observer<Void>()
		{
			@Override
			public void onChanged(@Nullable Void aVoid)
			{
				if (adapter.getItemCount() != 0)
					adapter.clear();

				adapter.addAll(blockInfoListViewModel.getItems());
			}
		});

		blockInfoListViewModel.getOpenBlockEvent().observe(this, new Observer<BlockInfo>()
		{
			@Override
			public void onChanged(@Nullable BlockInfo blockInfo)
			{
				if (listener != null)
					listener.get().onShowBlock(blockInfo);
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

	public static BlockInfoListViewModel obtainViewModel(Activity activity)
	{
		// Use a Factory to inject dependencies into the ViewModel
		ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());

		// future - could use ViewModelProviders

		return factory.create(BlockInfoListViewModel.class);
	}
}

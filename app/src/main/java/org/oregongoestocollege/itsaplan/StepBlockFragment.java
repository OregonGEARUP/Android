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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.oregongoestocollege.itsaplan.data.ChecklistState;
import org.oregongoestocollege.itsaplan.databinding.FragmentStepBlockBinding;
import org.oregongoestocollege.itsaplan.support.BindingItemsAdapter;
import org.oregongoestocollege.itsaplan.viewmodel.BlockViewModel;

/**
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class StepBlockFragment extends Fragment
{
	private static final String LOG_TAG = "GearUpStepBlockFragment";
	private OnFragmentInteractionListener listener;
	private int blockIndex = Utils.NO_INDEX;
	private String blockFileName;
	private RecyclerView recyclerView;
	private BindingItemsAdapter adapter;
	private BlockViewModel blockViewModel;

	public StepBlockFragment()
	{
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of this fragment.
	 *
	 * @return A new instance of fragment StepBlockFragment.
	 */
	public static StepBlockFragment newInstance(int blockIndex, String blockFileName)
	{
		StepBlockFragment fragment = new StepBlockFragment();
		Bundle args = new Bundle();
		args.putString(Utils.PARAM_BLOCK_FILE_NAME, blockFileName);
		args.putInt(Utils.PARAM_BLOCK_INDEX, blockIndex);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);

		if (!TextUtils.isEmpty(blockFileName))
			outState.putString(Utils.PARAM_BLOCK_FILE_NAME, blockFileName);
		if (blockIndex != Utils.NO_INDEX)
			outState.putInt(Utils.PARAM_BLOCK_INDEX, blockIndex);
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		if (getArguments() != null)
		{
			blockFileName = getArguments().getString(Utils.PARAM_BLOCK_FILE_NAME);
			blockIndex = getArguments().getInt(Utils.PARAM_BLOCK_INDEX);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		FragmentStepBlockBinding binding =
			DataBindingUtil.inflate(inflater, R.layout.fragment_step_block, container, false);
		View v = binding.getRoot();

		if (savedInstanceState != null)
		{
			blockFileName = savedInstanceState.getString(Utils.PARAM_BLOCK_FILE_NAME);
			blockIndex = savedInstanceState.getInt(Utils.PARAM_BLOCK_INDEX, Utils.NO_INDEX);
		}

		adapter = new BindingItemsAdapter(null);

		recyclerView = v.findViewById(R.id.recycler_view);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

		ViewModelFactory factory = ViewModelFactory.getInstance(getActivity().getApplication());
		blockViewModel = ViewModelProviders.of(this, factory).get(BlockViewModel.class);
		binding.setUxContext(blockViewModel);

		blockViewModel.getUpdateListEvent().observe(this, new Observer<Void>()
		{
			@Override
			public void onChanged(@Nullable Void aVoid)
			{
				if (adapter.getItemCount() != 0)
					adapter.clear();

				adapter.addAll(blockViewModel.getItems());

				getActivity().setTitle(blockViewModel.getTitle());
			}
		});

		blockViewModel.getOpenStageEvent().observe(this, new Observer<ChecklistState>()
		{
			@Override
			public void onChanged(@Nullable ChecklistState state)
			{
				if (listener != null && state != null)
					listener.onShowStage(state.blockIndex, state.stageIndex);
			}
		});

		return v;
	}

	@Override
	public void onResume()
	{
		super.onResume();
		blockViewModel.start(blockIndex, blockFileName);
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

package org.oregongoestocollege.itsaplan;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.oregongoestocollege.itsaplan.data.ChecklistState;
import org.oregongoestocollege.itsaplan.databinding.FragmentCheckpointBinding;
import org.oregongoestocollege.itsaplan.viewmodel.CheckpointViewModel;

/**
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class CheckpointFragment extends Fragment
{
	private static final String LOG_TAG = "GearUpCheckpointFragment";
	private OnFragmentInteractionListener listener;
	private CheckpointViewModel checkpointViewModel;
	private int blockIndex = Utils.NO_INDEX;
	private int stageIndex = Utils.NO_INDEX;
	private int checkpointIndex = Utils.NO_INDEX;

	public CheckpointFragment()
	{
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of this fragment.
	 *
	 * @return A new instance of fragment CheckpointFragment.
	 */
	public static CheckpointFragment newInstance(int blockIndex, int stageIndex, int checkpointIndex)
	{
		CheckpointFragment fragment = new CheckpointFragment();
		Bundle args = new Bundle();
		args.putInt(Utils.PARAM_BLOCK_INDEX, blockIndex);
		args.putInt(Utils.PARAM_STAGE_INDEX, stageIndex);
		args.putInt(Utils.PARAM_CHECKPOINT_INDEX, checkpointIndex);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);

		outState.putInt(Utils.PARAM_BLOCK_INDEX, blockIndex);
		outState.putInt(Utils.PARAM_STAGE_INDEX, stageIndex);
		outState.putInt(Utils.PARAM_CHECKPOINT_INDEX, checkpointIndex);
	}



	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if (getArguments() != null)
		{
			blockIndex = getArguments().getInt(Utils.PARAM_BLOCK_INDEX);
			stageIndex = getArguments().getInt(Utils.PARAM_STAGE_INDEX);
			checkpointIndex = getArguments().getInt(Utils.PARAM_CHECKPOINT_INDEX);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		FragmentCheckpointBinding binding =
			DataBindingUtil.inflate(inflater, R.layout.fragment_checkpoint, container, false);
		View v = binding.getRoot();

		if (savedInstanceState != null)
		{
			blockIndex = savedInstanceState.getInt(Utils.PARAM_BLOCK_INDEX, Utils.NO_INDEX);
			stageIndex = savedInstanceState.getInt(Utils.PARAM_STAGE_INDEX, Utils.NO_INDEX);
			checkpointIndex = savedInstanceState.getInt(Utils.PARAM_CHECKPOINT_INDEX, Utils.NO_INDEX);
		}

		ViewModelFactory factory = ViewModelFactory.getInstance(getActivity().getApplication());
		checkpointViewModel = ViewModelProviders.of(this, factory).get(CheckpointViewModel.class);
		binding.setUxContext(checkpointViewModel);

		checkpointViewModel.getNextStageEvent().observe(this, new Observer<ChecklistState>()
		{
			@Override
			public void onChanged(@Nullable ChecklistState state)
			{
				if (listener != null && state != null)
					listener.onShowStage(state.blockIndex, state.stageIndex);
			}
		});

		checkpointViewModel.getNextBlockEvent().observe(this, new Observer<ChecklistState>()
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
		checkpointViewModel.start(blockIndex, stageIndex, checkpointIndex);
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

	public CheckpointViewModel getCheckpointViewModel()
	{
		return checkpointViewModel;
	}
}

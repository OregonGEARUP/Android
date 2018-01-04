package org.oregongoestocollege.itsaplan;

import java.lang.ref.WeakReference;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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
	private WeakReference<OnChecklistInteraction> listener;
	private CheckpointViewModel checkpointViewModel;
	private int blockIndex;
	private int stageIndex;
	private int checkpointIndex;

	public CheckpointFragment()
	{
		// Required empty public constructor
	}

	public void init(OnChecklistInteraction listener, int blockIndex, int stageIndex, int checkpointIndex)
	{
		this.listener = new WeakReference<>(listener);
		this.blockIndex = blockIndex;
		this.stageIndex = stageIndex;
		this.checkpointIndex = checkpointIndex;
	}

	/**
	 * Use this factory method to create a new instance of this fragment.
	 *
	 * @return A new instance of fragment CheckpointFragment.
	 */
	public static CheckpointFragment newInstance()
	{
		return new CheckpointFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		FragmentCheckpointBinding binding =
			DataBindingUtil.inflate(inflater, R.layout.fragment_checkpoint, container, false);
		View v = binding.getRoot();

		ViewModelFactory factory = ViewModelFactory.getInstance(getActivity().getApplication());
		checkpointViewModel = ViewModelProviders.of(this, factory).get(CheckpointViewModel.class);
		binding.setUxContext(checkpointViewModel);

		checkpointViewModel.getNextStageEvent().observe(this, new Observer<ChecklistState>()
		{
			@Override
			public void onChanged(@Nullable ChecklistState state)
			{
				if (listener != null && state != null)
					listener.get().onShowStage(state.blockIndex, state.stageIndex);
			}
		});

		checkpointViewModel.getNextBlockEvent().observe(this, new Observer<ChecklistState>()
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
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if (getArguments() != null)
		{
			//description = getArguments().getString(CHECKPOINT_DESC);
		}
	}

	@Override
	public void onResume()
	{
		super.onResume();
		checkpointViewModel.start(blockIndex, stageIndex, checkpointIndex);
	}
}

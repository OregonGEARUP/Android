package org.oregongoestocollege.itsaplan;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.oregongoestocollege.itsaplan.databinding.FragmentCheckpointBinding;
import org.oregongoestocollege.itsaplan.viewmodel.CheckpointViewModel;

/**
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class CheckpointFragment extends Fragment
{
	private CheckpointViewModel checkpointViewModel;
	private int blockIndex;
	private int stageIndex;
	private int checkpointIndex;

	public CheckpointFragment()
	{
		// Required empty public constructor
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

	public void init(int blockIndex, int stageIndex, int checkpointIndex)
	{
		this.blockIndex = blockIndex;
		this.stageIndex = stageIndex;
		this.checkpointIndex = checkpointIndex;
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

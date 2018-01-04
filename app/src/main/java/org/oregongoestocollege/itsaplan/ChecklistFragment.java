package org.oregongoestocollege.itsaplan;

import java.util.Locale;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.oregongoestocollege.itsaplan.data.CheckpointRepository;
import org.oregongoestocollege.itsaplan.data.Stage;

/**
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class ChecklistFragment extends Fragment implements OnChecklistInteraction
{
	private static final String LOG_TAG = "GearUpChecklistFrag";
	private static final String PARAM_BLOCK_FILE_NAME = "blockFileName";
	private static final String PARAM_BLOCK_INDEX = "blockIndex";
	private static final String PARAM_STAGE_INDEX = "stageIndex";
	private OnFragmentInteractionListener mListener;
	private int identifier;
	private String currentBlockFileName;
	private int currentBlockIndex = -1;
	private int currentStageIndex = -1;

	public ChecklistFragment()
	{
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of this fragment.
	 *
	 * @return A new instance of fragment ChecklistFragment.
	 */
	public static ChecklistFragment newInstance()
	{
		return new ChecklistFragment();
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);

		outState.putString(PARAM_BLOCK_FILE_NAME, currentBlockFileName);
		outState.putInt(PARAM_BLOCK_INDEX, currentBlockIndex);
		outState.putInt(PARAM_STAGE_INDEX, currentStageIndex);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_checklist, container, false);

		if (savedInstanceState != null)
		{
			currentBlockFileName = savedInstanceState.getString(PARAM_BLOCK_FILE_NAME);
			currentBlockIndex = savedInstanceState.getInt(PARAM_BLOCK_INDEX);
			currentStageIndex = savedInstanceState.getInt(PARAM_STAGE_INDEX);
		}

		if (currentBlockIndex >= 0 && currentStageIndex >= 0)
			showStepStage(currentBlockIndex, currentStageIndex);
		else if (currentBlockIndex >= 0)
			showStepBlock(currentBlockIndex, currentBlockFileName);
		else
			showStepBlockInfo();

		return v;
	}

	private void showStepBlockInfo()
	{
		StepBlockInfoFragment newFragment = StepBlockInfoFragment.newInstance();
		newFragment.init(this);

		FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.fragment_container, newFragment);
		transaction.commit();

		identifier = 1;
		currentBlockFileName = null;
		currentBlockIndex = -1;
		currentStageIndex = -1;

		AppCompatActivity activity = (AppCompatActivity)getActivity();
		activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
	}

	private void showStepBlock(int blockIndex, String blockFileName)
	{
		StepBlockFragment newFragment = StepBlockFragment.newInstance();
		newFragment.init(this, blockIndex, blockFileName);

		FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.fragment_container, newFragment);
		transaction.commit();

		identifier = 2;
		currentBlockFileName = blockFileName;
		currentBlockIndex = blockIndex;
		currentStageIndex = -1;

		AppCompatActivity activity = (AppCompatActivity)getActivity();
		activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	private void showStepStage(int blockIndex, int stageIndex)
	{
		Stage stage = CheckpointRepository.getInstance().getStage(blockIndex, stageIndex);
		if (stage == null)
		{
			// this shouldn't happen
			Log.w(LOG_TAG, String.format(Locale.US, "Invalid block:%d stage:%d", blockIndex, stageIndex));
			return;
		}

		StepStageFragment newFragment = StepStageFragment.newInstance();
		newFragment.init(this, blockIndex, stageIndex);

		FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.fragment_container, newFragment);
		transaction.commit();

		identifier = 3;
		currentBlockIndex = blockIndex;
		currentStageIndex = stageIndex;

		AppCompatActivity activity = (AppCompatActivity)getActivity();
		activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri)
	{
		if (mListener != null)
		{
			mListener.onFragmentInteraction();
		}
	}

	@Override
	public void onAttach(Context context)
	{
		super.onAttach(context);
		if (context instanceof OnFragmentInteractionListener)
		{
			mListener = (OnFragmentInteractionListener)context;
		}
		else
		{
			throw new RuntimeException(context.toString()
				+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach()
	{
		super.onDetach();
		mListener = null;
	}

	@Override
	public void onShowBlock(int blockIndex, String blockFileName)
	{
		showStepBlock(blockIndex, blockFileName);
	}

	@Override
	public void onShowStage(int blockIndex, int stageIndex)
	{
		showStepStage(blockIndex, stageIndex);
	}

	public boolean handleBackPressed()
	{
		if (identifier == 3)
		{
			showStepBlock(currentBlockIndex, currentBlockFileName);
			return true;
		}
		if (identifier == 2)
		{
			showStepBlockInfo();
			return true;
		}
		return false;
	}

	public boolean canHandleBackPressed()
	{
		return (identifier > 1);
	}
}

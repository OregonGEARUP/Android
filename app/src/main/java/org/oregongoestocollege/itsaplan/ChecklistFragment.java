package org.oregongoestocollege.itsaplan;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.oregongoestocollege.itsaplan.data.Stage;

/**
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class ChecklistFragment extends Fragment implements OnChecklistInteraction
{
	private static final String PARAM_BLOCK_INDEX = "blockIndex";
	private OnFragmentInteractionListener mListener;
	private int identifier;
	private int blockIndex = -1;

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

		outState.putInt(PARAM_BLOCK_INDEX, blockIndex);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_checklist, container, false);

		if (savedInstanceState != null)
			blockIndex = savedInstanceState.getInt(PARAM_BLOCK_INDEX);

		if (blockIndex >= 0)
			showStepBlock(blockIndex);
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
		blockIndex = -1;

		AppCompatActivity activity = (AppCompatActivity)getActivity();
		activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
	}

	private void showStepBlock(int blockIndex)
	{
		StepBlockFragment newFragment = StepBlockFragment.newInstance();
		newFragment.init(this, blockIndex);

		FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.fragment_container, newFragment);
		transaction.commit();

		identifier = 2;
		this.blockIndex = blockIndex;

		AppCompatActivity activity = (AppCompatActivity)getActivity();
		activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	private void showStepStage(Stage stage)
	{
		StepStageFragment newFragment = StepStageFragment.newInstance();
		newFragment.init(this, stage);

		FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.fragment_container, newFragment);
		transaction.commit();

		identifier = 3;

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
	public void onShowBlock(int blockIndex)
	{
		showStepBlock(blockIndex);
	}

	@Override
	public void onShowStage(Stage stage)
	{
		showStepStage(stage);
	}

	public boolean handleBackPressed()
	{
		if (identifier == 3)
		{
			showStepBlock(blockIndex);
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

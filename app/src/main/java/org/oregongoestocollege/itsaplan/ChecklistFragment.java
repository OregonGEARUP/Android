package org.oregongoestocollege.itsaplan;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.oregongoestocollege.itsaplan.data.BlockInfo;

/**
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class ChecklistFragment extends Fragment implements OnChecklistInteraction
{
	private OnFragmentInteractionListener mListener;
	private int identifier;


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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_checklist, container, false);

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

		Resources resources = getResources();
		AppCompatActivity activity = (AppCompatActivity)getActivity();
		activity.setTitle(R.string.app_name);
		activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
	}

	private void showStepBlock(BlockInfo blockInfo)
	{
		StepBlockFragment newFragment = StepBlockFragment.newInstance();
		newFragment.init(this, blockInfo.blockFileName);

		FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.fragment_container, newFragment);
		transaction.commit();

		identifier = 2;

		Resources resources = getResources();
		AppCompatActivity activity = (AppCompatActivity)getActivity();
		activity.setTitle(blockInfo.title);
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
	public void onShowBlock(BlockInfo blockInfo)
	{
		showStepBlock(blockInfo);
	}

	public boolean handleBackPressed()
	{
		if (identifier == 2)
		{
			showStepBlockInfo();
			return true;
		}
		return false;
	}
}

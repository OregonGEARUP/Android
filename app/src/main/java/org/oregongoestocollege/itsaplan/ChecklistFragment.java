package org.oregongoestocollege.itsaplan;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.oregongoestocollege.itsaplan.data.ChecklistState;
import org.oregongoestocollege.itsaplan.viewmodel.ChecklistViewModel;

/**
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class ChecklistFragment extends Fragment implements OnFragmentInteractionListener
{
	private static final String LOG_TAG = "GearUp_ChecklistFrag";
	private static final String FRAG_OVERVIEW = "frag-overview";
	private static final String FRAG_BLOCK = "frag-block";
	private static final String FRAG_STAGE = "frag-stage";

	public ChecklistFragment()
	{
		// Required empty public constructor
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		Utils.d(LOG_TAG, "onCreate");

		ChecklistViewModel cvm = ViewModelProviders.of(getActivity()).get(ChecklistViewModel.class);
		cvm.getCurrentState().observe(this, this::onStateChanged);
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
		@Nullable Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_checklist, container, false);

		if (savedInstanceState == null)
		{
			// TODO - start where we left off...

			showOverview();

			/*if (currentBlockIndex >= 0 && currentStageIndex >= 0)
			showStage(currentBlockIndex, currentStageIndex);
		else if (currentBlockIndex >= 0)
			showBlock(currentBlockIndex, currentBlockFileName);
		else
			showBlocks();*/
		}

		return v;
	}

	private void onStateChanged(ChecklistState state)
	{
		boolean enableBackButton = false;

		if (state != null && state.hasBlockAndStageIndex())
		{
			Utils.d(LOG_TAG, "onStateChanged stage");

			showStage(state.blockIndex, state.stageIndex);
			enableBackButton = true;
		}
		else if (state != null && state.hasBlockIndexAndFile())
		{
			Utils.d(LOG_TAG, "onStateChanged Block");

			showBlock(state.blockIndex, state.blockFileName);
			enableBackButton = true;
		}
		else
		{
			Utils.d(LOG_TAG, "onStateChanged Overview");

			showOverview();
		}

		// show / hide the back button as appropriate
		setHomeAsUpEnabled(enableBackButton);
	}

	private void setHomeAsUpEnabled(boolean enabled)
	{
		FragmentActivity activity = getActivity();
		if (activity != null)
		{
			ActionBar actionBar = ((AppCompatActivity)activity).getSupportActionBar();
			if (actionBar != null)
				actionBar.setDisplayHomeAsUpEnabled(enabled);
		}
	}

	private void showOverview()
	{
		Utils.d(LOG_TAG, "showOverview");

		// make sure to use getChildFragmentManager versus getSupportFragmentManager
		FragmentManager manager = getChildFragmentManager();

		if (manager.getBackStackEntryCount() <= 0)
		{
			FragmentTransaction transaction = manager.beginTransaction();
			transaction.replace(R.id.fragment_container_checklist, ChecklistOverviewFragment.newInstance());
			transaction.addToBackStack(FRAG_OVERVIEW);
			transaction.commit();
		}
		else
			manager.popBackStack(FRAG_OVERVIEW, 0);
	}

	private void showBlock(int blockIndex, String blockFileName)
	{
		Utils.d(LOG_TAG, "showBlock");

		// make sure to use getChildFragmentManager versus getSupportFragmentManager
		FragmentManager manager = getChildFragmentManager();

		// if we have 2 entries than we are showing a Block so pop it off
		if (manager.getBackStackEntryCount() == 2)
			manager.popBackStackImmediate();

		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(R.id.fragment_container_checklist,
			ChecklistBlockFragment.newInstance(blockIndex, blockFileName));
		transaction.addToBackStack(FRAG_BLOCK);
		transaction.commit();
	}

	private void showStage(int blockIndex, int stageIndex)
	{
		Utils.d(LOG_TAG, "showStage");

		// make sure to use getChildFragmentManager versus getSupportFragmentManager
		FragmentManager manager = getChildFragmentManager();

		// if we have 3 entries than we are showing a Stage so pop it off
		if (manager.getBackStackEntryCount() == 3)
			manager.popBackStackImmediate();

		// then add our new one
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(R.id.fragment_container_checklist,
			ChecklistStageFragment.newInstance(blockIndex, stageIndex));
		transaction.addToBackStack(FRAG_STAGE);
		transaction.commit();
	}

	@Override
	public boolean handleBackPressed()
	{
		Utils.d(LOG_TAG, "handleBackPressed");

		FragmentManager manager = getChildFragmentManager();
		if (manager.getBackStackEntryCount() > 0)
		{
			manager.popBackStack();
			return true;
		}

		return false;
	}

	@Override
	public boolean canHandleBackPressed()
	{
		Utils.d(LOG_TAG, "canHandleBackPressed");

		FragmentManager manager = getChildFragmentManager();
		return manager.getBackStackEntryCount() > 0;
	}
}

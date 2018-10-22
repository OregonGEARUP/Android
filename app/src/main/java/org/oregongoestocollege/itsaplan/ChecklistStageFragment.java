package org.oregongoestocollege.itsaplan;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.oregongoestocollege.itsaplan.data.ChecklistState;
import org.oregongoestocollege.itsaplan.data.Checkpoint;
import org.oregongoestocollege.itsaplan.data.CheckpointInterface;
import org.oregongoestocollege.itsaplan.data.CheckpointRepository;
import org.oregongoestocollege.itsaplan.data.EntryType;
import org.oregongoestocollege.itsaplan.data.MyPlanRepository;
import org.oregongoestocollege.itsaplan.data.Stage;
import org.oregongoestocollege.itsaplan.data.UserEntries;
import org.oregongoestocollege.itsaplan.data.UserEntriesInterface;
import org.oregongoestocollege.itsaplan.viewmodel.CheckpointViewModel;

/**
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class ChecklistStageFragment extends Fragment implements ViewPager.OnPageChangeListener
{
	private static final String LOG_TAG = "GearUp_ChecklistStageFrag";
	private int blockIndex = Utils.NO_INDEX;
	private int stageIndex = Utils.NO_INDEX;
	private ViewPager viewPager;
	private CheckpointPagerAdapter pagerAdapter;
	private int lastVisitedPosition;
	private int currentPosition;

	public ChecklistStageFragment()
	{
		// Required empty public constructor
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState)
	{
		super.onSaveInstanceState(outState);

		outState.putInt(Utils.PARAM_BLOCK_INDEX, blockIndex);
		outState.putInt(Utils.PARAM_STAGE_INDEX, stageIndex);
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		if (Utils.DEBUG)
			Utils.d(LOG_TAG, "onCreate");

		if (getArguments() != null)
		{
			blockIndex = getArguments().getInt(Utils.PARAM_BLOCK_INDEX);
			stageIndex = getArguments().getInt(Utils.PARAM_STAGE_INDEX);
		}
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_checklist_stage, container, false);

		if (savedInstanceState != null)
		{
			blockIndex = savedInstanceState.getInt(Utils.PARAM_BLOCK_INDEX, Utils.NO_INDEX);
			stageIndex = savedInstanceState.getInt(Utils.PARAM_STAGE_INDEX, Utils.NO_INDEX);
		}

		Context context = v.getContext();
		CheckpointInterface checkpointInterface = CheckpointRepository.getInstance(context);
		List<ChecklistState> checklistStates = new ArrayList<>();

		// we only use Stage/Checkpoint model classes to make sure all is valid and setup indexes
		Stage stage = checkpointInterface.getStage(blockIndex, stageIndex);
		if (stage != null && stage.checkpoints != null)
		{
			UserEntriesInterface userEntries = new UserEntries(context);

			final int MAX_CHECKPOINTS = 7;
			int count = 0;
			int size = stage.checkpoints.size();
			for (int i = 0; i < size; i++)
			{
				boolean lastBlock = (checkpointInterface.getCountOfBlocks() - 1) == blockIndex;
				Checkpoint checkpoint = stage.checkpoints.get(i);

				if (checkpoint.entryType == EntryType.route)
				{
					boolean meetsCriteria = checkpoint.meetsCriteria(userEntries);
					if (meetsCriteria)
					{
						if (!TextUtils.isEmpty(checkpoint.routeFileName))
						{
							if (Utils.DEBUG)
								Utils.d(LOG_TAG, "nextCheckpoint meets criteria for %s, will route to %s",
									checkpointInterface.keyForBlockIndex(stageIndex, i),
									checkpoint.routeFileName);
						}
						else
						{
							if (Utils.DEBUG)
								Utils.d(LOG_TAG,
									"nextCheckpoint meets criteria for %s, but is MISSING a routeFileName for route checkpoint %s",
									checkpointInterface.keyForBlockIndex(stageIndex, i),
									checkpoint.routeFileName);

							meetsCriteria = false;
						}
					}
					else
					{
						if (Utils.DEBUG)
							Utils.d(LOG_TAG, "nextCheckpoint does NOT meet criteria for %s",
								checkpointInterface.keyForBlockIndex(stageIndex, i));
					}

					if (!meetsCriteria)
					{
						// unmet criteria == visited
						checkpointInterface.markVisited(stageIndex, i);

						// skip this checkpoint unless last one
						if (!lastBlock)
							continue;
					}

					checklistStates.add(new ChecklistState(blockIndex, stageIndex, i));
					count++;

					// Once we match on a route entry (eg. block3undoc.json, block3visa.json) we stop. The last
					// route entry doesn't have any criteria and is used as a fail safe (eg. block3citizen.json)
					if (meetsCriteria)
						break;
				}
				else if (checkpoint.entryType != null)
				{
					checklistStates.add(new ChecklistState(blockIndex, stageIndex, i));
					count++;
				}

				if (count > MAX_CHECKPOINTS)
					break;
			}
		}

		Resources resources = getResources();
		int padding = resources.getDimensionPixelSize(R.dimen.checkpoint_pager_padding);
		int margin = resources.getDimensionPixelSize(R.dimen.checkpoint_pager_margin);

		pagerAdapter = new CheckpointPagerAdapter(getChildFragmentManager(), checklistStates);

		viewPager = v.findViewById(R.id.viewpager_checkpoints);
		viewPager.setPadding(padding, padding, padding, padding);
		viewPager.setClipToPadding(false);
		viewPager.setPageMargin(margin);
		viewPager.addOnPageChangeListener(this);
		viewPager.setAdapter(pagerAdapter);

		setAsVisited(0);
		checkpointInterface.markVisited(stageIndex, 0);

		return v;
	}

	private void setAsVisited(int position)
	{
		CheckpointViewModel viewModel = pagerAdapter.getCurrentViewModel(position);
		if (viewModel != null)
			viewModel.checkpointSelected();

		if (lastVisitedPosition != position)
		{
			viewModel = pagerAdapter.getCurrentViewModel(lastVisitedPosition);
			if (viewModel != null)
				viewModel.saveCheckpointEntries();
			lastVisitedPosition = position;
		}
	}

	@Override
	public void onStop()
	{
		if (Utils.DEBUG)
			Utils.d(LOG_TAG, "onStop");

		Context context = getContext();
		if (context == null)
			return;

		CheckpointViewModel viewModel = pagerAdapter.getCurrentViewModel(lastVisitedPosition);
		if (viewModel != null)
			viewModel.saveCheckpointEntries();

		MyPlanRepository myPlanRepository = MyPlanRepository.getInstance(context);
		CheckpointInterface checkpointInterface = CheckpointRepository.getInstance(context);
		checkpointInterface.persistVisited(myPlanRepository);
		checkpointInterface.persistBlockCompletionInfo(blockIndex, myPlanRepository);

		super.onStop();
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
	{
		// no-op
	}

	@Override
	public void onPageSelected(int newPosition)
	{
		CheckpointViewModel viewModel = null;
		boolean isCompleted = true;
		if (lastVisitedPosition != newPosition)
		{
			viewModel = pagerAdapter.getCurrentViewModel(lastVisitedPosition);
			if (viewModel != null)
			{
				if (newPosition > lastVisitedPosition)
					isCompleted = viewModel.isCompleted();
			}
		}

		if (!isCompleted)
		{
			viewModel.showIncomplete.set(true);

			// force back to checkpoint that needs completing
			viewPager.setCurrentItem(currentPosition);
		}
		else
		{
			if (viewModel != null)
				viewModel.showIncomplete.set(false);

			// checkpoint is done, move on
			currentPosition = newPosition;

			setAsVisited(newPosition);
		}

		if (Utils.DEBUG)
			Utils.d(LOG_TAG, "onPageSelected isCompleted:%s position:%d", isCompleted, currentPosition);
	}

	@Override
	public void onPageScrollStateChanged(int state)
	{
		// no-op
	}

	/**
	 * Use this factory method to create a new instance of this fragment.
	 *
	 * @return A new instance of fragment ChecklistStageFragment.
	 */
	public static ChecklistStageFragment newInstance(int blockIndex, int stageIndex)
	{
		ChecklistStageFragment fragment = new ChecklistStageFragment();
		Bundle args = new Bundle();
		args.putInt(Utils.PARAM_BLOCK_INDEX, blockIndex);
		args.putInt(Utils.PARAM_STAGE_INDEX, stageIndex);
		fragment.setArguments(args);
		return fragment;
	}
}

package org.oregongoestocollege.itsaplan;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
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

import org.oregongoestocollege.itsaplan.data.Checkpoint;
import org.oregongoestocollege.itsaplan.data.CheckpointRepository;
import org.oregongoestocollege.itsaplan.data.EntryType;
import org.oregongoestocollege.itsaplan.data.Stage;
import org.oregongoestocollege.itsaplan.data.UserEntries;
import org.oregongoestocollege.itsaplan.data.UserEntriesInterface;
import org.oregongoestocollege.itsaplan.viewmodel.CheckpointViewModel;

/**
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class StepStageFragment extends Fragment implements ViewPager.OnPageChangeListener
{
	private static final String LOG_TAG = "GearUpStepStageFragment";
	private final int MAX_CHECKPOINTS = 7;
	private OnFragmentInteractionListener listener;
	private int blockIndex = Utils.NO_INDEX;
	private int stageIndex = Utils.NO_INDEX;
	private ViewPager viewPager;
	private CheckpointPagerAdapter pagerAdapter;
	private int lastVisitedPosition;

	public StepStageFragment()
	{
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of this fragment.
	 *
	 * @return A new instance of fragment StepStageFragment.
	 */
	public static StepStageFragment newInstance(int blockIndex, int stageIndex)
	{
		StepStageFragment fragment = new StepStageFragment();
		Bundle args = new Bundle();
		args.putInt(Utils.PARAM_BLOCK_INDEX, blockIndex);
		args.putInt(Utils.PARAM_STAGE_INDEX, stageIndex);
		fragment.setArguments(args);
		return fragment;
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
		View v = inflater.inflate(R.layout.fragment_step_stage, container, false);

		if (savedInstanceState != null)
		{
			blockIndex = savedInstanceState.getInt(Utils.PARAM_BLOCK_INDEX, Utils.NO_INDEX);
			stageIndex = savedInstanceState.getInt(Utils.PARAM_STAGE_INDEX, Utils.NO_INDEX);
		}

		Context context = getContext();
		List<CheckpointFragment> fragments = new ArrayList<>();

		// we only use Stage/Checkpoint model classes to make sure all is valid and setup indexes
		Stage stage = CheckpointRepository.getInstance().getStage(blockIndex, stageIndex);
		if (stage != null && stage.checkpoints != null && context != null)
		{
			UserEntriesInterface userEntries = new UserEntries(context);

			int count = 0;
			int size = stage.checkpoints.size();
			for (int i = 0; i < size; i++)
			{
				Checkpoint checkpoint = stage.checkpoints.get(i);

				if (checkpoint.entryType == EntryType.route)
				{
					boolean meetsCriteria = checkpoint.meetsCriteria(userEntries);
					if (meetsCriteria)
					{
						if (!TextUtils.isEmpty(checkpoint.routeFileName))
						{
							String log =
								String.format(Locale.US, "nextCheckpoint does meet criteria for %s, will route to %s",
									CheckpointRepository.getInstance().keyForBlockIndex(stageIndex, i),
									checkpoint.routeFileName);
							Utils.d(LOG_TAG, log);
						}
						else
						{
							String log = String.format(Locale.US,
								"nextCheckpoint does meet criteria for %s,  but is MISSING a routeFileName for route checkpoint %s",
								CheckpointRepository.getInstance().keyForBlockIndex(stageIndex, i),
								checkpoint.routeFileName);
							Utils.d(LOG_TAG, log);

							meetsCriteria = false;
						}
					}
					else
					{
						String log =
							String.format(Locale.US, "nextCheckpoint does NOT meet criteria for %s",
								CheckpointRepository.getInstance().keyForBlockIndex(stageIndex, i));
						Utils.d(LOG_TAG, log);
					}

					if (!meetsCriteria)
					{
						// unmet criteria == visited
						CheckpointRepository.getInstance().markVisited(stageIndex, i);

						// skip this checkpoint
						continue;
					}

					CheckpointFragment fragment = CheckpointFragment.newInstance(blockIndex, stageIndex, i);
					fragments.add(fragment);
					count++;
				}
				else
				{
					CheckpointFragment fragment = CheckpointFragment.newInstance(blockIndex, stageIndex, i);
					fragments.add(fragment);
					count++;
				}

				if (count > MAX_CHECKPOINTS)
					break;
			}

			if (!TextUtils.isEmpty(stage.title))
			{
				Activity activity = getActivity();
				if (activity != null)
					activity.setTitle(stage.title);
			}
		}

		Resources resources = getResources();
		int padding = resources.getDimensionPixelSize(R.dimen.checkpoint_pager_padding);
		int margin = resources.getDimensionPixelSize(R.dimen.checkpoint_pager_margin);

		pagerAdapter = new CheckpointPagerAdapter(getChildFragmentManager(), fragments);

		viewPager = v.findViewById(R.id.viewpager_checkpoints);
		viewPager.setPadding(padding, padding, padding, padding);
		viewPager.setClipToPadding(false);
		viewPager.setPageMargin(margin);
		viewPager.addOnPageChangeListener(this);
		viewPager.setAdapter(pagerAdapter);

		setAsVisited(v.getContext(), 0);
		CheckpointRepository.getInstance().markVisited(stageIndex, 0);

		return v;
	}

	private void setAsVisited(Context context, int position)
	{
		CheckpointViewModel viewModel = pagerAdapter.getCurrentViewModel(position);
		if (viewModel != null)
			viewModel.checkpointSelected();

		if (lastVisitedPosition != position)
		{
			viewModel = pagerAdapter.getCurrentViewModel(lastVisitedPosition);
			if (viewModel != null)
				viewModel.saveCheckpointEntries(new UserEntries(context));
			lastVisitedPosition = position;
		}
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
		CheckpointViewModel viewModel = pagerAdapter.getCurrentViewModel(lastVisitedPosition);
		if (viewModel != null)
			viewModel.saveCheckpointEntries(new UserEntries(getContext()));

		super.onDetach();
		listener = null;
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
	{
		// no-op
	}

	@Override
	public void onPageSelected(int position)
	{
		setAsVisited(getContext(), position);
		//Utils.d(LOG_TAG, "onPageSelected position:%d", position);
	}

	@Override
	public void onPageScrollStateChanged(int state)
	{
		// no-op
	}
}

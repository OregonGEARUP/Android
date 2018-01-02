package org.oregongoestocollege.itsaplan;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.oregongoestocollege.itsaplan.data.CheckpointRepository;
import org.oregongoestocollege.itsaplan.data.Stage;

/**
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class StepStageFragment extends Fragment
{
	private final int MAX_CHECKPOINTS = 6;
	private WeakReference<OnChecklistInteraction> listener;
	private int blockIndex;
	private int stageIndex;
	private ViewPager viewPager;
	private CheckpointPagerAdapter pagerAdapter;

	public StepStageFragment()
	{
		// Required empty public constructor
	}

	public void init(OnChecklistInteraction listener, int blockIndex, int stageIndex)
	{
		this.listener = new WeakReference<>(listener);
		this.blockIndex = blockIndex;
		this.stageIndex = stageIndex;
	}

	/**
	 * Use this factory method to create a new instance of this fragment.
	 *
	 * @return A new instance of fragment StepStageFragment.
	 */
	public static StepStageFragment newInstance()
	{
		return new StepStageFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_step_stage, container, false);

		List<Fragment> fragments = new ArrayList<>();

		// we only use Stage/Checkpoint model classes to make sure all is valid and setup indexes
		Stage stage = CheckpointRepository.getInstance().getStage(blockIndex, stageIndex);
		if (stage != null && stage.checkpoints != null)
		{
			int size = stage.checkpoints.size();

			for (int i = 0; i < size && i < MAX_CHECKPOINTS; i++)
			{
				CheckpointFragment fragment = CheckpointFragment.newInstance();
				fragment.init(blockIndex, stageIndex, i);
				fragments.add(fragment);
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
		viewPager.setAdapter(pagerAdapter);

		if (!TextUtils.isEmpty(stage.title))
		getActivity().setTitle(stage.title);

		return v;
	}
}

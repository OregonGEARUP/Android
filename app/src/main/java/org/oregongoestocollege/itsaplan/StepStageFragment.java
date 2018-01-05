package org.oregongoestocollege.itsaplan;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
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
import org.oregongoestocollege.itsaplan.support.Utils;

/**
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class StepStageFragment extends Fragment implements ViewPager.OnPageChangeListener
{
	private static final String LOG_TAG = "GearUpStepStageFragment";
	private final int MAX_CHECKPOINTS = 6;
	private OnFragmentInteractionListener listener;
	private int blockIndex;
	private int stageIndex;
	private ViewPager viewPager;
	private CheckpointPagerAdapter pagerAdapter;

	public StepStageFragment()
	{
		// Required empty public constructor
	}

	public void init(int blockIndex, int stageIndex)
	{
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

		List<CheckpointFragment> fragments = new ArrayList<>();

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

			if (!TextUtils.isEmpty(stage.title))
				getActivity().setTitle(stage.title);
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

		return v;
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

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
	{
		// no-op
	}

	@Override
	public void onPageSelected(int position)
	{
		Utils.d("GearUpStepStageFragment", "onPageSelected position:%d", position);
	}

	@Override
	public void onPageScrollStateChanged(int state)
	{
		// no-op
	}
}

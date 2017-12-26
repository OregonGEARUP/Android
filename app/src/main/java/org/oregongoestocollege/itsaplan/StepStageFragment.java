package org.oregongoestocollege.itsaplan;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.oregongoestocollege.itsaplan.data.Checkpoint;
import org.oregongoestocollege.itsaplan.data.Stage;

/**
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class StepStageFragment extends Fragment
{
	private WeakReference<OnChecklistInteraction> listener;
	private OnFragmentInteractionListener mListener;
	private Stage stage;
	private ViewPager viewPager;
	private CheckpointPagerAdapter pagerAdapter;

	public StepStageFragment()
	{
		// Required empty public constructor
	}

	public void init(OnChecklistInteraction listener, Stage stage)
	{
		this.listener = new WeakReference<>(listener);
		this.stage = stage;
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
		if (stage != null && stage.checkpoints != null)
		{
			for (Checkpoint checkpoint : stage.checkpoints)
			{
				fragments.add(CheckpointFragment.newInstance(checkpoint.description));
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

		getActivity().setTitle(stage.title);

		return v;
	}
}

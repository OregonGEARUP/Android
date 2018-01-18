package org.oregongoestocollege.itsaplan;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.oregongoestocollege.itsaplan.viewmodel.CheckpointViewModel;

/**
 * CheckpointPagerAdapter
 *
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class CheckpointPagerAdapter extends FragmentPagerAdapter
{
	private List<CheckpointFragment> fragments;
	private int size;

	CheckpointPagerAdapter(FragmentManager fm, List<CheckpointFragment> fragments)
	{
		super(fm);

		this.fragments = fragments;
		this.size = fragments != null ? fragments.size() : 0;
	}

	@Override
	public Fragment getItem(int position)
	{
		return this.fragments.get(position);
	}

	@Override
	public int getCount()
	{
		return this.fragments.size();
	}

	public CheckpointViewModel getCurrentViewModel(int position)
	{
		if (fragments != null && position >= 0 && position < size)
			return fragments.get(position).getCheckpointViewModel();

		return null;
	}
}

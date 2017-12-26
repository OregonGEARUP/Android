package org.oregongoestocollege.itsaplan;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * CheckpointPagerAdapter
 *
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class CheckpointPagerAdapter extends FragmentPagerAdapter
{
	private List<Fragment> fragments;

	public CheckpointPagerAdapter(FragmentManager fm, List<Fragment> fragments)
	{
		super(fm);

		this.fragments = fragments;
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
}

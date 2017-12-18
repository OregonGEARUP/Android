package org.oregongoestocollege.itsaplan.support;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

/**
 * BottomBarAdapter
 * Oregon GEAR UP App
 *
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class BottomBarAdapter extends SmartFragmentStatePageAdapter
{
	private final List<Fragment> fragments = new ArrayList<>();

	public BottomBarAdapter(FragmentManager fragmentManager)
	{
		super(fragmentManager);
	}

	/**
	 * Add fragments for our bottom navigation
	 */
	public void addFragments(Fragment fragment)
	{
		fragments.add(fragment);
	}

	@Override
	public Fragment getItem(int position)
	{
		return fragments.get(position);
	}

	@Override
	public int getCount()
	{
		return fragments.size();
	}

	public static Fragment getCurrentFragment(ViewPager viewPager)
	{
		if (viewPager != null)
		{
			int position = viewPager.getCurrentItem();
			BottomBarAdapter adapter = (BottomBarAdapter) viewPager.getAdapter();
			if (adapter != null)
				return adapter.getItem(position);
		}
		return null;
	}
}
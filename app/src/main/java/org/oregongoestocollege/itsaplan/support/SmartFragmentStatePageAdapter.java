package org.oregongoestocollege.itsaplan.support;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import org.oregongoestocollege.itsaplan.Utils;

/**
 * SmartFragmentStatePageAdapter - extension of FragmentStatePagerAdapter which intelligently
 * caches all active fragments and manages the fragment lifecycles. Usage involves extending
 * from SmartFragmentStatePagerAdapter as you would any other PagerAdapter.
 *
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public abstract class SmartFragmentStatePageAdapter extends FragmentStatePagerAdapter
{
	// Sparse array to keep track of registered fragments in memory
	private SparseArray<Fragment> registeredFragments = new SparseArray<>();
	private String logTag;

	public SmartFragmentStatePageAdapter(String logTag, FragmentManager fragmentManager)
	{
		super(fragmentManager);

		this.logTag = logTag;
	}

	// Register the fragment when the item is instantiated
	@Override
	public Object instantiateItem(ViewGroup container, int position)
	{
		Utils.d(logTag, "instantiateItem position:%d", position);

		Fragment fragment = (Fragment)super.instantiateItem(container, position);
		registeredFragments.put(position, fragment);
		return fragment;
	}

	// Unregister when the item is inactive
	@Override
	public void destroyItem(ViewGroup container, int position, Object object)
	{
		Utils.d(logTag, "destroyItem position:%d", position);

		registeredFragments.remove(position);
		super.destroyItem(container, position, object);
	}

	// Returns the fragment for the position (if instantiated)
	public Fragment getRegisteredFragment(int position)
	{
		return registeredFragments.get(position);
	}
}

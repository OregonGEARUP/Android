package org.oregongoestocollege.itsaplan.support;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import org.oregongoestocollege.itsaplan.Utils;

/**
 * SmartFragmentStatePageAdapter - extension of FragmentStatePagerAdapter which intelligently
 * caches all active fragments and manages the fragment lifecycles. Usage involves extending
 * from SmartFragmentStatePagerAdapter as you would any other PagerAdapter.
 *
 * Oregon GEAR UP App
 * Copyright © 2020 Oregon GEAR UP. All rights reserved.
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
		if (Utils.DEBUG)
			Utils.d(logTag, "instantiateItem position:%d", position);

		Fragment fragment = (Fragment)super.instantiateItem(container, position);
		registeredFragments.put(position, fragment);
		return fragment;
	}

	// Unregister when the item is inactive
	@Override
	public void destroyItem(ViewGroup container, int position, Object object)
	{
		if (Utils.DEBUG)
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

package org.oregongoestocollege.itsaplan.common;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

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
}
package org.oregongoestocollege.itsaplan.support;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.oregongoestocollege.itsaplan.ChecklistFragment;
import org.oregongoestocollege.itsaplan.InfoFragment;
import org.oregongoestocollege.itsaplan.MainActivity;
import org.oregongoestocollege.itsaplan.MyPlanFragment;
import org.oregongoestocollege.itsaplan.PasswordContainerFragment;
import org.oregongoestocollege.itsaplan.Utils;

/**
 * BottomBarAdapter
 * Oregon GEAR UP App
 *
 * Copyright © 2020 Oregon GEAR UP. All rights reserved.
 */
public class BottomBarAdapter extends SmartFragmentStatePageAdapter
{
	public BottomBarAdapter(FragmentManager fragmentManager)
	{
		super(MainActivity.LOG_TAG, fragmentManager);
	}

	@Override
	public Fragment getItem(int position)
	{
		if (Utils.DEBUG)
			Utils.d(MainActivity.LOG_TAG, "getItem position:%d", position);

		// When we initially run the app the ViewPager calls getItem for creating fragments.
		// But when re-creating activity (eg. rotate) the ViewPager restores the fragments
		// from it's instance state and we do not come through this code
		if (position == 3)
			return new InfoFragment();
		else if (position == 2)
			return new PasswordContainerFragment();
		else if (position == 1)
			return new MyPlanFragment();
		else
			return new ChecklistFragment();
	}

	@Override
	public int getCount()
	{
		return 4;
	}
}
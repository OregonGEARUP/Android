package org.oregongoestocollege.itsaplan;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.oregongoestocollege.itsaplan.data.ChecklistState;
import org.oregongoestocollege.itsaplan.support.SmartFragmentStatePageAdapter;
import org.oregongoestocollege.itsaplan.viewmodel.CheckpointViewModel;

/**
 * CheckpointPagerAdapter
 *
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class CheckpointPagerAdapter extends SmartFragmentStatePageAdapter
{
	private List<ChecklistState> checklistStates;
	private int size;

	CheckpointPagerAdapter(FragmentManager fm, List<ChecklistState> checklistStates)
	{
		super(CheckpointFragment.LOG_TAG, fm);

		this.checklistStates = checklistStates;
		this.size = checklistStates != null ? checklistStates.size() : 0;
	}

	@Override
	public Fragment getItem(int position)
	{
		// When we initially run the app the ViewPager calls getItem for creating fragments.
		// But when re-creating activity (eg. rotate) the ViewPager restores the fragments
		// from it's instance state and we do not come through this code

		ChecklistState state = checklistStates.get(position);

		if (Utils.DEBUG)
			Utils.d(CheckpointFragment.LOG_TAG, "getItem position:%d, blockIndex:%d, stageIndex:%d, checkpointIndex:%d",
				position, state.blockIndex, state.stageIndex, state.checkpointIndex);

		return CheckpointFragment.newInstance(state.blockIndex, state.stageIndex, state.checkpointIndex);
	}

	@Override
	public int getCount()
	{
		return size;
	}

	public CheckpointViewModel getCurrentViewModel(int position)
	{
		Fragment fragment = getRegisteredFragment(position);
		if (fragment instanceof CheckpointFragment)
		{
			return ((CheckpointFragment)fragment).getCheckpointViewModel();
		}

		return null;
	}
}

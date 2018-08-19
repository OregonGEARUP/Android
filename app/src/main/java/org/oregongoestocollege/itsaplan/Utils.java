package org.oregongoestocollege.itsaplan;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

/**
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class Utils
{
	static final String PARAM_BLOCK_FILE_NAME = "blockFileName";
	static final String PARAM_BLOCK_INDEX = "blockIndex";
	static final String PARAM_STAGE_INDEX = "stageIndex";
	static final String PARAM_CHECKPOINT_INDEX = "checkpointIndex";
	/**
	 * Default value when an index has not been set.
	 */
	public static final int NO_INDEX = -1;

	/**
	 * Helper method that checks for bread crumb titles on child fragments and if found set's
	 * the activity title to the bread crumb title. Works because we call
	 * {@link android.support.v4.app.FragmentTransaction#setBreadCrumbTitle(int)}.
	 */
	static int updateTitleOnBackStackChanged(@NonNull Fragment fragment, @NonNull String logTag)
	{
		FragmentActivity activity = fragment.getActivity();
		if (activity == null)
			return 0;

		FragmentManager fragmentManager = fragment.getChildFragmentManager();
		int count = fragmentManager.getBackStackEntryCount();

		if (Utils.DEBUG)
			Utils.d(logTag, "updateTitleOnBackStackChanged entries=%d", count);

		while (count > 0)
		{
			FragmentManager.BackStackEntry entry = fragmentManager.getBackStackEntryAt(--count);
			int resId = entry.getBreadCrumbTitleRes();
			CharSequence title;
			if (resId > 0)
				activity.setTitle(resId);
			else if ((title = entry.getBreadCrumbTitle()) != null)
				activity.setTitle(title);
			else
				continue;
			return count;
		}
		return count;
	}

	/**
	 * Set to true ONLY when developing. Using flag versus Log.isLoggable().
	 */
	public static final boolean DEBUG = true;

	public static void d(String tag, String format, Object... args)
	{
		Log.d(tag, String.format(format, args));
	}
}

package org.oregongoestocollege.itsaplan.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import android.text.TextUtils;

import org.oregongoestocollege.itsaplan.SingleLiveEvent;
import org.oregongoestocollege.itsaplan.Utils;

/**
 * Use for navigation between My Plan tasks such as colleges, calendar, etc. When creating this
 * view model it should always be scoped to the activity so that the current task stays in sync.
 * Eg: {@link androidx.lifecycle.ViewModelProviders#of(FragmentActivity)}
 * <p>
 *  This class works slightly differently than {@link ChecklistNavViewModel}. For My Plan tasks
 *  we use the {@link #resetTask()} to add/remove/pop child fragments. This allows us to keep
 *  state in this class and only notify observers on change.
 *  Future - could sync up how these operate.
 * </p>
 * Oregon GEAR UP App
 * Copyright © 2020 Oregon GEAR UP. All rights reserved.
 */
public class MyPlanNavViewModel extends ViewModel
{
	// debug
	private static final String LOG_TAG = "GearUp_MyPlanNavViewModel";
	private static int count = 0;
	public static final String MY_PLAN_COLLEGES = "colleges";
	public static final String MY_PLAN_SCHOLARSHIPS = "scholarships";
	public static final String MY_PLAN_TESTS = "tests";
	public static final String MY_PLAN_RESIDENCY = "residency";
	public static final String MY_PLAN_CALENDAR = "calendar";
	// the current My Plan task
	private final SingleLiveEvent<String> currentTask = new SingleLiveEvent<>();

	public MyPlanNavViewModel()
	{
		if (Utils.DEBUG)
			Utils.d(LOG_TAG, "create %d", ++count);
	}

	public LiveData<String> getCurrentTask()
	{
		return currentTask;
	}

	public void setCurrentTask(String task)
	{
		if (!TextUtils.isEmpty(task) && !task.equals(currentTask.getValue()))
			currentTask.setValue(task);
	}

	public boolean resetTask()
	{
		if (!TextUtils.isEmpty(getCurrentTask().getValue()))
		{
			currentTask.setValue(null);
			return true;
		}
		return false;
	}

	static String getOptionFromUrl(@NonNull String url)
	{
		if (url.endsWith(MY_PLAN_COLLEGES))
			return MY_PLAN_COLLEGES;
		else if (url.endsWith(MY_PLAN_SCHOLARSHIPS))
			return MY_PLAN_SCHOLARSHIPS;
		else if (url.endsWith(MY_PLAN_TESTS))
			return MY_PLAN_TESTS;
		else if (url.endsWith(MY_PLAN_RESIDENCY))
			return MY_PLAN_RESIDENCY;
		else if (url.endsWith(MY_PLAN_CALENDAR))
			return MY_PLAN_CALENDAR;

		return null;
	}
}

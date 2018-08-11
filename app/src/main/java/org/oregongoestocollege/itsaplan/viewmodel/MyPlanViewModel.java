package org.oregongoestocollege.itsaplan.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.oregongoestocollege.itsaplan.SingleLiveEvent;
import org.oregongoestocollege.itsaplan.Utils;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class MyPlanViewModel extends ViewModel
{
	// debug
	public static final String LOG_TAG = "GearUpMyPlan";
	private static int count = 0;
	private int mycount;
	public static final String MY_PLAN_COLLEGES = "colleges";
	public static final String MY_PLAN_SCHOLARSHIPS = "scholarships";
	public static final String MY_PLAN_TESTS = "tests";
	public static final String MY_PLAN_RESIDENCY = "residency";
	public static final String MY_PLAN_CALENDAR = "calendar";
	// the current My Plan task
	private final SingleLiveEvent<String> currentTask = new SingleLiveEvent<>();

	public MyPlanViewModel()
	{
		mycount = ++count;
		Utils.d(LOG_TAG, "create %d", mycount);
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

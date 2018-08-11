package org.oregongoestocollege.itsaplan.viewmodel;

import android.support.annotation.NonNull;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class MyPlanViewModel
{
	public static final String MY_PLAN_COLLEGES = "colleges";
	public static final String MY_PLAN_SCHOLARSHIPS = "scholarships";
	public static final String MY_PLAN_TESTS = "tests";
	public static final String MY_PLAN_RESIDENCY = "residency";
	public static final String MY_PLAN_CALENDAR = "calendar";

	public static String getOptionFromUrl(@NonNull String url)
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

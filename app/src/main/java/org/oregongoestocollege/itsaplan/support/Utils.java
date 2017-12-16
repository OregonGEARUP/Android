package org.oregongoestocollege.itsaplan.support;

import android.util.Log;

/**
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class Utils
{
	/**
	 * Set to true ONLY when developing. Using flag versus Log.isLoggable().
	 */
	public static final boolean DEBUG = true;

	public static void d(String tag, String format, Object... args)
	{
		Log.d(tag, String.format(format, args));
	}
}

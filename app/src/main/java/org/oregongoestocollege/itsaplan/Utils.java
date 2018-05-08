package org.oregongoestocollege.itsaplan;

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
	static final String PARAM_OPTION_NAME = "optionName";
	/**
	 * Default value when an index has not been set.
	 */
	public static final int NO_INDEX = -1;

	/**
	 * Set to true ONLY when developing. Using flag versus Log.isLoggable().
	 */
	public static final boolean DEBUG = true;

	public static void d(String tag, String format, Object... args)
	{
		Log.d(tag, String.format(format, args));
	}
}

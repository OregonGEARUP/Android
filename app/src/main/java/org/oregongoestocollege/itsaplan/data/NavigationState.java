package org.oregongoestocollege.itsaplan.data;

import androidx.annotation.Nullable;

/**
 * Holds the state needed to operate on itsaplan:// links.
 *
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class NavigationState
{
	public final int index;
	public final String option;

	public NavigationState(int index, @Nullable String option)
	{
		this.index = index;
		this.option = option;
	}
}

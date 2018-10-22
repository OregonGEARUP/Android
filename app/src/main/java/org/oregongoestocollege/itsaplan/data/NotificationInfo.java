package org.oregongoestocollege.itsaplan.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Holds the info needed to schedule a notification from user entered data.
 *
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class NotificationInfo
{
	@NonNull
	public final String dateKey;
	@Nullable
	public String descriptionKey;

	public NotificationInfo(@NonNull String dateKey, @Nullable String descriptionKey)
	{
		this.dateKey = dateKey;
		this.descriptionKey = descriptionKey;
	}
}

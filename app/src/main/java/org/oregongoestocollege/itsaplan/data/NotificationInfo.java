package org.oregongoestocollege.itsaplan.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Holds the info needed to schedule a notification from user entered data.
 *
 * Oregon GEAR UP App
 * Copyright © 2020 Oregon GEAR UP. All rights reserved.
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

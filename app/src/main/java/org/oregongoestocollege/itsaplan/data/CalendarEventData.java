package org.oregongoestocollege.itsaplan.data;

import java.util.List;

import android.support.annotation.NonNull;
import android.text.TextUtils;

/**
 * CalendarEventData - POJO to retrieve data from network
 * Oregon GEAR UP App
 *
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class CalendarEventData
{
	public List<String> date;
	public List<String> description;
	public String reminderId;
	public String reminder;
	public int reminderDelta;

	public boolean hasReminderId()
	{
		return !TextUtils.isEmpty(reminderId);
	}

	public boolean hasDateOrDescriptionKey(@NonNull NotificationInfo notificationInfo)
	{
		boolean match = false;

		final String dateKey = notificationInfo.dateKey;
		if (date != null && !TextUtils.isEmpty(dateKey))
		{
			// see if we have a match on the key
			for (String s : date)
			{
				if (s.startsWith("##") && s.endsWith("##"))
				{
					String key = getAdjustedDateKey(s);
					if (dateKey.equals(key))
					{
						match = true;
						break;
					}
				}
			}
		}

		// if we haven't found the date, try the description
		if (!match)
		{
			final String descriptionKey = notificationInfo.descriptionKey;
			if (description != null && !TextUtils.isEmpty(descriptionKey))
			{
				// see if we have a match on the key
				for (String s : description)
				{
					if (descriptionKey.equals(s))
					{
						match = true;
						break;
					}
				}
			}
		}

		return match;
	}

	public String getAdjustedDateKey(@NonNull String originalKey)
	{
		// Android code stores EntryType.dateOnly user entered data with `_date` in the key
		// iOS did not. Because of this we were not showing date only calendar events.
		// (##b5citizen_s4_cp1_i1## - Financial Aid Workshop)
		// Since we already released a version of the app before the calendar feature was
		// done. Adjust the key to the format used by Android.
		String key = originalKey.substring(2, originalKey.length() - 2);
		if (!key.endsWith("_date"))
			key += "_date";

		return key;
	}
}

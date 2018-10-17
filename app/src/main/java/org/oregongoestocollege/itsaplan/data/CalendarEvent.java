package org.oregongoestocollege.itsaplan.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.oregongoestocollege.itsaplan.data.dao.DateConverter;

/**
 * CalendarEvent
 * Oregon GEAR UP App
 *
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class CalendarEvent
{
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
	// data from the JSON files on the website
	@NonNull
	private CalendarEventData data;
	// support data
	@NonNull
	private Date eventDate;
	@NonNull
	private String eventDescription;

	private CalendarEvent(@NonNull CalendarEventData data, @NonNull Date eventDate, @NonNull String eventDescription)
	{
		this.data = data;
		this.eventDate = eventDate;
		this.eventDescription = eventDescription;
	}

	@NonNull
	public Date getEventDate()
	{
		return eventDate;
	}

	@NonNull
	public String getEventDescription()
	{
		return eventDescription;
	}

	/**
	 * Using calendar data from the network, build a {@link CalendarEvent} that substitutes user
	 * entered data where appropriate.
	 *
	 * @param data {@link CalendarEventData} from the GearUP website
	 * @param userEntries {@link UserEntriesInterface} used to retrieve user entered data
	 * @return fully populated {@link CalendarEvent} or null
	 */
	@Nullable
	public static CalendarEvent from(Context context, CalendarEventData data, UserEntriesInterface userEntries)
	{
		Date eventDate = null;
		String eventDescription = null;

		List<String> dates = data.date;
		if (dates != null)
		{
			int size = dates.size();

			for (int i = 0; i < size; i++)
			{
				String s = dates.get(i);

				if (s.startsWith("##") && s.endsWith("##"))
				{
					// Android code stores EntryType.dateOnly user entered data with `_date` in the key
					// iOS did not. Because of this we were not showing date only calendar events.
					// (##b5citizen_s4_cp1_i1## - Financial Aid Workshop)
					// Since we already released a version of the app before the calendar feature was
					// done. Adjust the key to the format used by Android.
					String key = s.substring(2, s.length() - 2);
					if (!key.endsWith("_date"))
						key += "_date";

					long date = userEntries.getValueAsLong(key);
					if (date > 0)
						eventDate = DateConverter.toDate(date);
				}
				else
				{
					try
					{
						eventDate = dateFormat.parse(s);
					}
					catch (ParseException e)
					{
						// no-op
					}
				}

				// if we have a date, make sure we have a corresponding description
				if (eventDate != null)
				{
					List<String> descriptions = data.description;
					if (descriptions != null)
					{
						// first try to get the corresponding indexed description
						s = (i < descriptions.size()) ? descriptions.get(i) : null;

						if (!TextUtils.isEmpty(s))
						{
							eventDescription = userEntries.stringWithSubstitutions(context, s);
						}
						else if (i > 0)
						{
							// should be a default description with no substitution
							eventDescription = descriptions.get(0);
						}
					}
				}

				// use first date & description found
				if (eventDate != null && eventDescription != null)
					break;
			}
		}

		if (eventDate != null && eventDescription != null)
			return new CalendarEvent(data, eventDate, eventDescription);
		else
			return null;
	}

	@Nullable
	public static CalendarEvent from(College college, int index)
	{
		if (college != null && college.hasApplicationDate())
		{
			CalendarEventData data = new CalendarEventData();
			data.reminderId = String.format(Locale.US, "collegeApp-%d", index);
			data.reminder = String
				.format(Locale.getDefault(), "The %s application is due in one week! Have you submitted it?",
					college.getName());
			data.reminderDelta = -7;

			return new CalendarEvent(data, college.getApplicationDate(),
				String.format(Locale.getDefault(), "%s  application deadline", college.getName()));
		}

		return null;
	}

	@Nullable
	public static CalendarEvent from(Scholarship scholarship, int index)
	{
		if (scholarship != null && scholarship.hasApplicationDate())
		{
			CalendarEventData data = new CalendarEventData();
			data.reminderId = String.format(Locale.US, "scholarshipApp-%d", index);
			data.reminder = String
				.format(Locale.getDefault(), "The %s application is due in one week! Have you submitted it?",
					scholarship.getName());
			data.reminderDelta = -7;

			return new CalendarEvent(data, scholarship.getApplicationDate(),
				String.format(Locale.getDefault(), "%s  application deadline", scholarship.getName()));
		}

		return null;
	}

	@Nullable
	public static CalendarEvent from(TestResult testResult)
	{
		if (testResult != null && testResult.hasTestDate())
		{
			final String testName = testResult.getName();
			if (TestResult.NAME_ACT.equals(testName))
			{
				CalendarEventData data = new CalendarEventData();
				data.reminderId = "testResultApp-act";
				data.reminder = "Good luck on the ACT tomorrow! Get plenty of rest and eat a good breakfast.";
				data.reminderDelta = -7;

				return new CalendarEvent(data, testResult.getDate(), "ACT Test");
			}
			else if (TestResult.NAME_SAT.equals(testName))
			{
				CalendarEventData data = new CalendarEventData();
				data.reminderId = "testResultApp-sat";
				data.reminder = "Good luck on the SAT tomorrow! Get plenty of rest and eat a good breakfast.";
				data.reminderDelta = -7;

				return new CalendarEvent(data, testResult.getDate(), "SAT Test");
			}
		}

		return null;
	}

	public boolean hasReminderInfo()
	{
		return !TextUtils.isEmpty(data.reminderId) && !TextUtils.isEmpty(data.reminder);
	}

	public int getReminderId()
	{
		// we need a numeric value for notification identifiers
		if (!TextUtils.isEmpty(data.reminderId))
			return Integer.valueOf(data.reminderId, 36);

		return 0;
	}

	public String getReminderMessage()
	{
		return data.reminder;
	}

	public int getReminderDelta()
	{
		return data.reminderDelta;
	}
}

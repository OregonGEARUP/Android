package org.oregongoestocollege.itsaplan.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
	private List<String> date;
	private List<String> description;
	private String reminderId;
	private String reminder;
	private int reminderDelta;
	// support data
	private transient boolean initialized;
	private transient Date eventDate;
	private transient String eventDescription;

	private void initialize()
	{
		if (initialized)
			return;

		if (date != null && !date.isEmpty())
		{
			String s = date.get(0);
			if (s.startsWith("##") && s.endsWith("##"))
			{
				// todo find user entered date
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
		}

		// if we have a date, make sure we have a corresponding description
		if (eventDate != null)
		{
			if (description != null && !description.isEmpty())
				eventDescription = description.get(0);
			else
				eventDate = null;
		}

		initialized = true;
	}

	List<String> getDate()
	{
		return date;
	}

	void setDate(List<String> date)
	{
		this.date = date;
	}

	List<String> getDescription()
	{
		return description;
	}

	void setDescription(List<String> description)
	{
		this.description = description;
	}

	String getReminderId()
	{
		return reminderId;
	}

	void setReminderId(String reminderId)
	{
		this.reminderId = reminderId;
	}

	String getReminder()
	{
		return reminder;
	}

	void setReminder(String reminder)
	{
		this.reminder = reminder;
	}

	int getReminderDelta()
	{
		return reminderDelta;
	}

	public void setReminderDelta(int reminderDelta)
	{
		this.reminderDelta = reminderDelta;
	}

	public Date getEventDate()
	{
		initialize();

		return eventDate;
	}

	public String getEventDescription()
	{
		initialize();

		return eventDescription;
	}

	public boolean hasEventDetails()
	{
		initialize();

		return eventDate != null;
	}
}

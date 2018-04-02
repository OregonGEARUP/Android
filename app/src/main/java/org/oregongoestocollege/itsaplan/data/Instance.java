package org.oregongoestocollege.itsaplan.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.content.Context;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.text.format.DateFormat;
import android.util.Log;

import org.oregongoestocollege.itsaplan.R;

/**
 * Instance
 * Oregon GEAR UP App
 *
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class Instance
{
	private static final String DATE_FORMAT = "yyyyMMdd";
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);
	private String id;
	private String prompt;
	private String placeholder;
	// stored data
	public long dateValue;
	// fields to support UX display
	public boolean isChecked;
	public final ObservableField<String> textEntry = new ObservableField<>();
	public final ObservableField<String> dateText = new ObservableField<>();

	public String getId()
	{
		return id;
	}

	public String getPrompt()
	{
		return prompt;
	}

	public String getPlaceholder()
	{
		return placeholder;
	}

	public Calendar getDate()
	{
		return getCalendar(dateValue);
	}

	// temporary - storing as string, will go away
	public void setDate(@NonNull Context context, int year, int month, int day)
	{
		final Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(year, month, day);

		// update UX
		dateText.set(DateFormat.getLongDateFormat(context).format(calendar.getTime()));
		// update stored value
		dateValue = Long.parseLong(dateFormat.format(calendar.getTime()));
	}

	// temporary - storing as string, will go away
	public void setDate(@NonNull Context context, long date)
	{
		if (date > 0)
		{
			final Calendar calendar = getCalendar(date);
			// update UX
			dateText.set(DateFormat.getLongDateFormat(context).format(calendar.getTime()));
		}
		else
			dateText.set(context.getResources().getString(R.string.hint_date_picker));

		dateValue = date;
	}

	public void onInstanceChecked(boolean checked)
	{
		isChecked = checked;
	}

	// temporary - storing as string, will go away
	private static Calendar getCalendar(long value)
	{
		final Calendar now = Calendar.getInstance();

		if (value > 0)
		{
			try
			{
				now.setTime(dateFormat.parse(String.valueOf(value)));
			}
			catch (ParseException e)
			{
				Log.w("EntryType.date", e);
			}
		}

		return now;
	}
}

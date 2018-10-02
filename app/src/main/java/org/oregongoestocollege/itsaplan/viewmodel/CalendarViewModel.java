package org.oregongoestocollege.itsaplan.viewmodel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import org.oregongoestocollege.itsaplan.R;
import org.oregongoestocollege.itsaplan.compactcalendarview.domain.Event;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class CalendarViewModel extends AndroidViewModel
{
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
	private final ObservableField<List<CalendarEventViewModel>> eventViewModels = new ObservableField<>();
	private final ObservableField<String> calendarTitle = new ObservableField<>();

	public CalendarViewModel(@NonNull Application application)
	{
		super(application);
	}

	public ObservableField<String> getCalendarTitle()
	{
		return calendarTitle;
	}

	public void setCalendarTitle(Date firstDayOfMonth)
	{
		if (firstDayOfMonth != null)
			calendarTitle.set(dateFormat.format(firstDayOfMonth));

	}

	public ObservableField<List<CalendarEventViewModel>> getEventViewModels()
	{
		return eventViewModels;
	}

	public void setEventViewModels(List<Event> events)
	{
		List<CalendarEventViewModel> newViewModels = new ArrayList<>();

		if (events != null)
		{
			int count = 0;

			for (Event event : events)
			{
				CalendarEventViewModel vm = new CalendarEventViewModel(
					String.format(Locale.getDefault(), "event %d", ++count));
				newViewModels.add(vm);
			}
		}

		if (newViewModels.isEmpty())
			newViewModels.add(new CalendarEventViewModel(getApplication().getString(R.string.calendar_no_events)));

		eventViewModels.set(newViewModels);
	}
}

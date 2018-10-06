package org.oregongoestocollege.itsaplan.viewmodel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import org.oregongoestocollege.itsaplan.R;
import org.oregongoestocollege.itsaplan.Utils;
import org.oregongoestocollege.itsaplan.compactcalendarview.domain.Event;
import org.oregongoestocollege.itsaplan.data.CalendarEvent;
import org.oregongoestocollege.itsaplan.data.ResponseData;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class CalendarViewModel extends AndroidViewModel
{
	private static final String LOG_TAG = "GearUp_CalendarViewModel";
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
	private final ObservableBoolean isLoading = new ObservableBoolean();
	private final ObservableField<List<CalendarEventViewModel>> eventViewModels = new ObservableField<>();
	private final ObservableField<String> calendarTitle = new ObservableField<>();
	private Date userSelectedDate;

	public CalendarViewModel(@NonNull Application application)
	{
		super(application);
	}

	private List<Event> createEvents(List<CalendarEvent> calendarEvents)
	{
		List<Event> events = null;
		if (calendarEvents != null)
		{
			int color = ContextCompat.getColor(getApplication(), R.color.colorAccent);

			events = new ArrayList<>();

			for (CalendarEvent calendarEvent : calendarEvents)
			{
				if (calendarEvent.hasEventDetails())
				{
					events.add(new Event(color, calendarEvent.getEventDate().getTime(), calendarEvent));
				}
			}
		}
		return events;
	}

	public ObservableBoolean getIsLoading()
	{
		return isLoading;
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

	public Date getSelectedDate()
	{
		return userSelectedDate;
	}

	/**
	 * Shows the events for a user selected date and keeps track of the selection.
	 *
	 * @param events the {@link Event}s for a given calendar date
	 */
	public void dateSelected(Date selectedDate, List<Event> events)
	{
		List<CalendarEventViewModel> newViewModels = new ArrayList<>();

		if (events != null)
		{
			for (Event event : events)
			{
				// skipping the instance of check since we know we created it
				CalendarEvent calendarEvent = (CalendarEvent)event.getData();
				if (calendarEvent != null)
				{
					CalendarEventViewModel vm = new CalendarEventViewModel(calendarEvent.getEventDescription());
					newViewModels.add(vm);
				}
			}
		}

		if (newViewModels.isEmpty())
			newViewModels.add(new CalendarEventViewModel(getApplication().getString(R.string.calendar_no_events)));

		eventViewModels.set(newViewModels);

		userSelectedDate = selectedDate;
	}

	public List<Event> onDataChanged(ResponseData<List<CalendarEvent>> response)
	{
		List<Event> events = null;

		if (response != null)
		{
			Utils.d(LOG_TAG, "onDataChanged status:%s", response.status);

			switch (response.status)
			{
			case LOADING:
				isLoading.set(true);
				break;
			case ERROR:
				isLoading.set(false);
				break;
			case SUCCESS:
				events = createEvents(response.data);
				isLoading.set(false);
				break;
			}
		}

		return events;
	}
}

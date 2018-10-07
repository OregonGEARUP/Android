package org.oregongoestocollege.itsaplan.viewmodel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import org.oregongoestocollege.itsaplan.R;
import org.oregongoestocollege.itsaplan.Utils;
import org.oregongoestocollege.itsaplan.compactcalendarview.domain.Event;
import org.oregongoestocollege.itsaplan.data.CalendarEvent;
import org.oregongoestocollege.itsaplan.data.MyPlanRepository;
import org.oregongoestocollege.itsaplan.data.ResponseData;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class CalendarViewModel extends AndroidViewModel
{
	private static final String LOG_TAG = "GearUp_CalendarViewModel";
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
	private Date lastSelectedDate;
	private ResponseData.Status lastStatus;
	// data binding fields
	private final ObservableBoolean loading = new ObservableBoolean();
	private final ObservableField<List<CalendarItemViewModel>> itemViewModels = new ObservableField<>();
	private final ObservableField<String> calendarTitle = new ObservableField<>();

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
				events.add(new Event(color, calendarEvent.getEventDate().getTime(), calendarEvent));
			}
		}
		return events;
	}

	public ObservableBoolean isLoading()
	{
		return loading;
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

	public ObservableField<List<CalendarItemViewModel>> getItemViewModels()
	{
		return itemViewModels;
	}

	@Nullable
	public Date getSelectedDate()
	{
		return lastSelectedDate;
	}

	public void start()
	{
		if (Utils.DEBUG)
			Utils.d(LOG_TAG, "start lastStatus:%s", lastStatus);

		// if we've already successfully retrieved calendar events do nothing
		if (lastStatus == ResponseData.Status.SUCCESS)
			return;

		MyPlanRepository myPlanRepository = MyPlanRepository.getInstance(getApplication());
		myPlanRepository.loadCalendarEvents(getApplication());
	}

	/**
	 * Shows the events for a user selected date and keeps track of the selection.
	 *
	 * @param events the {@link Event}s for a given calendar date
	 */
	public void dateSelected(Date selectedDate, List<Event> events)
	{
		List<CalendarItemViewModel> newViewModels = new ArrayList<>();

		if (events != null)
		{
			for (Event event : events)
			{
				// skipping the instance of check since we know we created it
				CalendarEvent calendarEvent = (CalendarEvent)event.getData();
				if (calendarEvent != null)
				{
					CalendarItemViewModel vm = new CalendarItemViewModel(calendarEvent.getEventDescription());
					newViewModels.add(vm);
				}
			}
		}

		if (newViewModels.isEmpty())
			newViewModels.add(new CalendarItemViewModel(getApplication().getString(R.string.calendar_no_events)));

		itemViewModels.set(newViewModels);

		lastSelectedDate = selectedDate;
	}

	public List<Event> onDataChanged(ResponseData<List<CalendarEvent>> response)
	{
		List<Event> events = null;

		if (response != null)
		{
			if (Utils.DEBUG)
				Utils.d(LOG_TAG, "onDataChanged status:%s", response.status);

			switch (response.status)
			{
			case LOADING:
				loading.set(true);
				break;
			case ERROR:
				List<CalendarItemViewModel> viewModels = Collections.singletonList(
					new CalendarItemViewModel(getApplication().getString(R.string.calendar_error_loading)));
				itemViewModels.set(viewModels);
				loading.set(false);
				break;
			case SUCCESS:
				events = createEvents(response.data);
				loading.set(false);
				break;
			}

			lastStatus = response.status;
		}

		return events;
	}
}
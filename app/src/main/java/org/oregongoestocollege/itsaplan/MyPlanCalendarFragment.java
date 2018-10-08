package org.oregongoestocollege.itsaplan;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.oregongoestocollege.itsaplan.compactcalendarview.CompactCalendarView;
import org.oregongoestocollege.itsaplan.compactcalendarview.domain.Event;
import org.oregongoestocollege.itsaplan.data.CalendarEvent;
import org.oregongoestocollege.itsaplan.data.MyPlanRepository;
import org.oregongoestocollege.itsaplan.data.ResponseData;
import org.oregongoestocollege.itsaplan.databinding.FragmentMyPlanCalendarBinding;
import org.oregongoestocollege.itsaplan.viewmodel.CalendarViewModel;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class MyPlanCalendarFragment extends Fragment implements CompactCalendarView.CompactCalendarViewListener
{
	private static final String LOG_TAG = "GearUp_CalendarFrag";
	private CalendarViewModel viewModel;
	private CompactCalendarView calendarView;

	public MyPlanCalendarFragment()
	{
		// Required empty public constructor
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
		@Nullable Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		FragmentMyPlanCalendarBinding binding =
			DataBindingUtil.inflate(inflater, R.layout.fragment_my_plan_calendar, container, false);
		View v = binding.getRoot();

		Utils.d(LOG_TAG, "onCreateView");

		// set first day to sunday and hookup listeners
		calendarView = v.findViewById(R.id.compactcalendar_view);
		calendarView.setFirstDayOfWeek(Calendar.SUNDAY);
		calendarView.setUseThreeLetterAbbreviation(true);
		calendarView.setListener(this);

		viewModel = ViewModelProviders.of(this).get(CalendarViewModel.class);

		// listen for data changes
		MyPlanRepository myPlanRepository = MyPlanRepository.getInstance(getContext());
		myPlanRepository.getCalendarEvents().observe(this, this::onCalendarEventsChanged);

		// our VM stores the last selected date for rotation, etc. check it
		Date selectedDate = viewModel.getSelectedDate();
		if (selectedDate != null)
			calendarView.setCurrentDate(selectedDate);
		else
			viewModel.setCalendarTitle(calendarView.getFirstDayOfCurrentMonth());

		// bind to fragment to show list of events and calendar title
		binding.setViewModel(viewModel);

		return v;
	}

	void onCalendarEventsChanged(ResponseData<List<CalendarEvent>> data)
	{
		Utils.d(LOG_TAG, "onCalendarEventsChanged");

		if (viewModel != null)
		{
			// determine if there are any events for us to add to our calendar view
			List<Event> events = viewModel.onDataChanged(data);
			if (events != null)
			{
				calendarView.removeAllEvents();

				if (!events.isEmpty())
				{
					calendarView.addEvents(events);

					Date currentDate = calendarView.getCurrentDate();
					if (currentDate != null)
					{
						List<Event> currentEvents = calendarView.getEvents(currentDate);
						viewModel.dateSelected(currentDate, currentEvents);
					}
				}
			}
		}
	}

	@Override
	public void onResume()
	{
		super.onResume();

		viewModel.start();
	}

	@Override
	public void onDayClick(Date dateClicked)
	{
		if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED) && calendarView != null &&
			viewModel != null)
		{
			List<Event> events = calendarView.getEvents(dateClicked);
			viewModel.dateSelected(dateClicked, events);
		}
	}

	@Override
	public void onMonthScroll(Date firstDayOfNewMonth)
	{
		if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED) && calendarView != null &&
			viewModel != null)
		{
			viewModel.setCalendarTitle(firstDayOfNewMonth);

			List<Event> events = calendarView.getEvents(firstDayOfNewMonth);
			viewModel.dateSelected(firstDayOfNewMonth, events);
		}
	}
}

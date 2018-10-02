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
		FragmentMyPlanCalendarBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_plan_calendar, container, false);
		View v = binding.getRoot();

		Utils.d(LOG_TAG, "onCreateView");

		// set to sunday, default is monday
		calendarView = v.findViewById(R.id.compactcalendar_view);
		calendarView.setFirstDayOfWeek(Calendar.SUNDAY);
		calendarView.setUseThreeLetterAbbreviation(true);

		viewModel = ViewModelProviders.of(this).get(CalendarViewModel.class);
		viewModel.setCalendarTitle(calendarView.getFirstDayOfCurrentMonth());

//		int color = ContextCompat.getColor(v.getContext(), R.color.colorAccent);
//
//		Calendar calendar = Calendar.getInstance();
//		calendar.add(Calendar.MONTH, 1);
//
//		calendarView.addEvent(new Event(color, calendar.getTimeInMillis()));
//		calendarView.addEvent(new Event(color, calendar.getTimeInMillis()));
//		calendarView.addEvent(new Event(color, calendar.getTimeInMillis()));
//		calendarView.addEvent(new Event(color, calendar.getTimeInMillis()));

		// hookup listener for date events
		calendarView.setListener(this);

		// bind to fragment to show list of events
		binding.setViewModel(viewModel);

		return v;
	}

	@Override
	public void onDayClick(Date dateClicked)
	{
		if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED) && calendarView != null &&
			viewModel != null)
		{
			List<Event> events = calendarView.getEvents(dateClicked);
			viewModel.setEventViewModels(events);
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
			viewModel.setEventViewModels(events);
		}
	}
}

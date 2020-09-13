package org.oregongoestocollege.itsaplan.data;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.oregongoestocollege.itsaplan.NotificationJobService;
import org.oregongoestocollege.itsaplan.R;
import org.oregongoestocollege.itsaplan.Utils;

/**
 * Oregon GEAR UP App
 * Copyright © 2020 Oregon GEAR UP. All rights reserved.
 */
class MyPlanTasks
{
	static class CalendarEventsTask extends AsyncTask<Void, Void, ResponseData<List<CalendarEvent>>>
	{
		private WeakReference<Context> contextWeakReference;
		private List<CalendarEventData> dataFromNetwork;
		private ResponseData<List<CalendarEvent>> responseData;

		CalendarEventsTask(@NonNull Context context, List<CalendarEventData> dataFromNetwork)
		{
			this.contextWeakReference = new WeakReference<>(context);
			this.dataFromNetwork = dataFromNetwork;
		}

		@Override
		protected ResponseData<List<CalendarEvent>> doInBackground(Void... voids)
		{
			boolean firstLoad = false;

			// see if we need to get the data from the network, if we do, store the
			// raw data so we can populate it with the latest user entered data
			if (dataFromNetwork == null)
			{
				try
				{
					URL url = new URL(Utils.BASE_URL + "calendar.json");
					HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();

					InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));

					Type listType = new TypeToken<ArrayList<CalendarEventData>>()
					{
					}.getType();
					dataFromNetwork = new Gson().fromJson(bufferedReader, listType);

					urlConnection.disconnect();

					firstLoad = true;
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}

			// track whether we got the calendar events so we can show a warning
			boolean gotNetworkData = dataFromNetwork != null && !dataFromNetwork.isEmpty();

			// build up all of our calendar events
			final Context context = contextWeakReference.get();
			List<CalendarEvent> events = buildCalendarEvents(context, dataFromNetwork, false);

			// if the network call failed than set the response as an error so the user
			// can be warned that not all events have been loaded...
			if (gotNetworkData)
			{
				responseData = ResponseData.success(events);

				// schedule notifications for all events only on first load
				if (firstLoad)
					NotificationJobService.scheduleNotifications(context, events);
			}
			else
				responseData = ResponseData.error(context.getString(R.string.calendar_error_loading), events);

			return responseData;
		}

		@Override
		protected void onPostExecute(ResponseData<List<CalendarEvent>> listResponseData)
		{
			super.onPostExecute(listResponseData);

			MyPlanRepository.getInstance(contextWeakReference.get()).loadCalendarEventsCompleted(responseData);
		}

		List<CalendarEventData> getRawData()
		{
			return dataFromNetwork;
		}
	}

	static class CalendarSchedulerTask extends AsyncTask<Void, Void, Boolean>
	{
		private WeakReference<Context> contextWeakReference;
		private List<CalendarEventData> dataFromNetwork;

		CalendarSchedulerTask(@NonNull Context context, @Nullable List<CalendarEventData> dataFromNetwork)
		{
			this.contextWeakReference = new WeakReference<>(context);
			this.dataFromNetwork = dataFromNetwork;
		}

		@Override
		protected Boolean doInBackground(Void... voids)
		{
			// build up all of our calendar events
			final Context context = contextWeakReference.get();
			List<CalendarEvent> events = buildCalendarEvents(context, dataFromNetwork, true);

			// schedule them so user gets notifications
			NotificationJobService.scheduleNotifications(context, events);

			return true;
		}
	}

	static class CalendarSchedulerUpdateTask extends AsyncTask<Void, Void, Boolean>
	{
		private WeakReference<Context> contextWeakReference;
		private List<CalendarEventData> dataFromNetwork;
		private NotificationInfo notificationInfo;

		CalendarSchedulerUpdateTask(@NonNull Context context, @Nullable List<CalendarEventData> dataFromNetwork,
			@NonNull NotificationInfo notificationInfo)
		{
			this.contextWeakReference = new WeakReference<>(context);
			this.dataFromNetwork = dataFromNetwork;
			this.notificationInfo = notificationInfo;
		}

		@Override
		protected Boolean doInBackground(Void... voids)
		{
			CalendarEvent calendarEvent = null;

			// see if our notification info is a match for any of the calendar data
			for (CalendarEventData data : dataFromNetwork)
			{
				if (data.hasReminderId() && data.hasDateOrDescriptionKey(notificationInfo))
				{
					final Context context = contextWeakReference.get();
					calendarEvent = CalendarEvent.from(context, data, new UserEntries(context));
					break;
				}
			}

			if (calendarEvent != null && calendarEvent.hasReminderInfo())
			{
				final Context context = contextWeakReference.get();

				// schedule / update so user gets notification
				NotificationJobService.scheduleNotification(context, calendarEvent);
			}

			return true;
		}
	}

	private static List<CalendarEvent> buildCalendarEvents(@NonNull Context context,
		@Nullable List<CalendarEventData> list, boolean forNotificationOnly)
	{
		CalendarEvent event;
		List<CalendarEvent> events = new ArrayList<>();

		// add events we pulled from the network
		if (list != null)
		{
			UserEntriesInterface userEntries = new UserEntries(context);

			for (CalendarEventData data : list)
			{
				// if we are building for display in the UX calendar we want all events
				// if we are building for scheduling notifications we only want events with id
				if (!forNotificationOnly || data.hasReminderId())
				{
					// add event with substitution of user entered data
					event = CalendarEvent.from(context, data, userEntries);
					if (event != null)
						events.add(event);
				}
			}
		}

		MyPlanRepository repository = MyPlanRepository.getInstance(context);

		// add college application deadlines
		College[] colleges = repository.collegeDao.getAllDirect();
		if (colleges != null)
		{
			for (int i = 0; i < colleges.length; i++)
			{
				event = CalendarEvent.from(colleges[i], i);
				if (event != null)
					events.add(event);
			}
		}

		// add scholarship application deadlines
		Scholarship[] scholarships = repository.scholarshipDao.getAllDirect();
		if (scholarships != null)
		{
			for (int i = 0; i < scholarships.length; i++)
			{
				event = CalendarEvent.from(scholarships[i], i);
				if (event != null)
					events.add(event);
			}
		}

		// add test dates
		TestResult[] testResults = repository.testResultDao.getAllDirect();
		if (testResults != null)
		{
			for (TestResult testResult : testResults)
			{
				event = CalendarEvent.from(testResult);
				if (event != null)
					events.add(event);
			}
		}

		return events;
	}
}

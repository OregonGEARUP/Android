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
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.oregongoestocollege.itsaplan.NotificationJobService;
import org.oregongoestocollege.itsaplan.R;
import org.oregongoestocollege.itsaplan.Utils;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
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

			CalendarEvent event;
			List<CalendarEvent> events = new ArrayList<>();
			final Context context = contextWeakReference.get();

			// track whether we got the calendar events so we can show a warning
			boolean gotNetworkData = dataFromNetwork != null && !dataFromNetwork.isEmpty();
			if (gotNetworkData)
			{
				UserEntriesInterface userEntries = new UserEntries(context);

				// add events with substitutions of user entered data
				for (CalendarEventData data : dataFromNetwork)
				{
					event = CalendarEvent.from(context, data, userEntries);
					if (event != null)
						events.add(event);
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
}

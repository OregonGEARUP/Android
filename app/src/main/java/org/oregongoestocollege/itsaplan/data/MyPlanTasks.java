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
import org.oregongoestocollege.itsaplan.Utils;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
class MyPlanTasks
{
	static class CalendarEventsTask extends AsyncTask<Void, Void, ResponseData<List<CalendarEvent>>>
	{
		// todo - find right place to store this, could put in DB but since CalendarEventData has fields of
		// List<String> docs suggest storing as JSON using converter so maybe just save the response to a file
		private static List<CalendarEventData> dataFromNetwork;
		private WeakReference<Context> contextWeakReference;
		private ResponseData<List<CalendarEvent>> responseData;

		CalendarEventsTask(@NonNull Context context)
		{
			this.contextWeakReference = new WeakReference<>(context);
		}

		@Override
		protected ResponseData<List<CalendarEvent>> doInBackground(Void... voids)
		{
			try
			{
				// see if we need to get the data from the network
				if (dataFromNetwork == null)
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
				}

				List<CalendarEvent> events = null;
				if (dataFromNetwork != null && !dataFromNetwork.isEmpty())
				{
					UserEntriesInterface userEntries = new UserEntries(contextWeakReference.get());

					// build event list with substitutions of user entered data
					events = new ArrayList<>();

					for (CalendarEventData data : dataFromNetwork)
					{
						CalendarEvent event = CalendarEvent.from(contextWeakReference.get(), data, userEntries);
						if (event != null)
							events.add(event);
					}
				}

				// if we have a list of events than we successfully retrieved them from the network
				// so we should at least have one event...
				if (events != null && !events.isEmpty())
					responseData = ResponseData.success(events);
				else
					responseData = ResponseData.error("No calendar data found on server.", null);
			}
			catch (IOException e)
			{
				responseData = ResponseData.error("There was a problem loading the calendar information.", null);

				e.printStackTrace();
			}

			return responseData;
		}

		@Override
		protected void onPostExecute(ResponseData<List<CalendarEvent>> listResponseData)
		{
			super.onPostExecute(listResponseData);

			MyPlanRepository.getInstance(contextWeakReference.get()).loadCalendarEventsCompleted(responseData);
		}
	}
}

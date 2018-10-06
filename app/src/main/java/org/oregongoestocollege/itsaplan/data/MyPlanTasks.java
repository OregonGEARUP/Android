package org.oregongoestocollege.itsaplan.data;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
		private MyPlanRepository myPlanRepository;
		private ResponseData<List<CalendarEvent>> responseData;

		CalendarEventsTask(@NonNull MyPlanRepository myPlanRepo)
		{
			this.myPlanRepository = myPlanRepo;
		}

		@Override
		protected ResponseData<List<CalendarEvent>> doInBackground(Void... voids)
		{
			try
			{
				// load the block info
				URL url = new URL(Utils.BASE_URL + "calendar.json");
				HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();

				InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));

				Type listType = new TypeToken<ArrayList<CalendarEvent>>()
				{
				}.getType();
				List<CalendarEvent> events = new Gson().fromJson(bufferedReader, listType);

				urlConnection.disconnect();

				responseData = ResponseData.success(events);
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

			myPlanRepository.loadCalendarEventsCompleted(responseData);
		}
	}
}

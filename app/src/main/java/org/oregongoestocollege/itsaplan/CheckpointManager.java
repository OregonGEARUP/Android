package org.oregongoestocollege.itsaplan;

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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.oregongoestocollege.itsaplan.data.BlockInfo;

/**
 * CheckpointManager.java
 * This class is a singleton used to retrieve and store checkpoint data.
 *
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
class CheckpointManager
{
	private static final String baseUrl = "https://oregongoestocollege.org/mobileApp/json/";
	private static CheckpointManager instance;

	/**
	 * @return a shared instance of the Manager
	 */
	static CheckpointManager getInstance()
	{
		if (instance == null)
			instance = new CheckpointManager();
		return instance;
	}

	void init(ChecklistFragment fragment)
	{
		// TODO HACK for now - implement observer for data changes
		new GetBlockInfoTask(fragment).execute();
	}

	private static class GetBlockInfoTask extends AsyncTask<Void, Void, List<BlockInfo>>
	{
		ChecklistFragment fragment;
		List<BlockInfo> blocks;

		GetBlockInfoTask(ChecklistFragment fragment)
		{
			this.fragment = fragment;
		}

		protected List<BlockInfo> doInBackground(Void... params)
		{
			try
			{
				URL url = new URL( baseUrl + "blocks.json");
				HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();

				InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));

				Type listType = new TypeToken<ArrayList<BlockInfo>>(){}.getType();
				blocks = new Gson().fromJson(bufferedReader, listType);

				urlConnection.disconnect();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			return blocks;
		}

		protected void onPostExecute(List<BlockInfo> blocks)
		{
			fragment.dataAvailable(blocks);
			fragment = null;
		}
	}
}

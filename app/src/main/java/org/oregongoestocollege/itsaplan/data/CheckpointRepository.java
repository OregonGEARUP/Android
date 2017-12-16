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
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.oregongoestocollege.itsaplan.support.Utils;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This class is a singleton used to retrieve and store checkpoint data.
 * Similar to the iOS CheckpointManager class.
 *
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class CheckpointRepository implements CheckpointInterface
{
	private final String TAG = "CheckpointManager";
	private static final String baseUrl = "https://oregongoestocollege.org/mobileApp/json/";
	private static CheckpointRepository instance;
	// cached data from a previous network request
	protected List<BlockInfo> cachedBlockInfoList;

	/**
	 * @return a shared instance of the Manager
	 */
	public static CheckpointRepository getInstance()
	{
		if (instance == null)
			instance = new CheckpointRepository();
		return instance;
	}

	@Override
	public void getBlockInfoList(@NonNull LoadBlockInfoListCallback callback)
	{
		checkNotNull(callback);

		// response immediately if we have the data cached
		if (cachedBlockInfoList != null)
		{
			Log.d(TAG, "BlockInfoList from cache");
			callback.onDataLoaded(cachedBlockInfoList);
			return;
		}

		Log.d(TAG, "BlockInfoList from network");

		// if not make the http request
		new GetBlockInfoListTask(callback).execute();
	}

	@Override
	public void getBlocks(@NonNull LoadBlocksCallback callback, String blockFileName)
	{
		checkNotNull(callback);
		checkNotNull(blockFileName);

		if (Utils.DEBUG)
			Utils.d(TAG, "Blocks %s from network", blockFileName);

		// if not make the http request
		new GetBlocksTask(callback, blockFileName).execute();
	}

	private static class GetBlockInfoListTask extends AsyncTask<Void, Void, List<BlockInfo>>
	{
		LoadBlockInfoListCallback callback;
		List<BlockInfo> blocks;

		GetBlockInfoListTask(@NonNull LoadBlockInfoListCallback callback)
		{
			this.callback = callback;
		}

		protected List<BlockInfo> doInBackground(Void... params)
		{
			try
			{
				URL url = new URL(baseUrl + "blocks.json");
				HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();

				InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));

				Type listType = new TypeToken<ArrayList<BlockInfo>>()
				{
				}.getType();
				blocks = new Gson().fromJson(bufferedReader, listType);

				urlConnection.disconnect();

				CheckpointRepository.getInstance().cachedBlockInfoList = blocks;
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			return blocks;
		}

		protected void onPostExecute(List<BlockInfo> blocks)
		{
			if (blocks != null && !blocks.isEmpty())
				callback.onDataLoaded(blocks);
			else
				callback.onDataNotAvailable();
		}
	}

	private static class GetBlocksTask extends AsyncTask<Void, Void, List<Block>>
	{
		LoadBlocksCallback callback;
		String blockFileName;
		List<Block> blocks;

		GetBlocksTask(@NonNull LoadBlocksCallback callback, @NonNull String blockFileName)
		{
			this.callback = callback;
			this.blockFileName = blockFileName;
		}

		protected List<Block> doInBackground(Void... params)
		{
			try
			{
				URL url = new URL(baseUrl + blockFileName);
				HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();

				InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));

				Type listType = new TypeToken<ArrayList<Block>>()
				{
				}.getType();
				blocks = new Gson().fromJson(bufferedReader, listType);

				urlConnection.disconnect();

			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			return blocks;
		}

		protected void onPostExecute(List<Block> blocks)
		{
			if (blocks != null && !blocks.isEmpty())
				callback.onDataLoaded(blocks);
			else
				callback.onDataNotAvailable();
		}
	}
}

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.TextUtils;

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
	private final String TAG = "GearUP_CheckpointMgr";
	private static final String baseUrl = "https://oregongoestocollege.org/mobileApp/json/";
	private static CheckpointRepository instance;
	// BlockInfo fields
	final Object blockInfoListLock = new Object();
	List<BlockInfo> blockInfoListCache;
	GetBlockInfoListTask blockInfoListTask;
	// Block fields
	final Object blocksLock = new Object();
	Map<Integer, Block> blocksCache = new HashMap<>();
	Map<Integer, GetBlockTask> blocksTask = new HashMap<>();

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

		List<BlockInfo> data = null;
		GetBlockInfoListTask newTask = null;

		synchronized (blockInfoListLock)
		{
			if (blockInfoListCache != null)
				data = blockInfoListCache;
			else if (blockInfoListTask != null)
				blockInfoListTask.setCallback(callback);
			else
			{
				newTask = new GetBlockInfoListTask(callback);
				blockInfoListTask = newTask;
			}
		}

		// response immediately if we have the data cached
		if (data != null)
		{
			Utils.d(TAG, "BlockInfoList from cache");
			callback.onDataLoaded(blockInfoListCache);
		}
		else if (newTask != null)
		{
			Utils.d(TAG, "BlockInfoList from network");
			newTask.execute();
		}
		else
			Utils.d(TAG, "GetBlockInfoListTask pending");
	}

	@Override
	public void getBlock(@NonNull LoadBlockCallback callback, int blockIndex)
	{
		checkNotNull(callback);

		// we must have our BlockInfo before we can load a block
		BlockInfo blockInfo = null;
		if (blockInfoListCache != null && blockIndex < blockInfoListCache.size())
			blockInfo = blockInfoListCache.get(blockIndex);

		if (blockInfo == null || TextUtils.isEmpty(blockInfo.blockFileName))
			return;

		Block data;
		GetBlockTask newTask = null;

		synchronized (blocksLock)
		{
			data = blocksCache.get(blockIndex);
			if (data == null)
			{
				GetBlockTask currentTask = blocksTask.get(blockIndex);
				if (currentTask != null)
					currentTask.setCallback(callback);
				else
				{
					newTask = new GetBlockTask(callback, blockInfo.blockFileName, blockIndex);
					blocksTask.put(blockIndex, newTask);
				}

			}
		}

		// response immediately if we have the data cached
		if (data != null)
		{
			Utils.d(TAG, "Block from cache");
			callback.onDataLoaded(data);
		}
		else if (newTask != null)
		{
			Utils.d(TAG, "Block from network");
			newTask.execute();
		}
		else
			Utils.d(TAG, "GetBlockTask pending");
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
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			return blocks;
		}

		protected void onPostExecute(List<BlockInfo> blocks)
		{
			CheckpointRepository repo = CheckpointRepository.getInstance();

			boolean hasData;

			synchronized (repo.blockInfoListLock)
			{
				hasData = blocks != null && !blocks.isEmpty();
				repo.blockInfoListCache = blocks;
				repo.blockInfoListTask = null;
			}

			if (hasData)
				callback.onDataLoaded(repo.blockInfoListCache);
			else
				callback.onDataNotAvailable();
		}

		public void setCallback(LoadBlockInfoListCallback callback)
		{
			this.callback = callback;
		}
	}

	private static class GetBlockTask extends AsyncTask<Void, Void, Block>
	{
		LoadBlockCallback callback;
		String blockFileName;
		int blockIndex;
		Block block;

		GetBlockTask(@NonNull LoadBlockCallback callback, @NonNull String blockFileName, int blockIndex)
		{
			this.callback = callback;
			this.blockFileName = blockFileName;
			this.blockIndex = blockIndex;
		}

		protected Block doInBackground(Void... params)
		{
			try
			{
				URL url = new URL(baseUrl + blockFileName);
				HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();

				InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));

				Type listType = new TypeToken<List<Block>>()
				{
				}.getType();
				List<Block> blocks = new Gson().fromJson(bufferedReader, listType);
				if (blocks != null)
					block = blocks.get(0);

				urlConnection.disconnect();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			return block;
		}

		protected void onPostExecute(Block block)
		{
			CheckpointRepository repo = CheckpointRepository.getInstance();

			synchronized (repo.blocksLock)
			{
				if (block != null)
					repo.blocksCache.put(blockIndex, block);
				repo.blocksTask.remove(this);
			}

			if (block != null)
				callback.onDataLoaded(block);
			else
				callback.onDataNotAvailable();
		}

		public void setCallback(LoadBlockCallback callback)
		{
			this.callback = callback;
		}
	}

	private static void simulateNetworkDelay()
	{
		try
		{
			Thread.sleep(5000L);
		}
		catch (InterruptedException e)
		{
			Thread.currentThread().interrupt();
		}
	}
}

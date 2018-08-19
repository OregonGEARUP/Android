package org.oregongoestocollege.itsaplan.data;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.oregongoestocollege.itsaplan.Utils;

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
	private final static String LOG_TAG = "GearUp_CheckpointRepo";
	private static final String baseUrl = "https://oregongoestocollege.org/mobileApp/json/";
	private static CheckpointRepository instance;
	private final List<String> traces = new ArrayList<>();
	// pending Tasks
	GetBlockInfoTask currentBlockInfoTask;
	final Map<String, GetBlockTask> currentBlockTasks = new HashMap<>();
	// sync access to cached data
	private final Object lock = new Object();
	// cached BlockInfo list
	private List<BlockInfo> cachedBlockInfos;
	private MutableLiveData<Boolean> blockInfosLoading = new MutableLiveData<>();
	// cached Block(s)
	private final Map<String, Block> cachedBlocks = new HashMap<>();
	// current state
	private String currentBlockFileName;
	private int currentBlockIndex = 0;
	private int currentStageIndex = 0;
	private int currentCheckpointIndex = 0;
	private final Set<String> visited = new HashSet<>();

	/**
	 * @return a shared instance of the Manager
	 */
	public static CheckpointRepository getInstance(@NonNull Context context)
	{
		if (instance == null)
		{
			instance = new CheckpointRepository(context);
		}
		return instance;
	}

	private CheckpointRepository(@NonNull Context context)
	{
		UserEntriesInterface userEntries = new UserEntries(context);
		ChecklistState currentState = userEntries.getChecklistState();
		currentBlockFileName = currentState.blockFileName;
		currentBlockIndex = currentState.blockIndex;
		currentStageIndex = currentState.stageIndex;
		currentCheckpointIndex = currentState.checkpointIndex;
	}

	@Override
	public LiveData<Boolean> blockInfoListLoading()
	{
		return blockInfosLoading;
	}

	@Override
	public void loadBlockInfoList(@NonNull MyPlanRepository myPlanRepo)
	{
		checkNotNull(myPlanRepo);

		// setup a background task or hookup to our existing one
		GetBlockInfoTask newTask = null;
		synchronized (lock)
		{
			if (currentBlockInfoTask == null)
			{
				newTask = new GetBlockInfoTask(this, myPlanRepo);
				currentBlockInfoTask = newTask;
			}
			else
				Utils.d(LOG_TAG, "loadBlockInfoList pending");
		}

		// if we didn't have a task executing, do it now
		blockInfosLoading.setValue(true);

		if (newTask != null)
			newTask.execute();
	}

	@Override
	public void loadBlock(@NonNull MyPlanRepository myPlanRepo,
		@NonNull CheckpointCallback callback, int blockIndex,
		String blockFileName)
	{
		checkNotNull(callback);

		// we must have our BlockInfo before we can load a block
		BlockInfo blockInfo = null;
		if (cachedBlockInfos != null && blockIndex >= 0 && blockIndex < cachedBlockInfos.size())
			blockInfo = cachedBlockInfos.get(blockIndex);

		// load either saved blockFileName or first from the BlockInfo
		String fileName = blockInfo != null ? blockInfo.getBlockFileName() : null;
		// or we could be loading a new block
		if (TextUtils.isEmpty(fileName))
			fileName = blockFileName;

		if (blockInfo == null || TextUtils.isEmpty(fileName))
		{
			Utils.d(LOG_TAG, "Missing information for loadBlock().");
			callback.onDataLoaded(false);
			return;
		}

		Block data;
		GetBlockTask newTask = null;

		synchronized (lock)
		{
			data = cachedBlocks.get(fileName);
			if (data == null)
			{
				GetBlockTask currentTask = currentBlockTasks.get(fileName);
				if (currentTask != null)
					currentTask.setCallback(callback);
				else
					newTask = new GetBlockTask(this, myPlanRepo, callback, fileName, blockIndex);
			}
		}

		// response immediately if we have the data cached
		if (data != null)
		{
			Utils.d(LOG_TAG, "Block from cache");

			currentBlockFileName = fileName;
			currentBlockIndex = blockIndex;
			currentStageIndex = Utils.NO_INDEX;
			currentCheckpointIndex = Utils.NO_INDEX;

			callback.onDataLoaded(true);
		}
		else if (newTask != null)
		{
			Utils.d(LOG_TAG, "Block from network");
			newTask.execute();
		}
		else
			Utils.d(LOG_TAG, "GetBlockTask pending");
	}

	private static class GetBlockInfoTask extends AsyncTask<Void, Void, Boolean>
	{
		CheckpointRepository repository;
		MyPlanRepository myPlanRepo;
		List<BlockInfo> blocks;

		GetBlockInfoTask(@NonNull CheckpointRepository repository, @NonNull MyPlanRepository myPlanRepo)
		{
			this.repository = repository;
			this.myPlanRepo = myPlanRepo;
		}

		protected Boolean doInBackground(Void... params)
		{
			try
			{
				// determine if I already have data
				BlockInfo [] dbList = myPlanRepo.blockInfoDao.getAllDirect();

				if (dbList == null || dbList.length < 1)
				{
					Utils.d(CheckpointRepository.LOG_TAG, "BlockInfos from network");

					// load the block info
					URL url = new URL(baseUrl + "blocks.json");
					HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();

					InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));

					Type listType = new TypeToken<ArrayList<BlockInfo>>()
					{
					}.getType();
					blocks = new Gson().fromJson(bufferedReader, listType);

					urlConnection.disconnect();

					if (blocks != null && !blocks.isEmpty())
						myPlanRepo.insertBlockInfos(blocks);

					// for now cache a local copy
					repository.cachedBlockInfos = blocks;
				}
				else if (repository.cachedBlockInfos == null || repository.cachedBlockInfos.isEmpty())
				{
					repository.cachedBlockInfos = Arrays.asList(dbList);

					Utils.d(CheckpointRepository.LOG_TAG, "BlockInfos from database");
				}
				else
					Utils.d(CheckpointRepository.LOG_TAG, "BlockInfos from cache");
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			return blocks != null && !blocks.isEmpty();
		}

		protected void onPostExecute(Boolean success)
		{
			// clear this task
			repository.blockInfosLoading.setValue(false);
			repository.currentBlockInfoTask = null;
		}
	}

	private static class GetBlockTask extends AsyncTask<Void, Void, Block>
	{
		CheckpointRepository repository;
		MyPlanRepository myPlanRepo;
		final String blockFileName;
		final int blockIndex;
		CheckpointCallback callback;
		Block block;

		GetBlockTask(@NonNull CheckpointRepository repository, @NonNull MyPlanRepository myPlanRepo,
			@NonNull CheckpointCallback callback,
			@NonNull String blockFileName,
			int blockIndex)
		{
			this.repository = repository;
			this.myPlanRepo = myPlanRepo;
			this.callback = callback;
			this.blockFileName = blockFileName;
			this.blockIndex = blockIndex;

			// keep track of the pending task
			repository.currentBlockTasks.put(blockFileName, this);
		}

		protected Block doInBackground(Void... params)
		{
			block = repository.fetchBlock(blockFileName, blockIndex, myPlanRepo);
			return block;
		}

		protected void onPostExecute(Block block)
		{
			// clear this task
			repository.currentBlockTasks.remove(blockFileName);

			callback.onDataLoaded(block != null);
		}

		/**
		 * This allows us to hook into an existing network call to show loading status
		 */
		public void setCallback(CheckpointCallback callback)
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

	Block fetchBlock(String blockFileName, int blockIndex, @NonNull MyPlanRepository myPlanRepository)
	{
		Block block = null;

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

			urlConnection.disconnect();

			if (blocks != null)
			{
				block = blocks.get(0);
				block.addNextStageCheckpoint();
			}

			if (block != null && block.stages != null && block.stages.size() > 0)
			{
				BlockInfo blockInfo;

				// update the blockIndex for the newly loaded block
				synchronized (lock)
				{
					// store the file name once loaded successfully
					blockInfo = cachedBlockInfos.get(blockIndex);
					blockInfo.setBlockFileName(blockFileName);

					currentBlockFileName = blockFileName;
					currentBlockIndex = blockIndex;
					currentStageIndex = Utils.NO_INDEX;
					currentCheckpointIndex = Utils.NO_INDEX;

					cachedBlocks.put(blockFileName, block);

					if (blockInfo.getIds() != null && blockInfo.getIds().contains(block.id))
					{
						Pair<Integer, Integer> status = blockStagesStatus(block);
						blockInfo.setStagesComplete(status.first);
						blockInfo.setStageCount(status.second);
					}
				}

				// persist changes to the BlockInfo
				myPlanRepository.update(blockInfo);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return block;
	}

	private Pair<Integer, Integer> blockStagesStatus(@NonNull Block block)
	{
		Integer completed = 0;
		Integer total = 0;

		List<Stage> stages = block.stages;
		if (stages != null)
		{
			for (int i = 0; i < stages.size(); i++)
			{
				completed += stageCompleted(i) ? 1 : 0;
				total += 1;
			}
		}

		return new Pair<>(completed, total);
	}

	public boolean stageCompleted(int stageIndex)
	{
		boolean completed = true;

		Block block = getBlock(currentBlockIndex);
		if (block != null)
		{
			List<Stage> stages = block.stages;
			if (stages != null && stageIndex >= 0 && stageIndex < stages.size())
			{
				Stage stage = stages.get(stageIndex);
				if (stage != null)
				{
					List<Checkpoint> checkpoints = stage.checkpoints;
					if (checkpoints != null)
					{
						for (int i = 0; i < checkpoints.size(); i++)
						{
							completed = completed && checkpointCompleted(i, stageIndex, stage);

							// check for completed route cp at the end of the stage, as soon as we find one visited
							// route cp then we are good for the stage (this assumes that route cps are always at the end of a stage)
							if (completed && checkpoints.get(i).entryType == EntryType.route)
								break;
						}
					}
				}
			}
		}

		return completed;
	}

//	private boolean stageCompleted(@NonNull Stage stage)
//	{
//		boolean completed = true;
//
//		List<Checkpoint> checkpoints = stage.checkpoints;
//		if (checkpoints != null)
//		{
//			for (int i = 0; i < checkpoints.size(); i++)
//			{
//				completed = completed && checkpointCompleted(i, stageIndex);
//
//				// check for completed route cp at the end of the stage, as soon as we find one visited
//				// route cp then we are good for the stage (this assumes that route cps are always at the end of a stage)
//				if (completed && currentBlock.stages.get(stageIndex).checkpoints.get(i).entryType == EntryType.info)
//					break;
//			}
//		}
//
//		return completed;
//	}

//	private boolean checkpointCompleted(int checkpointIndex, int stageIndex)
//	{
//		boolean completed = hasVisited(0, stageIndex, checkpointIndex);
//		if (!completed)
//			return false;
//
//		// check to see if checkpoint is completed
//		Checkpoint cp = currentBlock.stages.get(stageIndex).checkpoints.get(checkpointIndex);
//		return cp.isCompleted(0, stageIndex, checkpointIndex);
//	}

	private boolean checkpointCompleted(int checkpointIndex, int stageIndex, Stage stage)
	{
		boolean completed = hasVisited(stageIndex, checkpointIndex);
		if (!completed)
			return false;

		// check to see if checkpoint is completed
		Checkpoint cp = stage.checkpoints.get(checkpointIndex);
		return cp.isCompleted(stageIndex, checkpointIndex);
	}

	@Override
	public int getCountOfBlocks()
	{
		return cachedBlockInfos != null ? cachedBlockInfos.size() : 0;
	}

	@Override
	public BlockInfo getBlockInfo(int blockIndex)
	{
		if (cachedBlockInfos != null && blockIndex >= 0 && blockIndex < cachedBlockInfos.size())
			return cachedBlockInfos.get(blockIndex);

		return null;
	}

	public void persistBlockCompletionInfo(int blockIndex, MyPlanRepository myPlanRepository)
	{
		boolean dirty = false;
		Block block = getBlock(blockIndex);
		if (block != null)
		{
			Pair<Integer, Integer> status = blockStagesStatus(block);
			BlockInfo blockInfo = cachedBlockInfos.get(blockIndex);

			if (status.first != blockInfo.getStagesComplete())
			{
				blockInfo.setStagesComplete(status.first);
				dirty = true;
			}
			if (status.second != blockInfo.getStageCount())
			{
				blockInfo.setStagesComplete(status.second);
				dirty = true;
			}
			// TODO needed ?
			//cachedBlockInfos.get(blockIndex).blockFileName = blockFileName;

			if (dirty)
				myPlanRepository.update(blockInfo);
		}

		Utils.d(LOG_TAG, "persistBlockCompletionInfo blockIndex=%d dirty=%s", blockIndex, dirty);
	}

	@Override
	public LiveData<List<BlockInfo>> getBlockInfoList(@NonNull MyPlanRepository myPlanRepository)
	{
		return myPlanRepository.getAllBlockInfos();
	}


	@Override
	public Block getBlock(int blockIndex)
	{
		if (cachedBlockInfos != null && blockIndex >= 0 && blockIndex < cachedBlockInfos.size())
		{
			BlockInfo blockInfo = cachedBlockInfos.get(blockIndex);
			if (blockInfo != null)
				return cachedBlocks.get(blockInfo.getBlockFileName());
		}

		return null;
	}

	@Override
	public Stage getStage(int blockIndex, int stageIndex)
	{
		Block block = getBlock(blockIndex);
		if (block != null)
		{
			List<Stage> stages = block.stages;
			if (stages != null && stageIndex >= 0 && stageIndex < stages.size())
				return stages.get(stageIndex);
		}
		return null;
	}

	@Override
	public Checkpoint getCheckpoint(int blockIndex, int stageIndex, int checkpointIndex)
	{
		Stage stage = getStage(blockIndex, stageIndex);
		if (stage != null)
		{
			List<Checkpoint> checkpoints = stage.checkpoints;
			if (checkpoints != null && checkpointIndex >= 0 && checkpointIndex < checkpoints.size())
				return checkpoints.get(checkpointIndex);
		}
		return null;
	}

	@Override
	public void markVisited(int stageIndex, int checkpointIndex)
	{
		String key = keyForBlockIndex(stageIndex, checkpointIndex);

		if (Utils.DEBUG)
			Utils.d(LOG_TAG, String.format("visited %s", key));

		visited.add(key);
	}

	public boolean hasVisited(int stageIndex, int checkpointIndex)
	{
		return visited.contains(keyForBlockIndex(stageIndex, checkpointIndex));
	}

	public String keyForBlockIndex(int stageIndex, int checkpointIndex)
	{
		Block block = getBlock(currentBlockIndex);
		Stage stage = block.stages.get(stageIndex);
		Checkpoint cp = stage.checkpoints.get(checkpointIndex);
		return String.format(Locale.US, "%s_%s_%s", block.id, stage.id, cp.id);
	}

	public String keyForBlockIndex(int blockIndex, int stageIndex, int checkpointIndex, int instanceIndex)
	{
		Block block = getBlock(blockIndex);
		Stage stage = block.stages.get(stageIndex);
		Checkpoint cp = stage.checkpoints.get(checkpointIndex);
		Instance inst = cp.instances.get(instanceIndex);
		return String.format(Locale.US, "%s_%s_%s_%s", block.id, stage.id, cp.id, inst.getId());
	}

	@Override
	public void addTrace(String trace)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		String timestamp = dateFormat.format(new Date());
		String message = String.format(Locale.US, "%s:  %s", timestamp, trace);

		traces.add(message);

		Utils.d(LOG_TAG, trace);
	}


	/*
				// determine what our current state is
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
			currentBlockIndex = prefs.getInt("currentBlockIndex", -1);
			currentStageIndex = prefs.getInt("currentStageIndex", -1);
			currentCheckpointIndex = prefs.getInt("currentCheckpointIndex", -1);

			String filename = prefs.getString("currentBlockFilename", null);

			// handle initial startup case with first block
			if (TextUtils.isEmpty(filename) && !blocks.isEmpty())
			{
				currentBlockIndex = -1;
				currentStageIndex = -1;
				currentCheckpointIndex = -1;
				filename = blocks.get(0).blockFileName;
			}

			if (!TextUtils.isEmpty(filename))
			{
				Block block = fetchBlock(context, filename, 0);
				success = block != null;
			}
			else
				Log.e(LOG_TAG, "no block filename in fetchBlocks()");

	 */
}

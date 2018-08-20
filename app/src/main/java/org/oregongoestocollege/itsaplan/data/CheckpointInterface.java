package org.oregongoestocollege.itsaplan.data;

import java.util.List;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

/**
 * Main entry point for accessing data.
 * Oregon GEAR UP App
 *
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public interface CheckpointInterface
{
	interface CheckpointCallback
	{
		void onDataLoaded(boolean success);
	}

	LiveData<Boolean> blockInfoListLoading();

	void loadBlockInfoList(@NonNull MyPlanRepository myPlanRepo);

	void loadBlock(@NonNull MyPlanRepository myPlanRepo, @NonNull CheckpointCallback callback, int blockIndex, String blockFileName);

	int getCountOfBlocks();

	LiveData<List<BlockInfo>> getBlockInfoList(@NonNull MyPlanRepository myPlanRepository);

	BlockInfo getBlockInfo(int blockIndex);

	Block getBlock(int blockIndex);

	Stage getStage(int blockIndex, int stageIndex);

	Checkpoint getCheckpoint(int blockIndex, int stageIndex, int checkpointIndex);

	boolean stageCompleted(int stageIndex);

	String keyForBlockIndex(int stageIndex, int checkpointIndex);
	String keyForBlockIndex(int blockIndex, int stageIndex, int checkpointIndex, int instanceIndex);

	void markVisited(int stageIndex, int checkpointIndex);

	void persistVisited(@NonNull MyPlanRepository myPlanRepo);

	void persistBlockCompletionInfo(int blockIndex, MyPlanRepository myPlanRepository);

	/**
	 * Keeps a list of trace statements that can be used to debug the app in the field.
	 * This should be kept to simple / short messages since we keep them in memory.
	 *
	 * @param trace a simple message that traces the current checklist path
	 */
	void addTrace(String trace);
}

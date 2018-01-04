package org.oregongoestocollege.itsaplan.data;

import android.app.Application;
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

	void resumeCheckpoints(@NonNull Application context, @NonNull CheckpointCallback callback);

	void loadBlock(@NonNull Application context, @NonNull CheckpointCallback callback, int blockIndex, String blockFileName);

	int getCountOfBlocks();

	BlockInfo getBlockInfo(int blockIndex);

	Block getBlock(int blockIndex);

	Stage getStage(int blockIndex, int stageIndex);

	Checkpoint getCheckpoint(int blockIndex, int stageIndex, int checkpointIndex);

	String keyForBlockIndex(int blockIndex, int stageIndex, int checkpointIndex);

	/**
	 * Keeps a list of trace statments that can be used to debug the app in the field.
	 * This should be kept to simple / short messages since we keep them in memory.
	 *
	 * @param trace a simple message that traces the current checklist path
	 */
	void addTrace(String trace);
}

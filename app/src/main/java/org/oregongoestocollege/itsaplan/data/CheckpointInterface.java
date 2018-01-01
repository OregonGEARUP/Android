package org.oregongoestocollege.itsaplan.data;

import java.util.List;

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

	void loadBlock(@NonNull Application context, @NonNull CheckpointCallback callback, int blockIndex);

	List<BlockInfo> getBlockInfo();

	Block getBlock(int blockIndex);
}

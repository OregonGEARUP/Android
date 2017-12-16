package org.oregongoestocollege.itsaplan.data;

import java.util.List;

import android.support.annotation.NonNull;

/**
 * Main entry point for accessing data.
 * Oregon GEAR UP App
 *
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public interface CheckpointInterface
{
	interface LoadBlockInfoListCallback
	{
		void onDataLoaded(@NonNull List<BlockInfo> blockInfoList);

		void onDataNotAvailable();
	}

	interface LoadBlocksCallback
	{
		void onDataLoaded(@NonNull List<Block> blocks);

		void onDataNotAvailable();
	}

	void getBlockInfoList(@NonNull LoadBlockInfoListCallback callback);

	void getBlocks(@NonNull LoadBlocksCallback callback, String blockFileName);
}

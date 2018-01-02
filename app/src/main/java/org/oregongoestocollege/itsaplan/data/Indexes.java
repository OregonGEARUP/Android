package org.oregongoestocollege.itsaplan.data;

import android.support.annotation.Nullable;

/**
 * Holds the indexes needed to find {@link Block}s, {@link Stage}s and {@link Checkpoint}s.
 *
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class Indexes
{
	public static final int NO_INDEX = -1;
	public final int blockIndex;
	public final int stageIndex;
	public final int checkpointIndex;

	public Indexes(int blockIndex)
	{
		this(blockIndex, NO_INDEX, NO_INDEX);
	}

	public Indexes(int blockIndex, int stageIndex)
	{
		this(blockIndex, stageIndex, NO_INDEX);
	}

	public Indexes(int blockIndex, int stageIndex, int checkpointIndex)
	{
		this.blockIndex = blockIndex;
		this.stageIndex = stageIndex;
		this.checkpointIndex = checkpointIndex;
	}

	public static boolean hasBlockIndex(@Nullable Indexes indexes)
	{
		return indexes != null && indexes.blockIndex != NO_INDEX;
	}

	public static boolean hasBlockAndStageIndex(@Nullable Indexes indexes)
	{
		return indexes != null && indexes.blockIndex != NO_INDEX && indexes.stageIndex != NO_INDEX;
	}
}

package org.oregongoestocollege.itsaplan.data;

import android.support.annotation.Nullable;

/**
 * Holds the state needed to load {@link Block}s, {@link Stage}s and {@link Checkpoint}s.
 *
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class ChecklistState
{
	public static final int NO_INDEX = -1;
	public final String blockFileName;
	public final int blockIndex;
	public final int stageIndex;
	public final int checkpointIndex;

	public ChecklistState(int blockIndex)
	{
		this(null, blockIndex, NO_INDEX, NO_INDEX);

	}

	public ChecklistState(String blockFileName, int blockIndex)
	{
		this(blockFileName, blockIndex, NO_INDEX, NO_INDEX);
	}

	public ChecklistState(int blockIndex, int stageIndex)
	{
		this(null, blockIndex, stageIndex, NO_INDEX);
	}

	public ChecklistState(int blockIndex, int stageIndex, int checkpointIndex)
	{
		this(null, blockIndex, stageIndex, checkpointIndex);
	}

	private ChecklistState(String blockFileName, int blockIndex, int stageIndex, int checkpointIndex)
	{
		this.blockFileName = blockFileName;
		this.blockIndex = blockIndex;
		this.stageIndex = stageIndex;
		this.checkpointIndex = checkpointIndex;
	}

	public static boolean hasBlockIndex(@Nullable ChecklistState state)
	{
		return state != null && state.blockIndex != NO_INDEX;
	}

	public static boolean hasBlockAndStageIndex(@Nullable ChecklistState state)
	{
		return state != null && state.blockIndex != NO_INDEX && state.stageIndex != NO_INDEX;
	}
}

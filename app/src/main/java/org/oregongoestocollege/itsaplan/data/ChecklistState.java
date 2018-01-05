package org.oregongoestocollege.itsaplan.data;

import android.support.annotation.Nullable;

import org.oregongoestocollege.itsaplan.Utils;

/**
 * Holds the state needed to load {@link Block}s, {@link Stage}s and {@link Checkpoint}s.
 *
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class ChecklistState
{
	public final String blockFileName;
	public final int blockIndex;
	public final int stageIndex;
	public final int checkpointIndex;

	public ChecklistState(int blockIndex)
	{
		this(null, blockIndex, Utils.NO_INDEX, Utils.NO_INDEX);

	}

	public ChecklistState(String blockFileName, int blockIndex)
	{
		this(blockFileName, blockIndex, Utils.NO_INDEX, Utils.NO_INDEX);
	}

	public ChecklistState(int blockIndex, int stageIndex)
	{
		this(null, blockIndex, stageIndex, Utils.NO_INDEX);
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
		return state != null && state.blockIndex != Utils.NO_INDEX;
	}

	public static boolean hasBlockAndStageIndex(@Nullable ChecklistState state)
	{
		return state != null && state.blockIndex != Utils.NO_INDEX && state.stageIndex != Utils.NO_INDEX;
	}
}

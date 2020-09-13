package org.oregongoestocollege.itsaplan.data;

import android.text.TextUtils;

import org.oregongoestocollege.itsaplan.Utils;

/**
 * Holds the state needed to load {@link Block}s, {@link Stage}s and {@link Checkpoint}s.
 *
 * Oregon GEAR UP App
 * Copyright © 2020 Oregon GEAR UP. All rights reserved.
 */
public class ChecklistState
{
	public final String blockFileName;
	public final int blockIndex;
	public final int stageIndex;
	public final int checkpointIndex;

	public ChecklistState(String blockFileName, int blockIndex)
	{
		this(blockFileName, blockIndex, Utils.NO_INDEX, Utils.NO_INDEX);
	}

	public ChecklistState(String blockFileName, int blockIndex, int stageIndex)
	{
		this(blockFileName, blockIndex, stageIndex, Utils.NO_INDEX);
	}

	public ChecklistState(int blockIndex, int stageIndex)
	{
		this(null, blockIndex, stageIndex, Utils.NO_INDEX);
	}

	public ChecklistState(int blockIndex, int stageIndex, int checkpointIndex)
	{
		this(null, blockIndex, stageIndex, checkpointIndex);
	}

	public ChecklistState(String blockFileName, int blockIndex, int stageIndex, int checkpointIndex)
	{
		this.blockFileName = blockFileName;
		this.blockIndex = blockIndex;
		this.stageIndex = stageIndex;
		this.checkpointIndex = checkpointIndex;
	}

	public boolean hasBlockIndexAndFile()
	{
		return blockIndex >= 0 && !TextUtils.isEmpty(blockFileName);
	}

	public boolean hasBlockAndStageIndex()
	{
		return blockIndex >= 0 && stageIndex >= 0;
	}

	@SuppressWarnings("SimplifiableIfStatement")
	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ChecklistState state = (ChecklistState)o;

		if (blockIndex != state.blockIndex)
			return false;
		if (stageIndex != state.stageIndex)
			return false;
		if (checkpointIndex != state.checkpointIndex)
			return false;

		return blockFileName != null ? blockFileName.equals(state.blockFileName) : state.blockFileName == null;
	}

	@Override
	public int hashCode()
	{
		int result = blockFileName != null ? blockFileName.hashCode() : 0;
		result = 31 * result + blockIndex;
		result = 31 * result + stageIndex;
		result = 31 * result + checkpointIndex;
		return result;
	}
}

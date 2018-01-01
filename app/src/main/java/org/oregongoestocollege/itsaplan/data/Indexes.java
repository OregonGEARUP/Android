package org.oregongoestocollege.itsaplan.data;

/**
 * Holds the indexes needed to find {@link Block}s, {@link Stage}s and {@link Checkpoint}s.
 *
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class Indexes
{
	public final int blockIndex;
	public final int stageIndex;
	public final int checkpointIndex;

	public Indexes(int blockIndex, int stageIndex, int checkpointIndex)
	{
		this.blockIndex = blockIndex;
		this.stageIndex = stageIndex;
		this.checkpointIndex = checkpointIndex;
	}
}

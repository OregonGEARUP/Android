package org.oregongoestocollege.itsaplan.data;

import java.util.List;

/**
 * Block
 * Oregon GEAR UP App
 *
 * Copyright © 2020 Oregon GEAR UP. All rights reserved.
 */
public class Block
{
	public String id;
	public String blocktitle;
	public List<Stage> stages;

	public void addNextStageCheckpoint()
	{
		if (stages != null)
		{
			int size = stages.size();

			// add a next stage cp at the end of each stage (except the last one)
			for (int i = 0; i < size - 1; i++)
			{
				Stage stage = stages.get(i);
				if (stage != null && stage.checkpoints != null)
				{
					stage.checkpoints.add(new Checkpoint("ns", EntryType.nextstage));
				}
			}
		}
	}
}

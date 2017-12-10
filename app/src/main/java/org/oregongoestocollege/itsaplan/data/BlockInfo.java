package org.oregongoestocollege.itsaplan.data;

/**
 * Block
 * Oregon GEAR UP App
 *
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class BlockInfo
{
	// data from the JSON files on the website
	public String ids;
	public String title;
	public String blockFileName;

	// TODO not sure if this will stay here or in view models, data needs to get persisted
	// data stored locally when blocks are viewed / completed
	public int stageCount;
	public int stagesCompleted;

	public boolean available()
	{
		return blockFileName != null && !blockFileName.isEmpty();
	}
}

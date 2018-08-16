package org.oregongoestocollege.itsaplan.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Block
 * Oregon GEAR UP App
 *
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
@Entity(tableName = "block_info_table")
public class BlockInfo
{
	@PrimaryKey(autoGenerate = true)
	private int uid;
	// data from the JSON files on the website
	private String ids;
	private String title;
	private String blockFileName;
	// data stored locally when blocks are viewed / completed
	private int stageCount = -1;
	private int stagesComplete = -1;

	public int getUid()
	{
		return uid;
	}

	public void setUid(int uid)
	{
		this.uid = uid;
	}

	public String getIds()
	{
		return ids;
	}

	public void setIds(String ids)
	{
		this.ids = ids;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getBlockFileName()
	{
		return blockFileName;
	}

	public void setBlockFileName(String blockFileName)
	{
		this.blockFileName = blockFileName;
	}

	public int getStageCount()
	{
		return stageCount;
	}

	public void setStageCount(int stageCount)
	{
		this.stageCount = stageCount;
	}

	public int getStagesComplete()
	{
		return stagesComplete;
	}

	public void setStagesComplete(int stagesComplete)
	{
		this.stagesComplete = stagesComplete;
	}

	public boolean available()
	{
		return blockFileName != null && !blockFileName.isEmpty();
	}

	public boolean done()
	{
		return stageCount != -1 && stageCount == stagesComplete;
	}
}

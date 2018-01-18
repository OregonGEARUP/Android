package org.oregongoestocollege.itsaplan.data;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * Checkpoint
 * Oregon GEAR UP App
 *
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class Checkpoint
{
	public String id;
	public String requiredCP;
	public String title;
	public String description;
	public String url;
	public String urlText;
	public String routeFileName;
	@SerializedName("type")
	public EntryType entryType;
	public List<Instance> instances;
	public List<KeyValue> criteria;

	public Checkpoint()
	{

	}

	public Checkpoint(String id, EntryType entryType)
	{
		this.id = id;
		this.entryType = entryType;
	}

	public boolean required()
	{
		return "yes".equals(requiredCP);
	}

	public boolean isCompleted(int stageIndex, int checkpointIndex)
	{
		if (!required())
			return true;

		// TODO - check based on EntryType
		switch (entryType)
		{
		case info:
		case checkbox:
		case route:
		case nextstage:
			return true;
		case radio:
			// todo
			break;
		case field:
		case dateOnly:
			// todo
			break;
		case dateAndText:
			// todo
			break;
		}

		return true;
	}
}

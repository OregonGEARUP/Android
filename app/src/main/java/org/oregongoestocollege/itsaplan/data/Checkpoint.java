package org.oregongoestocollege.itsaplan.data;

import com.google.gson.annotations.SerializedName;

/**
 * Checkpoint
 * Oregon GEAR UP App
 *
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class Checkpoint
{
	private String requiredCP;
	public String title;
	public String description;
	public String url;
	public String urlText;
	public String routeFileName;
	@SerializedName("type")
	public EntryType entryType;
//	public List<Instance> instances;
//	public List<Criteria> criteria;


	public boolean required()
	{
		return "yes".equals(requiredCP);
	}
}

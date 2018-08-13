package org.oregongoestocollege.itsaplan.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * College
 * Oregon GEAR UP App
 *
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
@Entity(tableName = "password_table")
public class Password
{
	@PrimaryKey
	@NonNull
	private String name;
	private String value;

	public Password(@NonNull String name, String value)
	{
		this.name = name;
		this.value = value;
	}

	@NonNull
	public String getName()
	{
		return name;
	}

	public void setName(@NonNull String name)
	{
		this.name = name;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}
}

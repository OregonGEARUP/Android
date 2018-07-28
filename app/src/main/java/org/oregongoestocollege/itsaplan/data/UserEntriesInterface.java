package org.oregongoestocollege.itsaplan.data;

/**
 * This class is a interface used to retrieve and store user entered data.
 *
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public interface UserEntriesInterface
{
	String getValue(String key);

	boolean getValueAsBoolean(String key);

	long getValueAsLong(String key);

	void setValue(String key, String value);

	void setValue(String key, boolean value);

	void setValue(String key, long value);
}

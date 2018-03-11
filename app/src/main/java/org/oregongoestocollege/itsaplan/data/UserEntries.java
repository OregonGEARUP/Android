package org.oregongoestocollege.itsaplan.data;

import java.util.HashMap;
import java.util.Map;

import android.text.TextUtils;

/**
 * This class is a singleton used to retrieve and store user entered data.
 *
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class UserEntries
{
	private static UserEntries instance;
	private Map<String, String> values = new HashMap<>();

	/**
	 * @return a shared instance of the Manager
	 */
	public static UserEntries getInstance()
	{
		if (instance == null)
			instance = new UserEntries();
		return instance;
	}

	public String getValue(String key)
	{
		if (!TextUtils.isEmpty(key))
			return values.get(key);

		return null;
	}

	public void setValue(String key, String value)
	{
		values.put(key, value);
	}

	public void setValue(String key, boolean value)
	{
		if (value)
			values.put(key, "true");
		else
			values.remove(key);
	}

	public boolean getValueAsBoolean(String key)
	{
		return !TextUtils.isEmpty(key) && "true".equals(values.get(key));
	}

	public void setValue(String key, long value)
	{
		if (value > 0)
			values.put(key, Long.toString(value));
		else
			values.remove(key);
	}

	public long getValueAsLong(String key)
	{
		if (!TextUtils.isEmpty(key))
		{
			String value = values.get(key);
			if (!TextUtils.isEmpty(value))
				return Long.parseLong(value);
		}

		return 0;
	}
}

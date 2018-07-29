package org.oregongoestocollege.itsaplan.data;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;

/**
 * This class is used to retrieve and store user entered data.
 * For now storing in shared preferences since it's reasonable size list of
 * key / value pairs but could move to database.
 *
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class UserEntries implements UserEntriesInterface
{
	private WeakReference<Context> weakRef;

	public UserEntries(@NonNull Context context)
	{
		weakRef = new WeakReference<>(context);
	}

	public String getValue(String key)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(weakRef.get());
		return prefs.getString(key, null);
	}

	public boolean getValueAsBoolean(String key)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(weakRef.get());
		return prefs.getBoolean(key, false);
	}

	public long getValueAsLong(String key)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(weakRef.get());
		return prefs.getLong(key, 0);
	}

	public void setValue(String key, String value)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(weakRef.get());
		SharedPreferences.Editor editor = prefs.edit();
		if (TextUtils.isEmpty(value))
			editor.remove(key);
		else
			editor.putString(key, value);
		editor.apply();
	}

	public void setValue(String key, boolean value)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(weakRef.get());
		SharedPreferences.Editor editor = prefs.edit();
		if (!value)
			editor.remove(key);
		else
			editor.putBoolean(key, true);
		editor.apply();
	}

	public void setValue(String key, long value)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(weakRef.get());
		SharedPreferences.Editor editor = prefs.edit();
		if (value <= 0)
			editor.remove(key);
		else
			editor.putLong(key, value);
		editor.apply();
	}
}

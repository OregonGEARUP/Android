package org.oregongoestocollege.itsaplan.data;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.oregongoestocollege.itsaplan.Utils;

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
		String value = prefs.getString(key, null);
		return "true".equals(value);
	}

	public long getValueAsLong(String key)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(weakRef.get());
		String value = prefs.getString(key, null);
		return !TextUtils.isEmpty(value) ? Long.parseLong(value) : 0;
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
			editor.putString(key, "true");
		editor.apply();
	}

	public void setValue(String key, long value)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(weakRef.get());
		SharedPreferences.Editor editor = prefs.edit();
		if (value <= 0)
			editor.remove(key);
		else
			editor.putString(key, Long.toString(value));
		editor.apply();
	}

	public ChecklistState getChecklistState()
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(weakRef.get());
		return new ChecklistState(
			prefs.getString("currentBlockFileName", null),
			prefs.getInt("currentBlockIndex", Utils.NO_INDEX),
			prefs.getInt("currentStageIndex", Utils.NO_INDEX),
			prefs.getInt("currentCheckpointIndex", Utils.NO_INDEX));
	}

	public void setChecklistState(@NonNull ChecklistState checklistState)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(weakRef.get());
		SharedPreferences.Editor editor = prefs.edit();

		editor.putString("currentBlockFileName", checklistState.blockFileName);
		editor.putInt("currentBlockIndex", checklistState.blockIndex);
		editor.putInt("currentStageIndex", checklistState.stageIndex);
		editor.putInt("currentCheckpointIndex", checklistState.checkpointIndex);
		editor.apply();
	}
}

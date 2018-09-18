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
	private WeakReference<SharedPreferences> weakRef;
	private SharedPreferences.Editor editor;

	public UserEntries(@NonNull Context context)
	{
		weakRef = new WeakReference<>(PreferenceManager.getDefaultSharedPreferences(context));
	}

	public String getValue(String key)
	{
		SharedPreferences prefs = weakRef.get();
		return prefs.getString(key, null);
	}

	public boolean getValueAsBoolean(String key)
	{
		SharedPreferences prefs = weakRef.get();
		String value = prefs.getString(key, null);
		return "1".equals(value);
	}

	public long getValueAsLong(String key)
	{
		SharedPreferences prefs = weakRef.get();
		String value = prefs.getString(key, null);
		return !TextUtils.isEmpty(value) ? Long.parseLong(value) : 0;
	}

	public void close(boolean apply)
	{
		if (editor != null)
		{
			if (apply)
				editor.apply();
			editor = null;
		}
	}

	public void setValue(String key, String value)
	{
		if (editor == null)
			editor = weakRef.get().edit();

		if (TextUtils.isEmpty(value))
			editor.remove(key);
		else
			editor.putString(key, value);
	}

	public void setValue(String key, boolean value)
	{
		if (editor == null)
			editor = weakRef.get().edit();

		// use '1' for true, we later match criteria based on this value
		if (!value)
			editor.remove(key);
		else
			editor.putString(key, "1");
	}

	public void setValue(String key, long value)
	{
		if (editor == null)
			editor = weakRef.get().edit();

		if (value <= 0)
			editor.remove(key);
		else
			editor.putString(key, Long.toString(value));
	}

	public ChecklistState getChecklistState()
	{
		SharedPreferences prefs = weakRef.get();
		return new ChecklistState(
			prefs.getString("currentBlockFileName", null),
			prefs.getInt("currentBlockIndex", Utils.NO_INDEX),
			prefs.getInt("currentStageIndex", Utils.NO_INDEX),
			prefs.getInt("currentCheckpointIndex", Utils.NO_INDEX));
	}

	public void setChecklistState(@NonNull ChecklistState checklistState)
	{
		SharedPreferences prefs = weakRef.get();
		SharedPreferences.Editor editor = prefs.edit();

		editor.putString("currentBlockFileName", checklistState.blockFileName);
		editor.putInt("currentBlockIndex", checklistState.blockIndex);
		editor.putInt("currentStageIndex", checklistState.stageIndex);
		editor.putInt("currentCheckpointIndex", checklistState.checkpointIndex);
		editor.apply();
	}
}

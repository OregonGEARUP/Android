package org.oregongoestocollege.itsaplan.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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

	void close(boolean apply);

	ChecklistState getChecklistState();

	void setChecklistState(@NonNull ChecklistState checklistState);

	String stringWithSubstitutions(@NonNull Context context, @Nullable String original);
}

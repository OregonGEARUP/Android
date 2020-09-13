package org.oregongoestocollege.itsaplan.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * VisitedKey
 * Oregon GEAR UP App
 *
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
@Entity(tableName = "visited_key_table")
public class VisitedKey
{
	@PrimaryKey
	@NonNull
	private String key;

	public VisitedKey()
	{
	}

	@Ignore
	public VisitedKey(@NonNull String key)
	{
		this.key = key;
	}

	@NonNull
	public String getKey()
	{
		return key;
	}

	public void setKey(@NonNull String key)
	{
		this.key = key;
	}

	@Nullable
	static final VisitedKey[] toArrayList(@Nullable Set<String> keys)
	{
		int size = keys != null ? keys.size() : 0;
		if (size > 0)
		{
			ArrayList<VisitedKey> list = new ArrayList<>(size);
			for (String key : keys)
				list.add(new VisitedKey(key));

			return list.toArray(new VisitedKey[list.size()]);
		}

		return null;
	}

	@NonNull
	static Set<String> toSet(VisitedKey[] keys)
	{
		HashSet<String> set = new HashSet<>();
		if (keys != null)
		{
			for (VisitedKey key : keys)
				set.add(key.getKey());
		}
		return set;
	}
}

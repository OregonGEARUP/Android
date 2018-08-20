package org.oregongoestocollege.itsaplan.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import org.oregongoestocollege.itsaplan.data.VisitedKey;

/**
 * VisitedKeyDao
 * Oregon GEAR UP App
 *
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
@Dao
public abstract interface VisitedKeyDao
{
	@Insert(onConflict = OnConflictStrategy.IGNORE)
	abstract void insertAll(VisitedKey... visitedKeys);

	@Query("DELETE FROM visited_key_table")
	abstract void deleteAll();

/*	@Transaction
	void deleteAndInsertAll(VisitedKey[] visitedKeys)
	{
		// Anything inside this method runs in a single transaction.
		deleteAll();
		insertAll(visitedKeys);
	}*/

	@Query("SELECT * from visited_key_table")
	abstract VisitedKey[] getAllDirect();
}

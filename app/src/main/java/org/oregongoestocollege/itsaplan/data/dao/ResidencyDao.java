package org.oregongoestocollege.itsaplan.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.oregongoestocollege.itsaplan.data.Residency;

import static androidx.room.OnConflictStrategy.FAIL;

/**
 * Residency
 * Oregon GEAR UP App
 *
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
@Dao
public interface ResidencyDao
{
	@Insert(onConflict = FAIL)
	void insert(Residency residency);

	@Update
	void update(Residency residency);

	@Query("SELECT * from residency_table where uid = :uniqueId")
	LiveData<Residency> getResidency(String uniqueId);
}

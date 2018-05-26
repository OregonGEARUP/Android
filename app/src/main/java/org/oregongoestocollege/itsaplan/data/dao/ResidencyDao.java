package org.oregongoestocollege.itsaplan.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import org.oregongoestocollege.itsaplan.data.Residency;

import static android.arch.persistence.room.OnConflictStrategy.FAIL;

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

package org.oregongoestocollege.itsaplan.data.dao;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.oregongoestocollege.itsaplan.data.Scholarship;

import static androidx.room.OnConflictStrategy.FAIL;

/**
 * Scholarship
 * Oregon GEAR UP App
 *
 * Copyright © 2020 Oregon GEAR UP. All rights reserved.
 */
@Dao
public interface ScholarshipDao
{
	@Insert(onConflict = FAIL)
	void insert(Scholarship scholarship);

	@Update
	void update(Scholarship scholarship);

	@Delete
	void delete(Scholarship scholarship);

	@Query("SELECT * from scholarship_table")
	LiveData<List<Scholarship>> getAll();

	@Query("SELECT * from scholarship_table")
	Scholarship[] getAllDirect();
}

package org.oregongoestocollege.itsaplan.data.dao;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.oregongoestocollege.itsaplan.data.College;

import static androidx.room.OnConflictStrategy.FAIL;

/**
 * College
 * Oregon GEAR UP App
 *
 * Copyright © 2020 Oregon GEAR UP. All rights reserved.
 */
@Dao
public interface CollegeDao
{
	@Insert(onConflict = FAIL)
	void insert(College college);

	@Update
	void update(College college);

	@Delete
	void delete(College college);

	@Query("SELECT * from college_table")
	LiveData<List<College>> getAll();

	@Query("SELECT * from college_table")
	College[] getAllDirect();
}

package org.oregongoestocollege.itsaplan.data.dao;

import java.util.List;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import org.oregongoestocollege.itsaplan.data.College;

import static android.arch.persistence.room.OnConflictStrategy.FAIL;

/**
 * College
 * Oregon GEAR UP App
 *
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
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

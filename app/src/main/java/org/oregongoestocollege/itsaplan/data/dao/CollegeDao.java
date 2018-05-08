package org.oregongoestocollege.itsaplan.data.dao;

import java.util.List;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import org.oregongoestocollege.itsaplan.data.College;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * College
 * Oregon GEAR UP App
 *
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
@Dao
public interface CollegeDao
{
	@Insert(onConflict = REPLACE)
	void save(College college);

	@Delete
	void delete(College college);

//	@Query("SELECT * FROM college_table WHERE name = :name")
//	LiveData<College> load(String name);

	@Query("SELECT * from college_table")
	LiveData<List<College>> getAll();
}

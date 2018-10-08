package org.oregongoestocollege.itsaplan.data.dao;

import java.util.List;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import org.oregongoestocollege.itsaplan.data.Scholarship;

import static android.arch.persistence.room.OnConflictStrategy.FAIL;

/**
 * Scholarship
 * Oregon GEAR UP App
 *
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
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

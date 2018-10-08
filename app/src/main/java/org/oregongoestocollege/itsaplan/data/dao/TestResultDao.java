package org.oregongoestocollege.itsaplan.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import org.oregongoestocollege.itsaplan.data.TestResult;

import static android.arch.persistence.room.OnConflictStrategy.FAIL;

/**
 * TestResult
 * Oregon GEAR UP App
 *
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
@Dao
public interface TestResultDao
{
	@Insert(onConflict = FAIL)
	void insert(TestResult testResult);

	@Update
	void update(TestResult testResult);

	@Query("SELECT * from testresult_table where name = :name")
	LiveData<TestResult> getTestResult(String name);

	@Query("SELECT * from testresult_table")
	TestResult[] getAllDirect();
}

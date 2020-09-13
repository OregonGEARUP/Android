package org.oregongoestocollege.itsaplan.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.oregongoestocollege.itsaplan.data.TestResult;

import static androidx.room.OnConflictStrategy.FAIL;

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

package org.oregongoestocollege.itsaplan.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import org.oregongoestocollege.itsaplan.data.dao.CollegeDao;
import org.oregongoestocollege.itsaplan.data.dao.DateConverter;
import org.oregongoestocollege.itsaplan.data.dao.ScholarshipDao;
import org.oregongoestocollege.itsaplan.data.dao.TestResultDao;

/**
 * MyPlanDatabase - persist data for My Plan tab
 * Oregon GEAR UP App
 *
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
@Database(entities = { College.class, Scholarship.class, TestResult.class }, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class MyPlanDatabase extends RoomDatabase
{
	private static MyPlanDatabase instance;

	/**
	 * @return a shared instance of the database
	 */
	public static MyPlanDatabase getDatabase(final Context context)
	{
		if (instance == null)
		{
			synchronized (MyPlanDatabase.class)
			{
				if (instance == null)
				{
					// Create database here
					instance = Room.databaseBuilder(context.getApplicationContext(),
						MyPlanDatabase.class, "myplan_database")
						.addCallback(sRoomDatabaseCallback)
						.build();
				}
			}
		}
		return instance;
	}

	private static RoomDatabase.Callback sRoomDatabaseCallback =
		new RoomDatabase.Callback()
		{
			@Override
			public void onCreate(@NonNull SupportSQLiteDatabase db)
			{
				super.onCreate(db);
				new PopulateDbAsync(instance).execute();
			}
		};

	private static class PopulateDbAsync extends AsyncTask<Void, Void, Void>
	{
		private final TestResultDao testResultDao;

		PopulateDbAsync(MyPlanDatabase db)
		{
			testResultDao = db.testResultDao();
		}

		@Override
		protected Void doInBackground(final Void... params)
		{
			// add our 2 supported test results
			TestResult testResult = new TestResult(TestResult.NAME_ACT);
			testResultDao.insert(testResult);
			testResult = new TestResult(TestResult.NAME_SAT);
			testResultDao.insert(testResult);
			return null;
		}
	}

	public abstract CollegeDao collegeDao();

	public abstract ScholarshipDao scholarshipDao();

	public abstract TestResultDao testResultDao();
}

package org.oregongoestocollege.itsaplan.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * MyPlanDatabase - persist data for My Plan tab
 * Oregon GEAR UP App
 *
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
@Database(entities = { College.class }, version = 1)
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
						.build();
				}
			}
		}
		return instance;
	}

	public abstract College.CollegeDao collegeDao();
}

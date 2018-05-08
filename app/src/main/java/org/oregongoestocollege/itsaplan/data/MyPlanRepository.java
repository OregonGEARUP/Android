package org.oregongoestocollege.itsaplan.data;

import java.util.List;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import org.oregongoestocollege.itsaplan.data.dao.CollegeDao;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class MyPlanRepository
{
	private CollegeDao collegeDao;
	private LiveData<List<College>> allColleges;

	/**
	 * Constructor to initialize the database and variables
	 */
	public MyPlanRepository(Application application)
	{
		MyPlanDatabase db = MyPlanDatabase.getDatabase(application);
		collegeDao = db.collegeDao();
		allColleges = collegeDao.getAll();
	}

	private static class insertAsyncTask extends AsyncTask<College, Void, Void>
	{
		private CollegeDao mAsyncTaskDao;

		insertAsyncTask(CollegeDao dao)
		{
			mAsyncTaskDao = dao;
		}

		@Override
		protected Void doInBackground(final College... params)
		{
			mAsyncTaskDao.save(params[0]);
			return null;
		}
	}

	private static class deleteAsyncTask extends AsyncTask<College, Void, Void>
	{
		private CollegeDao mAsyncTaskDao;

		deleteAsyncTask(CollegeDao dao)
		{
			mAsyncTaskDao = dao;
		}

		@Override
		protected Void doInBackground(final College... params)
		{
			mAsyncTaskDao.delete(params[0]);
			return null;
		}
	}

	/**
	 * Wrapper to get all colleges from the database. Room executes all queries on a separate thread.
	 * Observed LiveData will notify the observer when the data has changed.
	 */
	public LiveData<List<College>> getAllColleges()
	{
		return allColleges;
	}

	/**
	 * Wrapper to save a college. You must call this on a non-UI thread or your app will crash.
	 * Room ensures that you don't do any long-running operations on the main thread, blocking the UI.
	 */
	public void save(College college)
	{
		new insertAsyncTask(collegeDao).execute(college);
	}

	/**
	 * Wrapper to delete a college. You must call this on a non-UI thread or your app will crash.
	 * Room ensures that you don't do any long-running operations on the main thread, blocking the UI.
	 */
	public void delete(College college)
	{
		new deleteAsyncTask(collegeDao).execute(college);
	}
}

package org.oregongoestocollege.itsaplan.data;

import java.util.List;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.oregongoestocollege.itsaplan.data.dao.CollegeDao;
import org.oregongoestocollege.itsaplan.data.dao.TestResultDao;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class MyPlanRepository
{
	private CollegeDao collegeDao;
	private TestResultDao testResultDao;
	private LiveData<List<College>> allColleges;

	/**
	 * Constructor to initialize the database and variables
	 */
	public MyPlanRepository(Application application)
	{
		MyPlanDatabase db = MyPlanDatabase.getDatabase(application);
		collegeDao = db.collegeDao();
		allColleges = collegeDao.getAll();
		testResultDao = db.testResultDao();
	}

	private static class InsertCollegeAsyncTask extends AsyncTask<College, Void, Void>
	{
		private CollegeDao mAsyncTaskDao;

		InsertCollegeAsyncTask(CollegeDao dao)
		{
			mAsyncTaskDao = dao;
		}

		@Override
		protected Void doInBackground(final College... params)
		{
			mAsyncTaskDao.insert(params[0]);
			return null;
		}
	}

	private static class DeleteCollegeAsyncTask extends AsyncTask<College, Void, Void>
	{
		private CollegeDao mAsyncTaskDao;

		DeleteCollegeAsyncTask(CollegeDao dao)
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

	private static class UpdateCollegeAsyncTask extends AsyncTask<College, Void, Void>
	{
		private CollegeDao mAsyncTaskDao;

		UpdateCollegeAsyncTask(CollegeDao dao)
		{
			mAsyncTaskDao = dao;
		}

		@Override
		protected Void doInBackground(final College... params)
		{
			mAsyncTaskDao.update(params[0]);
			return null;
		}
	}

	private static class UpdateTestResultAsyncTask extends AsyncTask<TestResult, Void, Void>
	{
		private TestResultDao mAsyncTaskDao;

		UpdateTestResultAsyncTask(TestResultDao dao)
		{
			mAsyncTaskDao = dao;
		}

		@Override
		protected Void doInBackground(final TestResult... params)
		{
			mAsyncTaskDao.update(params[0]);
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
	 * Wrapper to insert a new college. You must call this on a non-UI thread or your app will crash.
	 * Room ensures that you don't do any long-running operations on the main thread, blocking the UI.
	 */
	public void insertCollege(@NonNull String name)
	{
		if (TextUtils.isEmpty(name))
			return;

		College college = new College();
		college.setName(name);
		new InsertCollegeAsyncTask(collegeDao).execute(college);
	}

	/**
	 * Wrapper to delete a college. You must call this on a non-UI thread or your app will crash.
	 * Room ensures that you don't do any long-running operations on the main thread, blocking the UI.
	 */
	public void delete(College college)
	{
		new DeleteCollegeAsyncTask(collegeDao).execute(college);
	}

	/**
	 * Wrapper to update a college. You must call this on a non-UI thread or your app will crash.
	 * Room ensures that you don't do any long-running operations on the main thread, blocking the UI.
	 */
	public void update(College college)
	{
		new UpdateCollegeAsyncTask(collegeDao).execute(college);
	}

	/**
	 * Wrapper to update a test result. You must call this on a non-UI thread or your app will crash.
	 * Room ensures that you don't do any long-running operations on the main thread, blocking the UI.
	 */
	public void update(TestResult testResult)
	{
		new UpdateTestResultAsyncTask(testResultDao).execute(testResult);
	}
}

package org.oregongoestocollege.itsaplan.data;

import java.util.List;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.oregongoestocollege.itsaplan.data.dao.CollegeDao;
import org.oregongoestocollege.itsaplan.data.dao.ResidencyDao;
import org.oregongoestocollege.itsaplan.data.dao.ScholarshipDao;
import org.oregongoestocollege.itsaplan.data.dao.TestResultDao;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class MyPlanRepository
{
	private static MyPlanRepository instance;
	private final MyPlanDatabase database;
	private CollegeDao collegeDao;
	private ScholarshipDao scholarshipDao;
	private TestResultDao testResultDao;
	private ResidencyDao residencyDao;
	private LiveData<List<College>> allColleges;
	private LiveData<List<Scholarship>> allScholarships;

	/**
	 * Constructor to initialize the database and variables
	 */
	private MyPlanRepository(final MyPlanDatabase database)
	{
		this.database = database;

		collegeDao = database.collegeDao();
		allColleges = collegeDao.getAll();
		scholarshipDao = database.scholarshipDao();
		allScholarships = scholarshipDao.getAll();
		testResultDao = database.testResultDao();
		residencyDao = database.residencyDao();
	}

	public static MyPlanRepository getInstance(final MyPlanDatabase database)
	{
		if (instance == null)
		{
			synchronized (MyPlanRepository.class)
			{
				if (instance == null)
					instance = new MyPlanRepository(database);
			}
		}
		return instance;
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

	private static class InsertScholarshipAsyncTask extends AsyncTask<Scholarship, Void, Void>
	{
		private ScholarshipDao mAsyncTaskDao;

		InsertScholarshipAsyncTask(ScholarshipDao dao)
		{
			mAsyncTaskDao = dao;
		}

		@Override
		protected Void doInBackground(final Scholarship... params)
		{
			mAsyncTaskDao.insert(params[0]);
			return null;
		}
	}

	private static class DeleteScholarshipAsyncTask extends AsyncTask<Scholarship, Void, Void>
	{
		private ScholarshipDao mAsyncTaskDao;

		DeleteScholarshipAsyncTask(ScholarshipDao dao)
		{
			mAsyncTaskDao = dao;
		}

		@Override
		protected Void doInBackground(final Scholarship... params)
		{
			mAsyncTaskDao.delete(params[0]);
			return null;
		}
	}

	private static class UpdateScholarshipAsyncTask extends AsyncTask<Scholarship, Void, Void>
	{
		private ScholarshipDao mAsyncTaskDao;

		UpdateScholarshipAsyncTask(ScholarshipDao dao)
		{
			mAsyncTaskDao = dao;
		}

		@Override
		protected Void doInBackground(final Scholarship... params)
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

	private static class UpdateResidencyAsyncTask extends AsyncTask<Residency, Void, Void>
	{
		private ResidencyDao mAsyncTaskDao;

		UpdateResidencyAsyncTask(ResidencyDao dao)
		{
			mAsyncTaskDao = dao;
		}

		@Override
		protected Void doInBackground(final Residency... params)
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
	 * Wrapper to get all scholarships from the database. Room executes all queries on a separate thread.
	 * Observed LiveData will notify the observer when the data has changed.
	 */
	public LiveData<List<Scholarship>> getAllScholarships()
	{
		return allScholarships;
	}

	/**
	 * Wrapper to insert a new scholarship. You must call this on a non-UI thread or your app will crash.
	 * Room ensures that you don't do any long-running operations on the main thread, blocking the UI.
	 */
	public void insertScholarship(@NonNull String name)
	{
		if (TextUtils.isEmpty(name))
			return;

		Scholarship scholarship = new Scholarship();
		scholarship.setName(name);
		new InsertScholarshipAsyncTask(scholarshipDao).execute(scholarship);
	}

	/**
	 * Wrapper to delete a scholarship. You must call this on a non-UI thread or your app will crash.
	 * Room ensures that you don't do any long-running operations on the main thread, blocking the UI.
	 */
	public void delete(Scholarship scholarship)
	{
		new DeleteScholarshipAsyncTask(scholarshipDao).execute(scholarship);
	}

	/**
	 * Wrapper to update a scholarship. You must call this on a non-UI thread or your app will crash.
	 * Room ensures that you don't do any long-running operations on the main thread, blocking the UI.
	 */
	public void update(Scholarship scholarship)
	{
		new UpdateScholarshipAsyncTask(scholarshipDao).execute(scholarship);
	}

	/**
	 * Wrapper to update a test result. You must call this on a non-UI thread or your app will crash.
	 * Room ensures that you don't do any long-running operations on the main thread, blocking the UI.
	 */
	public void update(TestResult testResult)
	{
		new UpdateTestResultAsyncTask(testResultDao).execute(testResult);
	}

	/**
	 * Wrapper to get ACT test result from the database. Room executes all queries on a separate thread.
	 * Observed LiveData will notify the observer when the data has changed.
	 */
	public LiveData<TestResult> getActTestResult()
	{
		return database.testResultDao().getTestResult(TestResult.NAME_ACT);
	}

	/**
	 * Wrapper to get SAT test result from the database. Room executes all queries on a separate thread.
	 * Observed LiveData will notify the observer when the data has changed.
	 */
	public LiveData<TestResult> getSatTestResult()
	{
		return database.testResultDao().getTestResult(TestResult.NAME_SAT);
	}

	/**
	 * Wrapper to get a residency from the database. Room executes all queries on a separate thread.
	 * Observed LiveData will notify the observer when the data has changed.
	 */
	public LiveData<Residency> getResidency()
	{
		return database.residencyDao().getResidency(Residency.NAME_PRIMARY);
	}

	/**
	 * Wrapper to update a residency. You must call this on a non-UI thread or your app will crash.
	 * Room ensures that you don't do any long-running operations on the main thread, blocking the UI.
	 */
	public void update(Residency residency)
	{
		new UpdateResidencyAsyncTask(residencyDao).execute(residency);
	}
}

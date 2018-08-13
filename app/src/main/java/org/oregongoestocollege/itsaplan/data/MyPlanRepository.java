package org.oregongoestocollege.itsaplan.data;

import java.util.List;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.oregongoestocollege.itsaplan.data.dao.CollegeDao;
import org.oregongoestocollege.itsaplan.data.dao.DateConverter;
import org.oregongoestocollege.itsaplan.data.dao.PasswordsDao;
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
	private PasswordsDao passwordsDao;
	private LiveData<List<College>> allColleges;
	private LiveData<List<Scholarship>> allScholarships;
	private LiveData<List<Password>> allPasswords;

	/**
	 * Constructor to initialize the database and variables
	 */
	private MyPlanRepository(@NonNull Context context)
	{
		this.database = MyPlanDatabase.getDatabase(context);

		collegeDao = database.collegeDao();
		allColleges = collegeDao.getAll();
		scholarshipDao = database.scholarshipDao();
		allScholarships = scholarshipDao.getAll();
		testResultDao = database.testResultDao();
		residencyDao = database.residencyDao();
		passwordsDao = database.passwordsDao();
		allPasswords = passwordsDao.getAll();
	}

	public static MyPlanRepository getInstance(@NonNull Context context)
	{
		if (instance == null)
		{
			synchronized (MyPlanRepository.class)
			{
				if (instance == null)
					instance = new MyPlanRepository(context);
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

	private static class InsertPasswordAsyncTask extends AsyncTask<Password, Void, Void>
	{
		private PasswordsDao mAsyncTaskDao;

		InsertPasswordAsyncTask(PasswordsDao dao)
		{
			mAsyncTaskDao = dao;
		}

		@Override
		protected Void doInBackground(Password... passwords)
		{
			for (Password password : passwords)
				mAsyncTaskDao.insert(password);
			return null;
		}
	}

	private static class UpdatePasswordAsyncTask extends AsyncTask<Password, Void, Void>
	{
		private PasswordsDao mAsyncTaskDao;

		UpdatePasswordAsyncTask(PasswordsDao dao)
		{
			mAsyncTaskDao = dao;
		}

		@Override
		protected Void doInBackground(Password... passwords)
		{
			for (Password password : passwords)
				mAsyncTaskDao.update(password);
			return null;
		}
	}

	private static class DeletePasswordAsyncTask extends AsyncTask<Password, Void, Void>
	{
		private PasswordsDao mAsyncTaskDao;

		DeletePasswordAsyncTask(PasswordsDao dao)
		{
			mAsyncTaskDao = dao;
		}

		@Override
		protected Void doInBackground(Password... passwords)
		{
			for (Password password : passwords)
				mAsyncTaskDao.delete(password);
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

	public LiveData<List<Password>> getAllPasswords()
	{
		return allPasswords;
	}

	public void insertPassword(String name, String s)
	{
		if (TextUtils.isEmpty(name))
			return;

		Password password = new Password(name, s);
		new InsertPasswordAsyncTask(passwordsDao).execute(password);
	}

	public void updatePassword(Password password)
	{
		new UpdatePasswordAsyncTask(passwordsDao).execute(password);
	}

	public void deletePassword(Password password)
	{
		new DeletePasswordAsyncTask(passwordsDao).execute(password);
	}

	/**
	 * Add the first college from user entered data when visiting the My Plan - college tab.
	 */
	public void insertFirstCollege(@NonNull UserEntriesInterface userEntries, String defaultName)
	{
		// determine what college name was entered in the checkpoint
		String value = userEntries.getValue("b2_s3_cp2_i1_text");
		if (TextUtils.isEmpty(value))
			value = defaultName;

		College college = new College();
		college.setName(value);

		// fill in any missing pieces of the first college from the checkpoints
		long appDate = userEntries.getValueAsLong("b2_s3_cp2_i1_date");
		if (appDate > 0)
			college.setApplicationDate(DateConverter.toDate(appDate));

		value = userEntries.getValue("b3citizen_s1_cp3_i1");
		if (!TextUtils.isEmpty(value))
			college.setAverageNetPrice(value);
		else
		{
			value = userEntries.getValue("b3undoc_s1_cp3_i1");
			if (!TextUtils.isEmpty(value))
				college.setAverageNetPrice(value);
			else
			{
				value = userEntries.getValue("b3visa_s1_cp3_i1");
				if (TextUtils.isEmpty(value))
					college.setAverageNetPrice(value);
			}
		}

		new InsertCollegeAsyncTask(collegeDao).execute(college);
	}

	/**
	 * Add the first scholarship from user entered data when visiting the My Plan - scholarship tab.
	 */
	public void insertFirstScholarship(@NonNull UserEntriesInterface userEntries, String defaultName)
	{
		String value = userEntries.getValue("b3citizen_s2_cp2_i1_text");

		Scholarship scholarship = new Scholarship();

		if (!TextUtils.isEmpty(value = userEntries.getValue("b3citizen_s2_cp2_i1_text")))
		{
			scholarship.setName(value);

			long appDate = userEntries.getValueAsLong("b3citizen_s2_cp2_i1_date");
			if (appDate > 0)
				scholarship.setApplicationDate(DateConverter.toDate(appDate));
		}
		else if (!TextUtils.isEmpty(value = userEntries.getValue("b3undoc_s2_cp2_i1_text")))
		{
			scholarship.setName(value);

			long appDate = userEntries.getValueAsLong("b3undoc_s2_cp2_i1_date");
			if (appDate > 0)
				scholarship.setApplicationDate(DateConverter.toDate(appDate));
		}
		else if (!TextUtils.isEmpty(value = userEntries.getValue("b3visa_s2_cp2_i1_text")))
		{
			scholarship.setName(value);

			long appDate = userEntries.getValueAsLong("b3visa_s2_cp2_i1_date");
			if (appDate > 0)
				scholarship.setApplicationDate(DateConverter.toDate(appDate));
		}
		else
			scholarship.setName(defaultName);

		new InsertScholarshipAsyncTask(scholarshipDao).execute(scholarship);
	}
}

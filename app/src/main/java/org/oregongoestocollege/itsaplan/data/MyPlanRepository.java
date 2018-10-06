package org.oregongoestocollege.itsaplan.data;

import java.util.List;
import java.util.Set;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.oregongoestocollege.itsaplan.Utils;
import org.oregongoestocollege.itsaplan.data.dao.BlockInfoDao;
import org.oregongoestocollege.itsaplan.data.dao.CollegeDao;
import org.oregongoestocollege.itsaplan.data.dao.ResidencyDao;
import org.oregongoestocollege.itsaplan.data.dao.ScholarshipDao;
import org.oregongoestocollege.itsaplan.data.dao.TestResultDao;
import org.oregongoestocollege.itsaplan.data.dao.VisitedKeyDao;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class MyPlanRepository
{
	private final static String LOG_TAG = "GearUp_MyPlanRepo";
	private static MyPlanRepository instance;
	private final MyPlanDatabase database;
	private CollegeDao collegeDao;
	private ScholarshipDao scholarshipDao;
	private TestResultDao testResultDao;
	private ResidencyDao residencyDao;
	private LiveData<List<College>> allColleges;
	private LiveData<List<Scholarship>> allScholarships;
	private LiveData<List<BlockInfo>> allBlockInfos;
	private MutableLiveData<ResponseData<List<CalendarEvent>>> allCalendarEvents;
	// pending Tasks
	private MyPlanTasks.CalendarEventsTask calendarEventsTask;
	// allow for direct access for queries performed on a background thread
	BlockInfoDao blockInfoDao;
	VisitedKeyDao visitedKeyDao;

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
		blockInfoDao = database.blockInfoDao();
		allBlockInfos = blockInfoDao.getAll();
		visitedKeyDao = database.visitedKeyDao();

		allCalendarEvents = new MutableLiveData<>();
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

	private static class UpdateBlockInfoAsyncTask extends AsyncTask<BlockInfo, Void, Void>
	{
		private BlockInfoDao mAsyncTaskDao;

		UpdateBlockInfoAsyncTask(BlockInfoDao dao)
		{
			mAsyncTaskDao = dao;
		}

		@Override
		protected Void doInBackground(final BlockInfo... params)
		{
			mAsyncTaskDao.update(params[0]);
			return null;
		}
	}

	private static class InsertBlockInfoAsyncTask extends AsyncTask<BlockInfo, Void, Void>
	{
		private BlockInfoDao mAsyncTaskDao;

		InsertBlockInfoAsyncTask(BlockInfoDao dao)
		{
			mAsyncTaskDao = dao;
		}

		@Override
		protected Void doInBackground(BlockInfo... blockInfos)
		{
			mAsyncTaskDao.insertAll(blockInfos);
			return null;
		}
	}

	private static class DeleteAndInsertVisitedKeysAsyncTask extends AsyncTask<VisitedKey, Void, Void>
	{
		private VisitedKeyDao mAsyncTaskDao;

		DeleteAndInsertVisitedKeysAsyncTask(VisitedKeyDao dao)
		{
			mAsyncTaskDao = dao;
		}

		@Override
		protected Void doInBackground(VisitedKey... visitedKeys)
		{
			mAsyncTaskDao.deleteAll();
			if (visitedKeys != null)
				mAsyncTaskDao.insertAll(visitedKeys);
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
	 * Wrapper to insert a new college. You must call this on a non-UI thread or your app will crash.
	 * Room ensures that you don't do any long-running operations on the main thread, blocking the UI.
	 */
	public void insertCollege(@NonNull College college)
	{
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
	 * Wrapper to insert a new scholarship. You must call this on a non-UI thread or your app will crash.
	 * Room ensures that you don't do any long-running operations on the main thread, blocking the UI.
	 */
	public void insertScholarship(@NonNull Scholarship scholarship)
	{
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

	/**
	 * Wrapper to get all block info-s from the database. Room executes all queries on a separate thread.
	 * Observed LiveData will notify the observer when the data has changed.
	 */
	LiveData<List<BlockInfo>> getAllBlockInfos()
	{
		return allBlockInfos;
	}

	/**
	 * Wrapper to update a college. You must call this on a non-UI thread or your app will crash.
	 * Room ensures that you don't do any long-running operations on the main thread, blocking the UI.
	 */
	void update(BlockInfo blockInfo)
	{
		new UpdateBlockInfoAsyncTask(blockInfoDao).execute(blockInfo);
	}

	/**
	 * Insert all BlockInfo(s) into the database
	 */
	void insertBlockInfos(@NonNull List<BlockInfo> list)
	{
		if (list.isEmpty())
			return;

		final BlockInfo[] array = list.toArray(new BlockInfo[list.size()]);

		new InsertBlockInfoAsyncTask(blockInfoDao).execute(array);
	}

	/**
	 * Insert the list of visited keys, deleting all the ones that existed before.
	 */
	void replaceVisitedKeys(@Nullable Set<String> keys)
	{
		final VisitedKey[] array = VisitedKey.toArrayList(keys);

		new DeleteAndInsertVisitedKeysAsyncTask(visitedKeyDao).execute(array);
	}

	/**
	 * Wrapper to get all calendar events. All network / room queries are performed on a separate thread.
	 * Observed LiveData will notify the observer when the data has changed.
	 */
	public LiveData<ResponseData<List<CalendarEvent>>> getCalendarEvents()
	{
		return allCalendarEvents;
	}

	public void loadCalendarEvents()
	{
		// setup a background task or hookup to our existing one
		MyPlanTasks.CalendarEventsTask newTask = null;
		synchronized (this)
		{
			if (calendarEventsTask == null)
			{
				newTask = new MyPlanTasks.CalendarEventsTask(this);
				calendarEventsTask = newTask;

				Utils.d(LOG_TAG, "CalendarEventsTask starting");
			}
			else
				Utils.d(LOG_TAG, "CalendarEventsTask pending");
		}

		if (newTask != null)
		{
			allCalendarEvents.setValue(ResponseData.loading(null));
			newTask.execute();
		}
	}

	void loadCalendarEventsCompleted(@NonNull ResponseData<List<CalendarEvent>> responseData)
	{
		Utils.d(LOG_TAG, "CalendarEventsTask ending");

		calendarEventsTask = null;
		allCalendarEvents.setValue(responseData);
	}
}

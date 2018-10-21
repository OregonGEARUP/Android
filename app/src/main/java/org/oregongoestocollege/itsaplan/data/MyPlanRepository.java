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

import org.oregongoestocollege.itsaplan.SingleLiveEvent;
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
	private ResidencyDao residencyDao;
	private LiveData<List<College>> allColleges;
	private LiveData<List<Scholarship>> allScholarships;
	private LiveData<List<BlockInfo>> allBlockInfos;
	private MutableLiveData<ResponseData<List<CalendarEvent>>> allCalendarEvents;
	// calendar fields
	private MyPlanTasks.CalendarEventsTask calendarEventsTask;
	private List<CalendarEventData> calendarEventDataFromNetwork;
	private boolean collegeDeleted;
	private boolean scholarshipDeleted;
	// allow for direct access for queries performed in tasks / on a background thread
	CollegeDao collegeDao;
	ScholarshipDao scholarshipDao;
	TestResultDao testResultDao;
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

		allCalendarEvents = new SingleLiveEvent<>();
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
		collegeDeleted = true;

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
		scholarshipDeleted = true;

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

	/**
	 * Private method to make sure we only have one task executing to retrieve calendar events.
	 */
	private void loadCalendarEvents(@NonNull Context context, boolean prefetch)
	{
		boolean loading = false;

		// setup a background task or hookup to our existing one
		MyPlanTasks.CalendarEventsTask newTask = null;
		synchronized (this)
		{
			if (!prefetch || calendarEventDataFromNetwork == null)
			{
				if (calendarEventsTask == null)
				{
					// have task use previously loaded network data when available
					newTask = new MyPlanTasks.CalendarEventsTask(context, calendarEventDataFromNetwork);
					calendarEventsTask = newTask;

					Utils.d(LOG_TAG, "CalendarEventsTask starting");
				}
				else
					Utils.d(LOG_TAG, "CalendarEventsTask pending");

				loading = true;
			}
			else
				Utils.d(LOG_TAG, "CalendarEventsTask skipped");
		}

		if (loading)
		{
			allCalendarEvents.setValue(ResponseData.loading(null));

			if (newTask != null)
				newTask.execute();
		}
	}

	void loadCalendarEventsCompleted(@NonNull ResponseData<List<CalendarEvent>> responseData)
	{
		Utils.d(LOG_TAG, "CalendarEventsTask ending");

		// keep track of the network data, we only want to retrieve it once
		calendarEventDataFromNetwork = calendarEventsTask.getRawData();
		calendarEventsTask = null;
		allCalendarEvents.setValue(responseData);
	}

	/**
	 * Called on start of main activity to pre-load any calendar events from the network.
	 * If we already have data from the network this method should do nothing...
	 */
	public void preFetchCalenderEvents(@NonNull Context context)
	{
		loadCalendarEvents(context, true);
	}

	/**
	 * Called when displaying calender to user. Reflects any changes made to dates.
	 */
	public void loadCalendarEvents(@NonNull Context context)
	{
		loadCalendarEvents(context, false);
	}

	public void updateCollegeNotifications(@NonNull Context context, boolean refresh)
	{
		// could optimize to only update the scheduled college notifications
		if (collegeDeleted || refresh)
		{
			MyPlanTasks.CalendarSchedulerTask task =
				new MyPlanTasks.CalendarSchedulerTask(context, calendarEventDataFromNetwork);
			task.execute();
		}

		collegeDeleted = false;
	}

	public void updateScholarshipNotifications(@NonNull Context context, boolean refresh)
	{
		// could optimize to only update the scheduled scholarship notifications
		if (scholarshipDeleted || refresh)
		{
			MyPlanTasks.CalendarSchedulerTask task =
				new MyPlanTasks.CalendarSchedulerTask(context, calendarEventDataFromNetwork);
			task.execute();
		}

		scholarshipDeleted = false;
	}

	public void updateTestNotifications(@NonNull Context context, boolean refresh)
	{
		// could optimize to only update the scheduled test notifications
		if (refresh)
		{
			MyPlanTasks.CalendarSchedulerTask task =
				new MyPlanTasks.CalendarSchedulerTask(context, calendarEventDataFromNetwork);
			task.execute();
		}
	}
}

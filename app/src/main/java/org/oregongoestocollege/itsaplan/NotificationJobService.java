package org.oregongoestocollege.itsaplan;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.format.DateUtils;

import org.oregongoestocollege.itsaplan.data.CalendarEvent;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class NotificationJobService extends JobService
{
	private static final String LOG_TAG = "GearUp_Notifications";
	private static final String CHANNEL_ID = "GearUpChannelDefault";
	private static final String KEY_NOTIFICATION_ID = "notif-id";
	private static final String KEY_NOTIFICATION_MESSAGE = "notif-message";
	private static final SimpleDateFormat dateFormat =
		new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());

	/**
	 * This is called by the system once it determines it is time to run the job.
	 *
	 * @param jobParameters Contains the information about the job
	 * @return Boolean indicating whether or not the job was offloaded to a separate thread.
	 * In this case, it is false since the notification can be posted on the main thread.
	 */
	@Override
	public boolean onStartJob(JobParameters jobParameters)
	{
		PersistableBundle extras = jobParameters.getExtras();
		final int id = extras.getInt(KEY_NOTIFICATION_ID);
		final String message = extras.getString(KEY_NOTIFICATION_MESSAGE);

		if (Utils.DEBUG)
			Utils.d(LOG_TAG, "Notification id:%d message:%s", id, message);

		// setup intent to launch app if notification is clicked
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
			new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

		// need channel for API 26+
		createNotificationChannel(this);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
			.setSmallIcon(R.drawable.ic_event_black_24dp)
			.setContentTitle(this.getString(R.string.app_name))
			.setShowWhen(false)
			.setContentText(message)
			.setStyle(new NotificationCompat.BigTextStyle().bigText(message))
			.setPriority(NotificationCompat.PRIORITY_DEFAULT)
			.setContentIntent(pendingIntent)
			.setAutoCancel(true);

		NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
		notificationManager.notify(id, builder.build());

		return false;
	}

	/**
	 * Called by the system when the job is running but the conditions are no longer met.
	 *
	 * @param jobParameters information about the job
	 * @return flag indicating whether the job needs rescheduling
	 */
	@Override
	public boolean onStopJob(JobParameters jobParameters)
	{
		return true;
	}

	private void createNotificationChannel(Context context)
	{
		// Create the NotificationChannel, but only on API 26+
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
		{
			CharSequence name = context.getString(R.string.channel_name);
			String description = context.getString(R.string.channel_description);

			NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name,
				NotificationManager.IMPORTANCE_DEFAULT);
			channel.setDescription(description);

			// Register the channel with the system; you can't change the importance
			// or other notification behaviors after this
			NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
			if (notificationManager != null)
				notificationManager.createNotificationChannel(channel);
		}
	}

	private static void scheduleJob(@NonNull JobScheduler scheduler, @NonNull ComponentName serviceName,
		int id, @NonNull String message, long delayBy)
	{
		PersistableBundle extras = new PersistableBundle(2);
		extras.putInt(KEY_NOTIFICATION_ID, id);
		extras.putString(KEY_NOTIFICATION_MESSAGE, message);

		JobInfo.Builder builder = new JobInfo.Builder(id, serviceName)
			// this job should be delayed by the provided amount of time
			.setMinimumLatency(delayBy)
			// persist this job across device reboots
			.setPersisted(true)
			.setExtras(extras);

		int result = scheduler.schedule(builder.build());

		if (result == JobScheduler.RESULT_FAILURE && Utils.DEBUG)
			Utils.d(LOG_TAG, "failed to schedule id %d", id);
	}

	private static boolean scheduleJob(@NonNull JobScheduler scheduler, @NonNull ComponentName serviceName,
		@NonNull CalendarEvent event, long now)
	{
		boolean scheduled = false;

		// setup calendar for event date at 10 AM
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.setTime(event.getEventDate());
		calendar.set(Calendar.HOUR, 10);
		calendar.set(Calendar.MINUTE, 0);

		if (Utils.DEBUG)
		{
			Utils.d(LOG_TAG, "Event id:%s (%d) delta:%s %s", event.getReminderIdString(),
				event.getReminderId(), event.getReminderDelta(), event.getEventDescription());
			Utils.d(LOG_TAG, "  event  date %s", dateFormat.format(calendar.getTime()));
		}

		// calculate how long to delay notification from now
		long delta = TimeUnit.MILLISECONDS.convert(event.getReminderDelta(), TimeUnit.DAYS);
		long delayBy = calendar.getTimeInMillis() - now + delta;
		if (delayBy > 0)
		{
			scheduleJob(scheduler, serviceName, event.getReminderId(),
				event.getReminderMessage(), delayBy);

			scheduled = true;
		}

		if (Utils.DEBUG)
		{
			Utils.d(LOG_TAG, "  notify date %s",
				scheduled ? dateFormat.format(now + delayBy) : "<not scheduled>");
		}

		return scheduled;
	}

	/**
	 * Cancels all pending jobs and reschedules all current calendar events.
	 *
	 * @param context used to retrieve job scheduler resources
	 * @param events list of events to schedule for notification
	 */
	public static void scheduleNotifications(@NonNull Context context, @Nullable List<CalendarEvent> events)
	{
		if (events == null || events.isEmpty())
			return;

		JobScheduler scheduler = (JobScheduler)context.getSystemService(JOB_SCHEDULER_SERVICE);
		if (scheduler == null)
			return;

		ComponentName serviceName = new ComponentName(context.getPackageName(), NotificationJobService.class.getName());

		if (Utils.DEBUG)
			Utils.d(LOG_TAG, "Cancel and reschedule all jobs");

		// cancel all existing jobs
		scheduler.cancelAll();

		// get the time in milliseconds now
		long now = Calendar.getInstance().getTimeInMillis();

		for (CalendarEvent event : events)
		{
			if (event.hasReminderInfo())
				scheduleJob(scheduler, serviceName, event, now);
		}
	}

	/**
	 * Schedules a single calendar event.
	 *
	 * @param context used to retrieve job scheduler resources
	 * @param event event to schedule for notification
	 */
	public static void scheduleNotification(@NonNull Context context, @Nullable CalendarEvent event)
	{
		if (event == null || !event.hasReminderInfo())
			return;

		JobScheduler scheduler = (JobScheduler)context.getSystemService(JOB_SCHEDULER_SERVICE);
		if (scheduler == null)
			return;

		ComponentName serviceName = new ComponentName(context.getPackageName(), NotificationJobService.class.getName());

		if (Utils.DEBUG)
			Utils.d(LOG_TAG, "Reschedule single job");

		// get the time in milliseconds now
		long now = Calendar.getInstance().getTimeInMillis();

		if (!scheduleJob(scheduler, serviceName, event, now))
		{
			// not scheduled? try removing the notification in case the date is past
			scheduler.cancel(event.getReminderId());

			if (Utils.DEBUG)
				Utils.d(LOG_TAG, "Job cancelled");
		}
	}

	public static void testOnlyScheduleNotification(@NonNull Context context)
	{
		JobScheduler scheduler = (JobScheduler)context.getSystemService(JOB_SCHEDULER_SERVICE);
		if (scheduler == null)
			return;

		ComponentName serviceName = new ComponentName(context.getPackageName(), NotificationJobService.class.getName());

		scheduleJob(scheduler, serviceName, 9999,
			"Here is a test message that displays in 30 seconds!",
			(30 * 1000));
	}

	public static void testOnlyLogAllNotificationJobs(@NonNull Context context)
	{
		if (!Utils.DEBUG)
			return;

		JobScheduler scheduler = (JobScheduler)context.getSystemService(JOB_SCHEDULER_SERVICE);
		if (scheduler == null)
			return;

		List<JobInfo> list = scheduler.getAllPendingJobs();
		Utils.d(LOG_TAG, "testOnlyLogAllNotificationJobs count %d", list.size());

		// get the time in milliseconds now
		Calendar calendar = Calendar.getInstance();
		long now = calendar.getTimeInMillis();

		for (JobInfo jobInfo : list)
		{
			PersistableBundle extras = jobInfo.getExtras();
			final int id = extras.getInt(KEY_NOTIFICATION_ID);
			final String message = extras.getString(KEY_NOTIFICATION_MESSAGE);

			Utils.d(LOG_TAG, "id:%d when:%s extra_id:%d %s",
				jobInfo.getId(),
				DateUtils.getRelativeTimeSpanString(now + jobInfo.getMinLatencyMillis()),
				id, message);
		}
	}
}

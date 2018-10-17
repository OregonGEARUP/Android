package org.oregongoestocollege.itsaplan;

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
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;

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
		// Create the NotificationChannel, but only on API 26+ because
		// the NotificationChannel class is new and not in the support library
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

	public static void scheduleNotification(@NonNull Context context, int id, @NonNull String message,
		long delayBy)
	{
		if (id <= 0 || TextUtils.isEmpty(message))
			return;

		JobScheduler scheduler = (JobScheduler)context.getSystemService(JOB_SCHEDULER_SERVICE);
		if (scheduler == null)
			return;

		ComponentName serviceName = new ComponentName(context.getPackageName(), NotificationJobService.class.getName());

		PersistableBundle extras = new PersistableBundle(2);
		extras.putInt(KEY_NOTIFICATION_ID, id);
		extras.putString(KEY_NOTIFICATION_MESSAGE, message);

		JobInfo.Builder builder = new JobInfo.Builder(id, serviceName)
			// this job should be delayed by the provided amount of time
			.setMinimumLatency(delayBy)
			// persist this job across device reboots
			.setPersisted(true)
			.setExtras(extras);

		scheduler.schedule(builder.build());
	}
}

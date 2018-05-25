package org.oregongoestocollege.itsaplan.viewmodel;

import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;

import org.oregongoestocollege.itsaplan.R;
import org.oregongoestocollege.itsaplan.Utils;
import org.oregongoestocollege.itsaplan.data.MyPlanRepository;
import org.oregongoestocollege.itsaplan.data.Scholarship;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class ScholarshipViewModel implements BindingItem
{
	public static final String LOG_TAG = "GearUpScholarship";
	private MyPlanRepository repository;
	private final Scholarship scholarship;
	private final boolean removable;
	// UX fields
	private boolean initialized = false;
	private Date applicationDate;
	public final ObservableField<String> name = new ObservableField<>();
	public final ObservableField<String> applicationDateText = new ObservableField<>();
	public final ObservableField<String> website = new ObservableField<>();
	public final ObservableField<String> otherInfo = new ObservableField<>();
	public final ObservableBoolean essayDone = new ObservableBoolean();
	public final ObservableBoolean recommendationsDone = new ObservableBoolean();
	public final ObservableBoolean activitiesChartDone = new ObservableBoolean();
	public final ObservableBoolean testsDone = new ObservableBoolean();
	public final ObservableBoolean applicationDone = new ObservableBoolean();

	public ScholarshipViewModel(@NonNull MyPlanRepository repository, @NonNull Scholarship scholarship, boolean removable)
	{
		this.repository = checkNotNull(repository);
		this.scholarship = checkNotNull(scholarship);
		this.removable = removable;

		this.applicationDate = scholarship.getApplicationDate();
	}

	private boolean isDirty()
	{
		boolean dateOk = (applicationDate == null && scholarship.getApplicationDate() == null) ||
			(applicationDate != null && applicationDate.equals(scholarship.getApplicationDate()));

		return !dateOk ||
			!TextUtils.equals(name.get(), scholarship.getName()) ||
			!TextUtils.equals(website.get(), scholarship.getWebsite()) ||
			!TextUtils.equals(otherInfo.get(), scholarship.getOtherInfo()) ||
			essayDone.get() != scholarship.isEssayDone() ||
			recommendationsDone.get() != scholarship.isRecommendationsDone() ||
			activitiesChartDone.get() != scholarship.isActivitiesChartDone() ||
			testsDone.get() != scholarship.isTestsDone() ||
			applicationDone.get() != scholarship.isApplicationDone();
	}

	private void remove()
	{
		repository.delete(scholarship);
	}

	@Override
	public int getLayoutId()
	{
		return R.layout.item_scholarship;
	}

	@Override
	public void onBind(@NonNull Context context)
	{
		if (initialized)
			return;

		// use local value we set in constructor / picker
		if (applicationDate != null)
			applicationDateText.set(DateFormat.getLongDateFormat(context).format(applicationDate));

		name.set(scholarship.getName());
		website.set(scholarship.getWebsite());
		otherInfo.set(scholarship.getOtherInfo());
		essayDone.set(scholarship.isEssayDone());
		recommendationsDone.set(scholarship.isRecommendationsDone());
		activitiesChartDone.set(scholarship.isActivitiesChartDone());
		testsDone.set(scholarship.isTestsDone());
		applicationDone.set(scholarship.isApplicationDone());

		initialized = true;
	}

	public void update()
	{
		if (!isDirty())
			return;

		Utils.d(LOG_TAG, "Saving %s to database", name.get());

		scholarship.setName(name.get());
		scholarship.setApplicationDate(applicationDate);
		scholarship.setWebsite(website.get());
		scholarship.setOtherInfo(otherInfo.get());
		scholarship.setEssayDone(essayDone.get());
		scholarship.setRecommendationsDone(recommendationsDone.get());
		scholarship.setActivitiesChartDone(activitiesChartDone.get());
		scholarship.setTestsDone(testsDone.get());
		scholarship.setApplicationDone(applicationDone.get());

		repository.update(scholarship);
	}

	public boolean isRemovable()
	{
		return removable;
	}

	public void onRemoveScholarship(View view)
	{
		if (!removable)
			return;

		final Context context = view.getContext();

		new AlertDialog.Builder(context)
			.setTitle(context.getString(R.string.remove_scholarship))
			.setMessage(context.getString(R.string.remove_scholarship_confirm, scholarship.getName()))
			.setPositiveButton(context.getString(R.string.remove), new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int whichButton)
				{
					remove();
				}
			})
			.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int whichButton)
				{
				}
			})
			.show();
	}

	public void onChangeApplicationDate(View view)
	{
		final Context context = view.getContext();

		final Calendar c = Calendar.getInstance();
		Date initialDate = scholarship.getApplicationDate();
		if (initialDate != null)
			c.setTime(initialDate);

		DatePickerDialog datePickerDialog = new DatePickerDialog(context,
			new DatePickerDialog.OnDateSetListener()
			{
				@Override
				public void onDateSet(DatePicker view, int year, int month, int day)
				{
					final Calendar calendar = Calendar.getInstance();
					calendar.clear();
					calendar.set(year, month, day);

					// update UX
					applicationDate = calendar.getTime();
					applicationDateText.set(DateFormat.getLongDateFormat(context).format(applicationDate));
				}
			},
			c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

		datePickerDialog.show();
	}
}

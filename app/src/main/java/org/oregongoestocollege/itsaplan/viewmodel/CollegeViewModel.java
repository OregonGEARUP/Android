package org.oregongoestocollege.itsaplan.viewmodel;

import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableDouble;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;

import org.oregongoestocollege.itsaplan.R;
import org.oregongoestocollege.itsaplan.Utils;
import org.oregongoestocollege.itsaplan.data.College;
import org.oregongoestocollege.itsaplan.data.MyPlanRepository;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class CollegeViewModel implements BindingItem
{
	public static final String LOG_TAG = "GearUpCollege";
	private MyPlanRepository repository;
	private final College college;
	private final boolean removable;
	// UX fields
	private boolean initialized = false;
	private Date applicationDate;
	public final ObservableField<String> name = new ObservableField<>();
	public final ObservableField<String> applicationDateText = new ObservableField<>();
	public final ObservableDouble averageNetPrice = new ObservableDouble();
	public final ObservableDouble applicationCost = new ObservableDouble();
	public final ObservableBoolean essayDone = new ObservableBoolean();
	public final ObservableBoolean recommendationsDone = new ObservableBoolean();
	public final ObservableBoolean activitiesChartDone = new ObservableBoolean();
	public final ObservableBoolean testsDone = new ObservableBoolean();
	public final ObservableBoolean addlFinancialAidDone = new ObservableBoolean();
	public final ObservableBoolean addlScholarshipDone = new ObservableBoolean();
	public final ObservableBoolean feeDeferralDone = new ObservableBoolean();
	public final ObservableBoolean applicationDone = new ObservableBoolean();

	public CollegeViewModel(@NonNull MyPlanRepository repository, @NonNull College college, boolean removable)
	{
		this.repository = checkNotNull(repository);
		this.college = checkNotNull(college);
		this.removable = removable;

		this.applicationDate = college.getApplicationDate();
	}

	private boolean isDirty()
	{
		boolean dateOk = (applicationDate == null && college.getApplicationDate() == null) ||
			(applicationDate != null && applicationDate.equals(college.getApplicationDate()));

		return !dateOk ||
			!TextUtils.equals(name.get(), college.getName()) ||
			averageNetPrice.get() != college.getAverageNetPrice() ||
			applicationCost.get() != college.getApplicationCost() ||
			essayDone.get() != college.isEssayDone() ||
			recommendationsDone.get() != college.isRecommendationsDone() ||
			activitiesChartDone.get() != college.isActivitiesChartDone() ||
			addlFinancialAidDone.get() != college.isAddlFinancialAidDone() ||
			addlScholarshipDone.get() != college.isAddlScholarshipDone() ||
			feeDeferralDone.get() != college.isFeeDeferralDone() ||
			applicationDone.get() != college.isApplicationDone();
	}

	@Override
	public int getLayoutId()
	{
		return R.layout.item_college;
	}

	@Override
	public void onBind(@NonNull Context context)
	{
		if (initialized)
			return;

		// use local value we set in constructor / picker
		if (applicationDate != null)
			applicationDateText.set(DateFormat.getLongDateFormat(context).format(applicationDate));

		name.set(college.getName());
		averageNetPrice.set(college.getAverageNetPrice());
		applicationCost.set(college.getApplicationCost());
		essayDone.set(college.isEssayDone());
		recommendationsDone.set(college.isRecommendationsDone());
		activitiesChartDone.set(college.isActivitiesChartDone());
		testsDone.set(college.isTestsDone());
		addlFinancialAidDone.set(college.isAddlFinancialAidDone());
		addlScholarshipDone.set(college.isAddlScholarshipDone());
		feeDeferralDone.set(college.isFeeDeferralDone());
		applicationDone.set(college.isApplicationDone());

		initialized = true;
	}

	public void update()
	{
		if (!isDirty())
			return;

		Utils.d(LOG_TAG, "Saving %s to database", name.get());

		college.setName(name.get());
		college.setApplicationDate(applicationDate);
		college.setApplicationCost(applicationCost.get());
		college.setEssayDone(essayDone.get());
		college.setRecommendationsDone(recommendationsDone.get());
		college.setActivitiesChartDone(activitiesChartDone.get());
		college.setAddlFinancialAidDone(addlFinancialAidDone.get());
		college.setAddlScholarshipDone(addlScholarshipDone.get());
		college.setFeeDeferralDone(feeDeferralDone.get());
		college.setApplicationDone(applicationDone.get());
		college.setEssayDone(essayDone.get());

		repository.update(college);
	}

	public void remove()
	{
		repository.delete(college);
	}

	public boolean isRemovable()
	{
		return removable;
	}

	public void onRemoveCollege(View view)
	{
		if (!removable)
			return;

		final Context context = view.getContext();

		new AlertDialog.Builder(context)
			.setTitle(context.getString(R.string.remove_college))
			.setMessage(context.getString(R.string.remove_college_confirm, college.getName()))
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
		Date initialDate = college.getApplicationDate();
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

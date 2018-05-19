package org.oregongoestocollege.itsaplan.viewmodel;

import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.content.Context;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;

import org.oregongoestocollege.itsaplan.R;
import org.oregongoestocollege.itsaplan.Utils;
import org.oregongoestocollege.itsaplan.data.MyPlanRepository;
import org.oregongoestocollege.itsaplan.data.TestResult;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class TestResultViewModel implements BindingItem
{
	public static final String LOG_TAG = "GearUpTestResult";
	private MyPlanRepository repository;
	private TestResult testResult;
	// UX fields
	private boolean initialized = false;
	private Date testDate;
	public final ObservableField<String> testDateText = new ObservableField<>();
	public final ObservableField<String> composite = new ObservableField<>();
	public final ObservableField<String> math = new ObservableField<>();
	public final ObservableField<String> science = new ObservableField<>();
	public final ObservableField<String> reading = new ObservableField<>();
	public final ObservableField<String> writing = new ObservableField<>();

	public TestResultViewModel(@NonNull MyPlanRepository repository)
	{
		this.repository = checkNotNull(repository);
	}

	private boolean isDirty()
	{
		if (testResult == null)
			return false;

		boolean dateEqual = (testDate == null && testResult.getDate() == null) ||
			(testDate != null && testDate.equals(testResult.getDate()));

		return !dateEqual ||
			TextUtils.equals(composite.get(), testResult.getComposite()) ||
			TextUtils.equals(math.get(), testResult.getMath()) ||
			TextUtils.equals(science.get(), testResult.getScience()) ||
			TextUtils.equals(reading.get(), testResult.getReading()) ||
			TextUtils.equals(writing.get(), testResult.getWriting());
	}

	public void setTestResult(@NonNull TestResult testResult)
	{
		this.testResult = checkNotNull(testResult);

		this.testDate = testResult.getDate();
	}

	@Override
	public int getLayoutId()
	{
		return R.layout.item_college;
	}

	@Override
	public void onBind(@NonNull Context context)
	{
		if (initialized || testResult == null)
			return;

		// use local value we set in constructor / picker
		if (testDate != null)
			testDateText.set(DateFormat.getLongDateFormat(context).format(testDate));

		composite.set(testResult.getComposite());
		math.set(testResult.getMath());
		science.set(testResult.getScience());
		reading.set(testResult.getReading());
		writing.set(testResult.getWriting());

		initialized = true;
	}

	public void update()
	{
		if (!isDirty())
			return;

		Utils.d(LOG_TAG, "Saving %s to database", testResult.getName());

		testResult.setComposite(composite.get());
		testResult.setMath(math.get());
		testResult.setScience(science.get());
		testResult.setReading(reading.get());
		testResult.setWriting(writing.get());

		repository.update(testResult);
	}

	public void onChangeTestDate(View view)
	{
		if (testResult == null)
			return;

		final Context context = view.getContext();

		final Calendar c = Calendar.getInstance();
		Date initialDate = testResult.getDate();
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
					testDate = calendar.getTime();
					testDateText.set(DateFormat.getLongDateFormat(context).format(testDate));
				}
			},
			c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

		datePickerDialog.show();
	}
}

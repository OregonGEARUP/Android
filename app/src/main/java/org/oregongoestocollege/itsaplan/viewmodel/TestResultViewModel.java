package org.oregongoestocollege.itsaplan.viewmodel;

import android.content.Context;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.TextUtils;

import org.oregongoestocollege.itsaplan.Utils;
import org.oregongoestocollege.itsaplan.data.MyPlanRepository;
import org.oregongoestocollege.itsaplan.data.TestResult;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class TestResultViewModel
{
	private static final String LOG_TAG = "GearUp_TestResultViewModel";
	// UX fields
	public final ObservableField<DateViewModel> testDateVm = new ObservableField<>();
	public final ObservableField<String> composite = new ObservableField<>();
	public final ObservableField<String> math = new ObservableField<>();
	public final ObservableField<String> science = new ObservableField<>();
	public final ObservableField<String> reading = new ObservableField<>();
	public final ObservableField<String> writing = new ObservableField<>();

	public TestResultViewModel()
	{
	}

	private boolean isDirty(TestResult value)
	{
		// compare against the original data value
		if (value == null)
			return false;

		return testDateVm.get().isDirty() ||
			!TextUtils.equals(composite.get(), value.getComposite()) ||
			!TextUtils.equals(math.get(), value.getMath()) ||
			!TextUtils.equals(science.get(), value.getScience()) ||
			!TextUtils.equals(reading.get(), value.getReading()) ||
			!TextUtils.equals(writing.get(), value.getWriting());
	}

	public void setTestResult(@NonNull Context context, TestResult testResult, @StringRes int placeholderStringResId)
	{
		testDateVm.set(DateViewModel.build(context, testResult.getDate(), placeholderStringResId));

		composite.set(testResult.getComposite());
		math.set(testResult.getMath());
		science.set(testResult.getScience());
		reading.set(testResult.getReading());
		writing.set(testResult.getWriting());
	}

	public boolean isNotificationDirty()
	{
		return testDateVm.get().isDirty();
	}

	public void update(@NonNull MyPlanRepository repository, TestResult value)
	{
		if (!isDirty(value))
			return;

		if (Utils.DEBUG)
			Utils.d(LOG_TAG, "Saving TestResult %s to database", value.getName());

		value.setDate(testDateVm.get().getSelectedDate());
		value.setComposite(composite.get());
		value.setMath(math.get());
		value.setScience(science.get());
		value.setReading(reading.get());
		value.setWriting(writing.get());

		testDateVm.get().saved();

		repository.update(value);
	}
}

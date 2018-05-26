package org.oregongoestocollege.itsaplan.viewmodel;

import android.content.Context;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.text.TextUtils;

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
	public final ObservableField<DateViewModel> testDateVm = new ObservableField<>();
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

		return testDateVm.get().isDirty() ||
			TextUtils.equals(composite.get(), testResult.getComposite()) ||
			TextUtils.equals(math.get(), testResult.getMath()) ||
			TextUtils.equals(science.get(), testResult.getScience()) ||
			TextUtils.equals(reading.get(), testResult.getReading()) ||
			TextUtils.equals(writing.get(), testResult.getWriting());
	}

	public void setTestResult(@NonNull TestResult testResult)
	{
		this.testResult = checkNotNull(testResult);

		testDateVm.set(new DateViewModel(testResult.getDate()));
	}

	@Override
	public int getLayoutId()
	{
		return 0;
	}

	@Override
	public void onBind(@NonNull Context context)
	{
		if (initialized || testResult == null)
			return;

		testDateVm.get().onBind(context);

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

		testResult.setDate(testDateVm.get().getSelectedDate());
		testResult.setComposite(composite.get());
		testResult.setMath(math.get());
		testResult.setScience(science.get());
		testResult.setReading(reading.get());
		testResult.setWriting(writing.get());

		repository.update(testResult);
	}
}

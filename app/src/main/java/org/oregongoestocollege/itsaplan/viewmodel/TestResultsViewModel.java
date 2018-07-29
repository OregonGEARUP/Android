package org.oregongoestocollege.itsaplan.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import org.oregongoestocollege.itsaplan.data.MyPlanRepository;
import org.oregongoestocollege.itsaplan.data.TestResult;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class TestResultsViewModel extends AndroidViewModel
{
	private MyPlanRepository repository;
	private LiveData<TestResult> actTestResultData;
	private LiveData<TestResult> satTestResultData;
	// UX fields
	public final ObservableField<TestResultViewModel> actVm = new ObservableField<>();
	public final ObservableField<TestResultViewModel> satVm = new ObservableField<>();

	public TestResultsViewModel(@NonNull Application application)
	{
		super(application);

		this.repository = MyPlanRepository.getInstance(application);
		actTestResultData = repository.getActTestResult();
		satTestResultData = repository.getSatTestResult();
	}

	public LiveData<TestResult> getActTestResultData()
	{
		return actTestResultData;
	}

	public LiveData<TestResult> getSatTestResultData()
	{
		return satTestResultData;
	}

	public void setActTestResult(@NonNull Context context, TestResult testResult)
	{
		if (actVm.get() == null)
		{
			TestResultViewModel viewModel = new TestResultViewModel();
			viewModel.setTestResult(context, testResult);
			actVm.set(viewModel);
		}
		else
			actVm.get().setTestResult(context, testResult);
	}

	public void setSatTestResult(@NonNull Context context, TestResult testResult)
	{
		if (satVm.get() == null)
		{
			TestResultViewModel viewModel = new TestResultViewModel();
			viewModel.setTestResult(context, testResult);
			satVm.set(viewModel);
		}
		else
			satVm.get().setTestResult(context, testResult);
	}

	public void update()
	{
		TestResultViewModel viewModel = actVm.get();
		if (viewModel != null)
			viewModel.update(repository, actTestResultData.getValue());
		viewModel = satVm.get();
		if (viewModel != null)
			viewModel.update(repository, satTestResultData.getValue());
	}
}

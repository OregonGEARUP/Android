package org.oregongoestocollege.itsaplan.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import org.oregongoestocollege.itsaplan.data.MyPlanRepository;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class TestResultsViewModel extends AndroidViewModel
{
	private MyPlanRepository repository;
	public final TestResultViewModel actVm;
	public final TestResultViewModel satVm;

	public TestResultsViewModel(@NonNull Application application)
	{
		super(application);

		repository = new MyPlanRepository(application);
		actVm = new TestResultViewModel(repository);
		satVm = new TestResultViewModel(repository);
	}
}

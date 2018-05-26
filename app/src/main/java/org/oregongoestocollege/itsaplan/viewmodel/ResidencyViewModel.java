package org.oregongoestocollege.itsaplan.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.oregongoestocollege.itsaplan.Utils;
import org.oregongoestocollege.itsaplan.data.MyPlanDatabase;
import org.oregongoestocollege.itsaplan.data.MyPlanRepository;
import org.oregongoestocollege.itsaplan.data.Residency;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class ResidencyViewModel extends AndroidViewModel
{
	public static final String LOG_TAG = "GearUpResidency";
	private MyPlanRepository repository;
	private final LiveData<Residency> residency;
	// UX fields
	public final ObservableField<String> nameEmployer1 = new ObservableField<>();
	public final ObservableField<String> cityEmployer1 = new ObservableField<>();
	public final ObservableField<DateViewModel> startEmployer1DateVm = new ObservableField<>();
	public final ObservableField<DateViewModel> endEmployer1DateVm = new ObservableField<>();

	public ResidencyViewModel(@NonNull Application application, @NonNull MyPlanRepository repository)
	{
		super(application);

		this.repository = repository;
		this.residency = repository.getResidency();

		Residency residency = getResidency().getValue();
		startEmployer1DateVm.set(new DateViewModel(residency.getStartEmployer1()));
		endEmployer1DateVm.set(new DateViewModel(residency.getEndEmployer1()));
	}

	private boolean isDirty()
	{
		Residency residency = getResidency().getValue();
		if (residency == null)
			return false;

		return !TextUtils.equals(nameEmployer1.get(), residency.getNameEmployer1()) ||
			!TextUtils.equals(cityEmployer1.get(), residency.getCityEmployer1()) ||
			startEmployer1DateVm.get().isDirty() ||
			endEmployer1DateVm.get().isDirty();
	}

	public LiveData<Residency> getResidency()
	{
		return residency;
	}

	public void residencyChanged(@NonNull Context context)
	{
		Residency residency = getResidency().getValue();
		if (residency == null)
			return;

		if (startEmployer1DateVm == null)
		{

		}

		nameEmployer1.set(residency.getNameEmployer1());
		cityEmployer1.set(residency.getCityEmployer1());
		if (startEmployer1DateVm == null)
			startEmployer1DateVm.set(new DateViewModel(residency.getStartEmployer1()));
		else if (context != null)
			startEmployer1DateVm.get().onBind(context);
		if (endEmployer1DateVm == null)
			endEmployer1DateVm.set(new DateViewModel(residency.getEndEmployer1()));
		else if (context != null)
			endEmployer1DateVm.get().onBind(context);
	}

	public void update()
	{
		if (!isDirty())
			return;

		Utils.d(LOG_TAG, "Saving Residency to database");

		Residency residency = getResidency().getValue();
		residency.setNameEmployer1(nameEmployer1.get());
		residency.setCityEmployer1(cityEmployer1.get());

		repository.update(residency);
	}

	/**
	 * TODO Not really needed, could get the DB using the application parameter, leaving for now.
	 */
	public static class Factory extends ViewModelProvider.NewInstanceFactory
	{
		@NonNull
		private final Application application;
		private final MyPlanRepository repository;

		public Factory(@NonNull Application application)
		{
			this.application = application;
			this.repository = MyPlanRepository.getInstance(MyPlanDatabase.getDatabase(application));
		}

		@NonNull
		@Override
		public <T extends ViewModel> T create(@NonNull Class<T> modelClass)
		{
			//noinspection unchecked
			return (T)new ResidencyViewModel(application, repository);
		}
	}
}

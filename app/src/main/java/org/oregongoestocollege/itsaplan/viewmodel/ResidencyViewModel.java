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
	private final LiveData<Residency> residencyData;
	// UX fields
	public final ObservableField<DateViewModel> residencyStartDateVm = new ObservableField<>();
	public final ObservableField<DateViewModel> residencyEndDateVm = new ObservableField<>();
	public final ObservableField<DateViewModel> parentResidencyStartDateVm = new ObservableField<>();
	public final ObservableField<DateViewModel> parentResidencyEndDateVm = new ObservableField<>();
	public final ObservableField<DateViewModel> registerToVoteDateVm = new ObservableField<>();
	public final ObservableField<DateViewModel> parentsRegisterToVoteDateVm = new ObservableField<>();
	public final ObservableField<DateViewModel> militaryServiceStartDateVm = new ObservableField<>();
	public final ObservableField<DateViewModel> militaryServiceEndDateVm = new ObservableField<>();
	public final ObservableField<DateViewModel> parentMilitaryServiceStartDateVm = new ObservableField<>();
	public final ObservableField<DateViewModel> parentMilitaryServiceEndDateVm = new ObservableField<>();
	public final ObservableField<DateViewModel> fileOregonTaxesYear1DateVm = new ObservableField<>();
	public final ObservableField<DateViewModel> fileOregonTaxesYear2DateVm = new ObservableField<>();
	public final ObservableField<DateViewModel> parentsFileOregonTaxesYear1DateVm = new ObservableField<>();
	public final ObservableField<DateViewModel> parentsFileOregonTaxesYear2DateVm = new ObservableField<>();
	public final ObservableField<String> nameEmployer1 = new ObservableField<>();
	public final ObservableField<String> cityEmployer1 = new ObservableField<>();
	public final ObservableField<DateViewModel> startEmployer1DateVm = new ObservableField<>();
	public final ObservableField<DateViewModel> endEmployer1DateVm = new ObservableField<>();

	public ResidencyViewModel(@NonNull Application application, @NonNull MyPlanRepository repository)
	{
		super(application);

		this.repository = repository;
		this.residencyData = repository.getResidency();
	}

	private boolean isDirty()
	{
		// make sure we have the original residency data
		Residency oldResidency = residencyData.getValue();
		if (oldResidency == null)
			return false;

		// compare the residency data against the user entry fields
		return residencyStartDateVm.get().isDirty() ||
			residencyEndDateVm.get().isDirty() ||
			parentResidencyStartDateVm.get().isDirty() ||
			parentResidencyEndDateVm.get().isDirty() ||
			registerToVoteDateVm.get().isDirty() ||
			parentsRegisterToVoteDateVm.get().isDirty() ||
			militaryServiceStartDateVm.get().isDirty() ||
			militaryServiceEndDateVm.get().isDirty() ||
			parentMilitaryServiceStartDateVm.get().isDirty() ||
			parentMilitaryServiceEndDateVm.get().isDirty() ||
			!TextUtils.equals(nameEmployer1.get(), oldResidency.getNameEmployer1()) ||
			!TextUtils.equals(cityEmployer1.get(), oldResidency.getCityEmployer1()) ||
			startEmployer1DateVm.get().isDirty() ||
			endEmployer1DateVm.get().isDirty();
	}

	public LiveData<Residency> getResidencyData()
	{
		return residencyData;
	}

	public void setResidency(@NonNull Context context, Residency residency)
	{
		residencyStartDateVm.set(new DateViewModel(context, residency.getResidencyStart()));
		residencyEndDateVm.set(new DateViewModel(context, residency.getResidencyEnd()));
		parentResidencyStartDateVm.set(new DateViewModel(context, residency.getParentResidencyStart()));
		parentResidencyEndDateVm.set(new DateViewModel(context, residency.getParentResidencyEnd()));
		registerToVoteDateVm.set(new DateViewModel(context, residency.getRegisterToVote()));
		parentsRegisterToVoteDateVm.set(new DateViewModel(context, residency.getParentsRegisterToVote()));
		militaryServiceStartDateVm.set(new DateViewModel(context, residency.getMilitaryServiceStart()));
		militaryServiceEndDateVm.set(new DateViewModel(context, residency.getMilitaryServiceEnd()));
		parentMilitaryServiceStartDateVm.set(new DateViewModel(context, residency.getParentMilitaryServiceStart()));
		parentMilitaryServiceEndDateVm.set(new DateViewModel(context, residency.getParentMilitaryServiceEnd()));
		fileOregonTaxesYear1DateVm.set(new DateViewModel(context, residency.getFileOregonTaxesYear1()));
		fileOregonTaxesYear2DateVm.set(new DateViewModel(context, residency.getFileOregonTaxesYear2()));
		parentsFileOregonTaxesYear1DateVm.set(new DateViewModel(context, residency.getParentsFileOregonTaxesYear1()));
		parentsFileOregonTaxesYear2DateVm.set(new DateViewModel(context, residency.getParentsFileOregonTaxesYear2()));
		nameEmployer1.set(residency.getNameEmployer1());
		cityEmployer1.set(residency.getCityEmployer1());
		startEmployer1DateVm.set(new DateViewModel(context, residency.getStartEmployer1()));
		endEmployer1DateVm.set(new DateViewModel(context, residency.getEndEmployer1()));
	}

	public void update()
	{
		if (!isDirty())
			return;

		Utils.d(LOG_TAG, "Saving Residency to database");

		Residency newResidency = residencyData.getValue();

		newResidency.setResidencyStart(residencyStartDateVm.get().getSelectedDate());
		newResidency.setResidencyEnd(residencyEndDateVm.get().getSelectedDate());
		newResidency.setParentResidencyStart(parentResidencyStartDateVm.get().getSelectedDate());
		newResidency.setParentResidencyEnd(parentResidencyEndDateVm.get().getSelectedDate());
		newResidency.setRegisterToVote(registerToVoteDateVm.get().getSelectedDate());
		newResidency.setParentsRegisterToVote(parentsRegisterToVoteDateVm.get().getSelectedDate());
		newResidency.setMilitaryServiceStart(militaryServiceStartDateVm.get().getSelectedDate());
		newResidency.setMilitaryServiceEnd(militaryServiceEndDateVm.get().getSelectedDate());
		newResidency.setParentMilitaryServiceStart(parentMilitaryServiceStartDateVm.get().getSelectedDate());
		newResidency.setParentMilitaryServiceEnd(parentMilitaryServiceEndDateVm.get().getSelectedDate());
		newResidency.setFileOregonTaxesYear1(fileOregonTaxesYear1DateVm.get().getSelectedDate());
		newResidency.setFileOregonTaxesYear2(fileOregonTaxesYear2DateVm.get().getSelectedDate());
		newResidency.setParentsFileOregonTaxesYear1(parentsFileOregonTaxesYear1DateVm.get().getSelectedDate());
		newResidency.setParentsFileOregonTaxesYear2(parentsFileOregonTaxesYear2DateVm.get().getSelectedDate());
		newResidency.setNameEmployer1(nameEmployer1.get());
		newResidency.setCityEmployer1(cityEmployer1.get());
		newResidency.setStartEmployer1(startEmployer1DateVm.get().getSelectedDate());
		newResidency.setEndEmployer1(endEmployer1DateVm.get().getSelectedDate());

		repository.update(newResidency);
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

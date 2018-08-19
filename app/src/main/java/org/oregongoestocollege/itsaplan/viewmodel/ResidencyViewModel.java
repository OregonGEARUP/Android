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

import org.oregongoestocollege.itsaplan.R;
import org.oregongoestocollege.itsaplan.Utils;
import org.oregongoestocollege.itsaplan.data.MyPlanRepository;
import org.oregongoestocollege.itsaplan.data.Residency;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class ResidencyViewModel extends AndroidViewModel
{
	private static final String LOG_TAG = "GearUp_ResidencyViewModel";
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
		// compare against the original data value
		Residency value = residencyData.getValue();
		if (value == null)
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
			!TextUtils.equals(nameEmployer1.get(), value.getNameEmployer1()) ||
			!TextUtils.equals(cityEmployer1.get(), value.getCityEmployer1()) ||
			startEmployer1DateVm.get().isDirty() ||
			endEmployer1DateVm.get().isDirty();
	}

	public LiveData<Residency> getResidencyData()
	{
		return residencyData;
	}

	public void setResidency(@NonNull Context context, Residency residency)
	{
		residencyStartDateVm.set(DateViewModel.build(context, residency.getResidencyStart(), R.string.residency_start_date));
		residencyEndDateVm.set(DateViewModel.build(context, residency.getResidencyEnd(), R.string.residency_end_date));
		parentResidencyStartDateVm.set(DateViewModel.build(context, residency.getParentResidencyStart(), R.string.residency_start_date));
		parentResidencyEndDateVm.set(DateViewModel.build(context, residency.getParentResidencyEnd(), R.string.residency_end_date));
		registerToVoteDateVm.set(DateViewModel.build(context, residency.getRegisterToVote(), R.string.voter_registration_date));
		parentsRegisterToVoteDateVm.set(DateViewModel.build(context, residency.getParentsRegisterToVote(), R.string.voter_registration_date));
		militaryServiceStartDateVm.set(DateViewModel.build(context, residency.getMilitaryServiceStart(), R.string.military_start_date));
		militaryServiceEndDateVm.set(DateViewModel.build(context, residency.getMilitaryServiceEnd(), R.string.military_end_date));
		parentMilitaryServiceStartDateVm.set(DateViewModel.build(context, residency.getParentMilitaryServiceStart(), R.string.military_start_date));
		parentMilitaryServiceEndDateVm.set(DateViewModel.build(context, residency.getParentMilitaryServiceEnd(), R.string.military_end_date));
		fileOregonTaxesYear1DateVm.set(DateViewModel.build(context, residency.getFileOregonTaxesYear1(), R.string.taxes_year1));
		fileOregonTaxesYear2DateVm.set(DateViewModel.build(context, residency.getFileOregonTaxesYear2(), R.string.taxes_year2));
		parentsFileOregonTaxesYear1DateVm.set(DateViewModel.build(context, residency.getParentsFileOregonTaxesYear1(), R.string.taxes_year1));
		parentsFileOregonTaxesYear2DateVm.set(DateViewModel.build(context, residency.getParentsFileOregonTaxesYear2(), R.string.taxes_year2));
		nameEmployer1.set(residency.getNameEmployer1());
		cityEmployer1.set(residency.getCityEmployer1());
		startEmployer1DateVm.set(DateViewModel.build(context, residency.getStartEmployer1(), R.string.employment_start_date));
		endEmployer1DateVm.set(DateViewModel.build(context, residency.getEndEmployer1(), R.string.employment_end_date));
	}

	public void update()
	{
		if (!isDirty())
			return;

		Utils.d(LOG_TAG, "Saving Residency to database");

		Residency value = residencyData.getValue();

		value.setResidencyStart(residencyStartDateVm.get().getSelectedDate());
		value.setResidencyEnd(residencyEndDateVm.get().getSelectedDate());
		value.setParentResidencyStart(parentResidencyStartDateVm.get().getSelectedDate());
		value.setParentResidencyEnd(parentResidencyEndDateVm.get().getSelectedDate());
		value.setRegisterToVote(registerToVoteDateVm.get().getSelectedDate());
		value.setParentsRegisterToVote(parentsRegisterToVoteDateVm.get().getSelectedDate());
		value.setMilitaryServiceStart(militaryServiceStartDateVm.get().getSelectedDate());
		value.setMilitaryServiceEnd(militaryServiceEndDateVm.get().getSelectedDate());
		value.setParentMilitaryServiceStart(parentMilitaryServiceStartDateVm.get().getSelectedDate());
		value.setParentMilitaryServiceEnd(parentMilitaryServiceEndDateVm.get().getSelectedDate());
		value.setFileOregonTaxesYear1(fileOregonTaxesYear1DateVm.get().getSelectedDate());
		value.setFileOregonTaxesYear2(fileOregonTaxesYear2DateVm.get().getSelectedDate());
		value.setParentsFileOregonTaxesYear1(parentsFileOregonTaxesYear1DateVm.get().getSelectedDate());
		value.setParentsFileOregonTaxesYear2(parentsFileOregonTaxesYear2DateVm.get().getSelectedDate());
		value.setNameEmployer1(nameEmployer1.get());
		value.setCityEmployer1(cityEmployer1.get());
		value.setStartEmployer1(startEmployer1DateVm.get().getSelectedDate());
		value.setEndEmployer1(endEmployer1DateVm.get().getSelectedDate());

		repository.update(value);
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
			this.repository = MyPlanRepository.getInstance(application);
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

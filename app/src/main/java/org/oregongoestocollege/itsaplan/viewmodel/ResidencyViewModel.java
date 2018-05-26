package org.oregongoestocollege.itsaplan.viewmodel;

import java.util.Calendar;
import java.util.Date;

import android.app.Application;
import android.app.DatePickerDialog;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;

import org.oregongoestocollege.itsaplan.Utils;
import org.oregongoestocollege.itsaplan.data.MyPlanDatabase;
import org.oregongoestocollege.itsaplan.data.MyPlanRepository;
import org.oregongoestocollege.itsaplan.data.Residency;

import static com.google.common.base.Verify.verifyNotNull;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class ResidencyViewModel extends AndroidViewModel
{
	public static final String LOG_TAG = "GearUpResidency";
	private MyPlanRepository repository;
	private final LiveData<Residency> residency;
	private boolean dirty;
	// UX fields
	public final ObservableField<String> nameEmployer1 = new ObservableField<>();
	public final ObservableField<String> cityEmployer1 = new ObservableField<>();
	public final ObservableField<String> startEmployer1 = new ObservableField<>();
	public final ObservableField<String> endEmployer1 = new ObservableField<>();

	public ResidencyViewModel(@NonNull Application application, @NonNull MyPlanRepository repository)
	{
		super(application);

		this.repository = repository;
		this.residency = repository.getResidency();
	}

	private boolean isDirty()
	{
		Residency residency = getResidency().getValue();
		if (residency == null)
			return false;

		return dirty ||
			!TextUtils.equals(nameEmployer1.get(), residency.getNameEmployer1()) ||
			!TextUtils.equals(cityEmployer1.get(), residency.getCityEmployer1());
	}

	public LiveData<Residency> getResidency()
	{
		return residency;
	}

	public void setResidency(@NonNull Residency residency, @NonNull Context context)
	{
		verifyNotNull(residency);

		nameEmployer1.set(residency.getNameEmployer1());
		cityEmployer1.set(residency.getCityEmployer1());
		Date date = residency.getStartEmployer1();
		if (date != null)
			startEmployer1.set(DateFormat.getLongDateFormat(context).format(date));
		else
			startEmployer1.set(null);
		date = residency.getEndEmployer1();
		if (date != null)
			endEmployer1.set(DateFormat.getLongDateFormat(context).format(date));
		else
			endEmployer1.set(null);
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

	public void onChangeDate(View view)
	{
		final Context context = view.getContext();

		final Calendar c = Calendar.getInstance();
		Date initialDate = residency.getValue().getStartEmployer1();
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
					residency.getValue().setStartEmployer1(calendar.getTime());
					startEmployer1.set(DateFormat.getLongDateFormat(context).format(calendar.getTime()));
					dirty = true;
				}
			},
			c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

		datePickerDialog.show();
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

package org.oregongoestocollege.itsaplan.viewmodel;

import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;

import org.oregongoestocollege.itsaplan.R;
import org.oregongoestocollege.itsaplan.data.College;
import org.oregongoestocollege.itsaplan.data.MyPlanRepository;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class CollegeViewModel implements BindingItem
{
	private MyPlanRepository repository;
	public final College college;
	private final boolean removable;
	public final ObservableField<String> name = new ObservableField<>();
	public final ObservableField<String> applicationDeadline = new ObservableField<>();

	public CollegeViewModel(@NonNull MyPlanRepository repository, @NonNull College college, boolean removable)
	{
		this.repository = repository;
		this.college = college;
		this.removable = removable;

		name.set(college.getName());
	}

	@Override
	public int getLayoutId()
	{
		return R.layout.item_college;
	}

	@Override
	public void onBind(@NonNull Context context)
	{
		Date date = college.getApplicationDate();
		if (date != null)
			applicationDeadline.set(DateFormat.getLongDateFormat(context).format(date));

	}

	public void save()
	{
		repository.save(college);
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

	public void onShowDatePicker(View view)
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

					Date date = calendar.getTime();

					// update UX
					applicationDeadline.set(DateFormat.getLongDateFormat(context).format(date));
					// update stored value
					college.setApplicationDate(date);
				}
			}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

		datePickerDialog.show();
	}
}

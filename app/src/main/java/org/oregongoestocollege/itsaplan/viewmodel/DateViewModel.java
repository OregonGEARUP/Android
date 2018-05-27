package org.oregongoestocollege.itsaplan.viewmodel;

import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.content.Context;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;

import org.oregongoestocollege.itsaplan.R;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class DateViewModel implements BindingItem
{
	private Date originalDate;
	private Date selectedDate;
	@StringRes
	private final int placeholderStringRes;
	public final ObservableField<String> value = new ObservableField<>();

	/**
	 * Constructor for using in recycler view, binding item
	 */
	public DateViewModel(Date originalDate)
	{
		this(originalDate, R.string.hint_date_picker);
	}

	/**
	 * Constructor for use in fragment, view, etc.
	 */
	public DateViewModel(@NonNull Context context, Date originalDate)
	{
		this(originalDate, R.string.hint_date_picker);

		onBind(context);
	}

	public DateViewModel(Date originalDate, @StringRes int placeholderStringRes)
	{
		this.originalDate = originalDate;
		this.selectedDate = originalDate;
		this.placeholderStringRes = placeholderStringRes;
	}

	@Override
	public int getLayoutId()
	{
		return 0;
	}

	@Override
	public void onBind(@NonNull Context context)
	{
		if (selectedDate != null)
			value.set(DateFormat.getLongDateFormat(context).format(selectedDate));
		else
			value.set(context.getString(placeholderStringRes));
	}

	public boolean isDirty()
	{
		return originalDate != selectedDate;
	}

	public Date getSelectedDate()
	{
		return selectedDate;
	}

	public void onChangeDate(View view)
	{
		final Context context = view.getContext();

		final Calendar c = Calendar.getInstance();
		if (selectedDate != null)
			c.setTime(selectedDate);

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
					selectedDate = calendar.getTime();
					value.set(DateFormat.getLongDateFormat(context).format(selectedDate));
				}
			},
			c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

		datePickerDialog.show();
	}
}

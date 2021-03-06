package org.oregongoestocollege.itsaplan.viewmodel;

import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.content.Context;
import androidx.databinding.ObservableField;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import android.text.format.DateFormat;
import android.view.View;

import org.oregongoestocollege.itsaplan.R;
import org.oregongoestocollege.itsaplan.support.BindingItem;

/**
 * Oregon GEAR UP App
 * Copyright © 2020 Oregon GEAR UP. All rights reserved.
 */
public class DateViewModel implements BindingItem
{
	private Date originalDate;
	private Date selectedDate;
	@StringRes
	private final int labelStringRes;
	public final ObservableField<String> label = new ObservableField<>();
	public final ObservableField<String> value = new ObservableField<>();

	public DateViewModel(Date originalDate, @StringRes int labelStringRes)
	{
		this.originalDate = originalDate;
		this.selectedDate = originalDate;
		this.labelStringRes = labelStringRes;
	}

	public DateViewModel(Date originalDate, String label)
	{
		this(originalDate, 0);

		this.label.set(label);
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
			value.set(context.getString(R.string.hint_date_picker));

		if (labelStringRes != 0)
			this.label.set(context.getString(labelStringRes));
	}

	/**
	 * @return true if the stored data is different than the user entered data
	 */
	public boolean isDirty()
	{
		return originalDate != selectedDate;
	}

	/**
	 * Reset the data so the stored data is the same as the user entered data
	 */
	public void saved()
	{
		originalDate = selectedDate;
	}

	public Date getSelectedDate()
	{
		return selectedDate;
	}

	public boolean hasSelectedDate()
	{
		return selectedDate != null;
	}

	public void onChangeDate(View view)
	{
		final Context context = view.getContext();

		final Calendar c = Calendar.getInstance();
		if (selectedDate != null)
			c.setTime(selectedDate);

		DatePickerDialog datePickerDialog = new DatePickerDialog(context,
			(view1, year, month, day) ->
			{
				final Calendar calendar = Calendar.getInstance();
				calendar.clear();
				calendar.set(year, month, day);

				// update UX
				selectedDate = calendar.getTime();
				value.set(DateFormat.getLongDateFormat(context).format(selectedDate));
			},
			c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

		datePickerDialog.show();
	}

	public static DateViewModel build(@NonNull Context context, Date originalDate, @StringRes int placeholderStringRes)
	{
		DateViewModel viewModel = new DateViewModel(originalDate, placeholderStringRes);
		viewModel.onBind(context);
		return viewModel;
	}

	public static DateViewModel build(@NonNull Context context, Date originalDate, String label)
	{
		DateViewModel viewModel = new DateViewModel(originalDate, label);
		viewModel.onBind(context);
		return viewModel;
	}
}

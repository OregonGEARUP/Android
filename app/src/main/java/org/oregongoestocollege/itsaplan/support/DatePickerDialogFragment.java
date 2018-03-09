package org.oregongoestocollege.itsaplan.support;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

/**
 * BindingItemsAdapter
 * Oregon GEAR UP App
 *
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class DatePickerDialogFragment extends DialogFragment
{
	private int year;
	private int month;
	private int day;
	private OnDateSetListener listener;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		return new DatePickerDialog(getActivity(), listener, year, month, day);
	}

	@Override
	public void onDismiss(final DialogInterface dialog)
	{
		super.onDismiss(dialog);
		Fragment targetFragment = getTargetFragment();
		if (targetFragment instanceof DialogInterface.OnDismissListener)
			((DialogInterface.OnDismissListener)targetFragment).onDismiss(dialog);
	}

	/**
	 * Single-use Builder
	 */
	public static class Builder
	{
		private final DatePickerDialogFragment building = new DatePickerDialogFragment();

		public Builder setYear(int year)
		{
			building.year = year;
			return this;
		}

		public Builder setMonth(int month)
		{
			building.month = month;
			return this;
		}

		public Builder setDay(int day)
		{
			building.day = day;
			return this;
		}

		public DatePickerDialogFragment createFromFragment(int requestCode, Fragment target)
		{
			building.setTargetFragment(target, requestCode);
			building.listener = (OnDateSetListener)target;
			return building;
		}

		public DatePickerDialogFragment createFromActivity(int requestCode, Activity target)
		{
			building.setTargetFragment(null, requestCode);
			building.listener = (OnDateSetListener)target;
			return building;
		}
	}
}
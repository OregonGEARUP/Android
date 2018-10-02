package org.oregongoestocollege.itsaplan.viewmodel;

import android.content.Context;
import android.support.annotation.NonNull;

import org.oregongoestocollege.itsaplan.R;
import org.oregongoestocollege.itsaplan.support.BindingItem;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class CalendarEventViewModel implements BindingItem
{
	public final String title;

	public CalendarEventViewModel(String title)
	{
		this.title = title;
	}

	@Override
	public int getLayoutId()
	{
		return R.layout.item_calendar_event;
	}

	@Override
	public void onBind(@NonNull Context context)
	{
		// no-op
	}
}

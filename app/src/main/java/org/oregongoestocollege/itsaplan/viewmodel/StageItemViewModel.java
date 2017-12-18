package org.oregongoestocollege.itsaplan.viewmodel;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;
import android.view.View;

import org.oregongoestocollege.itsaplan.R;
import org.oregongoestocollege.itsaplan.SingleLiveEvent;
import org.oregongoestocollege.itsaplan.data.Stage;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class StageItemViewModel implements BindingItem
{
	// service data
	private final Stage model;
	private final SingleLiveEvent<Stage> openStageEvent;
	// view data
	public final String title;
	public final ObservableBoolean isComplete = new ObservableBoolean();

	public StageItemViewModel(@NonNull Context context, @NonNull Stage model, SingleLiveEvent<Stage> openStageEvent)
	{
		checkNotNull(context);
		checkNotNull(model);

		this.model = model;
		this.openStageEvent = openStageEvent;
		this.title = model.title;
	}

	@Override
	public int getLayoutId()
	{
		return R.layout.item_stage;
	}

	public void onStageClick(View view)
	{
		if (openStageEvent != null)
			openStageEvent.setValue(model);
	}
}

package org.oregongoestocollege.itsaplan.viewmodel;

import android.app.Application;
import android.content.Context;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;
import android.view.View;

import org.oregongoestocollege.itsaplan.R;
import org.oregongoestocollege.itsaplan.SingleLiveEvent;
import org.oregongoestocollege.itsaplan.data.ChecklistState;
import org.oregongoestocollege.itsaplan.data.CheckpointInterface;
import org.oregongoestocollege.itsaplan.data.Stage;
import org.oregongoestocollege.itsaplan.support.BindingItem;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class StageItemViewModel implements BindingItem
{
	// service data
	private int blockIndex;
	private int stageIndex;
	private Stage model;
	private final SingleLiveEvent<ChecklistState> openStageEvent;
	// view data
	private final CheckpointInterface repository;
	public String title;
	public final ObservableBoolean isCompleted = new ObservableBoolean();

	public StageItemViewModel(@NonNull Application context, @NonNull CheckpointInterface repository,
		int blockIndex, int stageIndex,
		SingleLiveEvent<ChecklistState> openStageEvent)
	{
		checkNotNull(context);
		checkNotNull(repository);

		this.repository = repository;
		this.blockIndex = blockIndex;
		this.stageIndex = stageIndex;
		this.openStageEvent = openStageEvent;

		model = repository.getStage(blockIndex, stageIndex);
		if (model != null)
			title = model.title;

		isCompleted.set(repository.stageCompleted(stageIndex));
	}

	@Override
	public int getLayoutId()
	{
		return R.layout.item_stage;
	}

	@Override
	public void onBind(@NonNull Context context)
	{

	}

	public void onStageClick(View view)
	{
		if (openStageEvent != null)
			openStageEvent.setValue(new ChecklistState(blockIndex, stageIndex));
	}
}

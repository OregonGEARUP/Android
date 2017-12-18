package org.oregongoestocollege.itsaplan.viewmodel;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;

import org.oregongoestocollege.itsaplan.SingleLiveEvent;
import org.oregongoestocollege.itsaplan.data.Block;
import org.oregongoestocollege.itsaplan.data.CheckpointInterface;
import org.oregongoestocollege.itsaplan.data.CheckpointRepository;
import org.oregongoestocollege.itsaplan.data.Stage;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class BlockViewModel extends AndroidViewModel implements CheckpointInterface.LoadBlockCallback
{
	// service data
	private String blockFileName;
	private Block model;
	// view data
	private final Context context; // To avoid leaks, this must be an Application Context.
	private final CheckpointRepository checkpointRepository;
	private final SingleLiveEvent<Void> updateListEvent = new SingleLiveEvent<>();
	private final SingleLiveEvent<Stage> openStageEvent = new SingleLiveEvent<>();
	private List<BindingItem> items;
	public final ObservableBoolean dataLoading = new ObservableBoolean(false);

	public BlockViewModel(@NonNull Application context, @NonNull CheckpointRepository checkpointRepository)
	{
		super(context);

		checkNotNull(context);

		// force use of application context
		this.context = context.getApplicationContext();
		this.checkpointRepository = checkpointRepository;
	}

	public void start(@NonNull String blockFileName)
	{
		dataLoading.set(true);

		checkpointRepository.getBlock(this, blockFileName);
	}

	public SingleLiveEvent<Void> getUpdateListEvent()
	{
		return updateListEvent;
	}

	public SingleLiveEvent<Stage> getOpenStageEvent()
	{
		return openStageEvent;
	}

	public List<BindingItem> getItems()
	{
		return items;
	}

	@Override
	public void onDataLoaded(@NonNull Block block)
	{
		checkNotNull(block);

		List<Stage> stages = block.stages;
		if (stages != null && !stages.isEmpty())
		{
			List<BindingItem> viewModels = new ArrayList<>(stages.size());

			for (Stage stage : stages)
				viewModels.add(new StageItemViewModel(context, stage, openStageEvent));

			items = viewModels;
		}

		updateListEvent.call();

		dataLoading.set(false);
	}

	@Override
	public void onDataNotAvailable()
	{
		dataLoading.set(false);
	}

	public String getTitle()
	{
		return model != null ? model.blocktitle : null;
	}
}

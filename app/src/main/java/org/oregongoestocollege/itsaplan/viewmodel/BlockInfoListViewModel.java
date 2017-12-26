package org.oregongoestocollege.itsaplan.viewmodel;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;

import org.oregongoestocollege.itsaplan.SingleLiveEvent;
import org.oregongoestocollege.itsaplan.data.BlockInfo;
import org.oregongoestocollege.itsaplan.data.CheckpointInterface;
import org.oregongoestocollege.itsaplan.data.CheckpointRepository;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class BlockInfoListViewModel extends AndroidViewModel implements CheckpointInterface.LoadBlockInfoListCallback
{
	private final CheckpointRepository checkpointRepository;
	private final SingleLiveEvent<Void> updateListEvent = new SingleLiveEvent<>();
	private final SingleLiveEvent<Integer> openBlockEvent = new SingleLiveEvent<>();
	private List<BindingItem> items;
	// These observable fields will update Views automatically
	public final ObservableBoolean dataLoading = new ObservableBoolean(false);

	public BlockInfoListViewModel(@NonNull Application context, @NonNull CheckpointRepository checkpointRepository)
	{
		// To avoid leaks, force use of application context
		super(context);

		this.checkpointRepository = checkpointRepository;
	}

	public void start()
	{
		dataLoading.set(true);

		checkpointRepository.getBlockInfoList(this);
	}

	public SingleLiveEvent<Void> getUpdateListEvent()
	{
		return updateListEvent;
	}

	public SingleLiveEvent<Integer> getOpenBlockEvent()
	{
		return openBlockEvent;
	}

	public List<BindingItem> getItems()
	{
		return items;
	}

	@Override
	public void onDataLoaded(@NonNull List<BlockInfo> blockInfoList)
	{
		checkNotNull(blockInfoList);

		int counter = 0;
		List<BindingItem> viewModels = new ArrayList<>(blockInfoList.size());

		for (BlockInfo blockInfo : blockInfoList)
			viewModels.add(new BlockInfoItemViewModel(this.getApplication(), blockInfo, counter++, openBlockEvent));

		items = viewModels;

		updateListEvent.call();

		dataLoading.set(false);
	}

	@Override
	public void onDataNotAvailable()
	{
		dataLoading.set(false);
	}
}

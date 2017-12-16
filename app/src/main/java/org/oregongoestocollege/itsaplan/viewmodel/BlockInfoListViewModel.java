package org.oregongoestocollege.itsaplan.viewmodel;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
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
	// These observable fields will update Views automatically
	public final ObservableBoolean dataLoading = new ObservableBoolean(false);
	private final SingleLiveEvent<String> updateListEvent = new SingleLiveEvent<>();
	private final SingleLiveEvent<String> openBlockEvent = new SingleLiveEvent<>();
	private final Context context; // To avoid leaks, this must be an Application Context.
	private final CheckpointRepository checkpointRepository;
	private List<BaseViewModel> items;

	public BlockInfoListViewModel(@NonNull Application context, @NonNull CheckpointRepository checkpointRepository)
	{
		super(context);

		// force use of application context
		this.context = context.getApplicationContext();
		this.checkpointRepository = checkpointRepository;
	}

	public void start()
	{
		dataLoading.set(true);

		checkpointRepository.getBlockInfoList(this);
	}

	public SingleLiveEvent<String> getUpdateListEvent()
	{
		return updateListEvent;
	}

	public SingleLiveEvent<String> getOpenBlockEvent()
	{
		return openBlockEvent;
	}

	public List<BaseViewModel> getItems()
	{
		return items;
	}

	@Override
	public void onDataLoaded(@NonNull List<BlockInfo> blockInfoList)
	{
		checkNotNull(blockInfoList);

		int counter = 1;
		List<BaseViewModel> viewModels = new ArrayList<>(blockInfoList.size());

		for (BlockInfo blockInfo : blockInfoList)
			viewModels.add(new BlockInfoViewModel(context, blockInfo, counter++));

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

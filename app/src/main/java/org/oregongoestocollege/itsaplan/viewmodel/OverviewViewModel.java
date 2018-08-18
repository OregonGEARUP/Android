package org.oregongoestocollege.itsaplan.viewmodel;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;

import org.oregongoestocollege.itsaplan.data.BlockInfo;
import org.oregongoestocollege.itsaplan.data.CheckpointInterface;
import org.oregongoestocollege.itsaplan.data.CheckpointRepository;
import org.oregongoestocollege.itsaplan.data.MyPlanRepository;
import org.oregongoestocollege.itsaplan.support.BindingItem;

/**
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class OverviewViewModel extends AndroidViewModel
{
	private final CheckpointInterface checkpointRepo;
	private final MyPlanRepository myPlanRepo;
	private LiveData<List<BindingItem>> itemViewModels;
	private LiveData<Boolean> state;
	private ObservableBoolean dataLoading;

	public OverviewViewModel(@NonNull Application context)
	{
		// To avoid leaks, force use of application context
		super(context);

		checkpointRepo = CheckpointRepository.getInstance();
		myPlanRepo = MyPlanRepository.getInstance(context);

		// talk to the database to get our data
		LiveData<List<BlockInfo>> blockInfoList = myPlanRepo.getAllBlockInfos();
		itemViewModels = Transformations.map(blockInfoList, this::onApply);

		// talk to the checkpoint repo to get our
		//checkpointRepo.blockInfoListLoading().observe(getApplication(), this::onLoading);
	}

	private List<BindingItem> onApply(List<BlockInfo> input)
	{
		if (input == null)
			return null;

		List<BindingItem> items = null;

		for (int i = 0, size = input.size(); i < size; i++)
		{
			if (items == null)
				items = new ArrayList<>(size);

			items.add(new BlockInfoItemViewModel(input.get(i), i));
		}

		return items;
	}

	private void onLoading(Boolean loading)
	{
		dataLoading.set(loading);
	}

	/**
	 * Expose the LiveData request so the UI can observe it.
	 */
	public LiveData<List<BindingItem>> getBlockInfoList()
	{
		return itemViewModels;
	}

	/**
	 * Expose the LiveData request so the UI can observe it.
	 */
	public ObservableBoolean isDataLoading()
	{
		return dataLoading;
	}

	public void start()
	{
		// make sure the repo has the latest
		checkpointRepo.resumeCheckpoints(myPlanRepo);
	}
}

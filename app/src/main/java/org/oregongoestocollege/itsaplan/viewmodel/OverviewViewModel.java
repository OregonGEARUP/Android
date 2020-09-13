package org.oregongoestocollege.itsaplan.viewmodel;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.databinding.ObservableBoolean;
import androidx.annotation.NonNull;

import org.oregongoestocollege.itsaplan.data.BlockInfo;
import org.oregongoestocollege.itsaplan.data.CheckpointInterface;
import org.oregongoestocollege.itsaplan.data.CheckpointRepository;
import org.oregongoestocollege.itsaplan.data.MyPlanRepository;
import org.oregongoestocollege.itsaplan.support.BindingItem;

/**
 * Oregon GEAR UP App
 * Copyright © 2020 Oregon GEAR UP. All rights reserved.
 */
public class OverviewViewModel extends AndroidViewModel
{
	private final CheckpointInterface checkpointRepo;
	private final MyPlanRepository myPlanRepo;
	private LiveData<List<BindingItem>> itemViewModels;
	private LiveData<Boolean> listLoading;
	public final ObservableBoolean dataLoading = new ObservableBoolean();

	public OverviewViewModel(@NonNull Application context)
	{
		// To avoid leaks, force use of application context
		super(context);

		checkpointRepo = CheckpointRepository.getInstance(context);
		myPlanRepo = MyPlanRepository.getInstance(context);

		// listen for data changes
		LiveData<List<BlockInfo>> blockInfoList = checkpointRepo.getBlockInfoList(myPlanRepo);
		itemViewModels = Transformations.map(blockInfoList, this::onApplyList);

		// listen for loading changes
		listLoading = checkpointRepo.blockInfoListLoading();
	}

	private List<BindingItem> onApplyList(List<BlockInfo> input)
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
	public LiveData<Boolean> getListLoading()
	{
		return listLoading;
	}

	public void start()
	{
		// make sure the repo has the latest
		checkpointRepo.loadBlockInfoList(myPlanRepo);
	}
}

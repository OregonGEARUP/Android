package org.oregongoestocollege.itsaplan.viewmodel;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;
import android.widget.Toast;

import org.oregongoestocollege.itsaplan.R;
import org.oregongoestocollege.itsaplan.SingleLiveEvent;
import org.oregongoestocollege.itsaplan.data.BlockInfo;
import org.oregongoestocollege.itsaplan.data.CheckpointInterface;
import org.oregongoestocollege.itsaplan.data.CheckpointRepository;
import org.oregongoestocollege.itsaplan.support.BindingItem;

/**
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class AllBlocksViewModel extends AndroidViewModel implements CheckpointInterface.CheckpointCallback
{
	private final CheckpointInterface repository;
	private final SingleLiveEvent<Void> updateListEvent = new SingleLiveEvent<>();
	private List<BindingItem> items;
	// These observable fields will update Views automatically
	public final ObservableBoolean dataLoading = new ObservableBoolean(false);

	public AllBlocksViewModel(@NonNull Application context)
	{
		// To avoid leaks, force use of application context
		super(context);

		this.repository = CheckpointRepository.getInstance();
	}

	public void start()
	{
		dataLoading.set(true);

		repository.resumeCheckpoints(this);
	}

	public SingleLiveEvent<Void> getUpdateListEvent()
	{
		return updateListEvent;
	}

	public List<BindingItem> getItems()
	{
		return items;
	}

	@Override
	public void onDataLoaded(boolean success)
	{
		if (success)
		{
			int size = repository.getCountOfBlocks();
			if (size > 0)
			{
				List<BindingItem> viewModels = new ArrayList<>(size);

				for (int i = 0; i < size; i++)
				{
					BlockInfo blockInfo = repository.getBlockInfo(i);
					viewModels
						.add(new BlockInfoItemViewModel(blockInfo, i));
				}

				items = viewModels;

				updateListEvent.call();
			}
		}
		else
		{
			Toast.makeText(
				this.getApplication(), getApplication().getResources().getText(R.string.error_data), Toast.LENGTH_SHORT)
				.show();
		}

		dataLoading.set(false);
	}
}

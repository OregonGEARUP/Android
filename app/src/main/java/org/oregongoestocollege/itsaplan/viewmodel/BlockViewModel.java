package org.oregongoestocollege.itsaplan.viewmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;
import android.widget.Toast;

import org.oregongoestocollege.itsaplan.R;
import org.oregongoestocollege.itsaplan.SingleLiveEvent;
import org.oregongoestocollege.itsaplan.data.Block;
import org.oregongoestocollege.itsaplan.data.CheckpointInterface;
import org.oregongoestocollege.itsaplan.data.CheckpointRepository;
import org.oregongoestocollege.itsaplan.data.MyPlanRepository;
import org.oregongoestocollege.itsaplan.data.Stage;
import org.oregongoestocollege.itsaplan.support.BindingItem;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class BlockViewModel extends AndroidViewModel implements CheckpointInterface.CheckpointCallback
{
	private final CheckpointInterface repository;
	private final MyPlanRepository myPlanRepository;
	// service data
	private Block model;
	private int blockIndex;
	private String blockFileName;
	// view data
	private final SingleLiveEvent<Void> updateListEvent = new SingleLiveEvent<>();
	private List<BindingItem> items;
	public final ObservableBoolean dataLoading = new ObservableBoolean(false);

	public BlockViewModel(@NonNull Application context)
	{
		// To avoid leaks, force use of application context
		super(context);

		checkNotNull(context);

		this.repository = CheckpointRepository.getInstance();
		this.myPlanRepository = MyPlanRepository.getInstance(context);
	}

	public void start(int blockIndex, String blockFileName)
	{
		this.blockIndex = blockIndex;
		this.blockFileName = blockFileName;

		dataLoading.set(true);

		repository.loadBlock(myPlanRepository, this, blockIndex, blockFileName);
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
			Block block = repository.getBlock(blockIndex);
			if (block != null)
			{
				List<Stage> stages = block.stages;
				if (stages != null)
				{
					int size = stages.size();
					if (size > 0)
					{
						List<BindingItem> viewModels = new ArrayList<>(size);

						for (int i = 0; i < size; i++)
						{
							viewModels.add(new StageItemViewModel(
								repository,
								blockIndex, i));
						}

						items = viewModels;
						model = block;
					}
				}

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

	public String getTitle()
	{
		return model != null ?
			String.format(Locale.getDefault(), "%d. %s", blockIndex + 1, model.blocktitle) :
			null;
	}
}

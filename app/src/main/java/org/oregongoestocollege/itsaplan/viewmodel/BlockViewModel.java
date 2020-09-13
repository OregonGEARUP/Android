package org.oregongoestocollege.itsaplan.viewmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.databinding.ObservableBoolean;
import androidx.annotation.NonNull;
import android.widget.Toast;

import org.oregongoestocollege.itsaplan.R;
import org.oregongoestocollege.itsaplan.data.Block;
import org.oregongoestocollege.itsaplan.data.CheckpointInterface;
import org.oregongoestocollege.itsaplan.data.CheckpointRepository;
import org.oregongoestocollege.itsaplan.data.MyPlanRepository;
import org.oregongoestocollege.itsaplan.data.Stage;
import org.oregongoestocollege.itsaplan.support.BindingItem;

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
	private MutableLiveData<List<BindingItem>> itemViewModels = new MutableLiveData<>();
	public final ObservableBoolean dataLoading = new ObservableBoolean(false);

	public BlockViewModel(@NonNull Application context)
	{
		// To avoid leaks, force use of application context
		super(context);

		this.repository = CheckpointRepository.getInstance(context);
		this.myPlanRepository = MyPlanRepository.getInstance(context);
	}

	public void start(int blockIndex, String blockFileName)
	{
		this.blockIndex = blockIndex;
		this.blockFileName = blockFileName;

		dataLoading.set(true);

		repository.loadBlock(myPlanRepository, this, blockIndex, blockFileName);
	}

	/**
	 * Expose the LiveData request so the UI can observe it.
	 */
	public LiveData<List<BindingItem>> getBlockItems()
	{
		return itemViewModels;
	}

	@Override
	public void onDataLoaded(boolean success)
	{
		if (success)
		{
			Block block = repository.getBlock(blockIndex);
			if (block != null)
			{
				List<BindingItem> viewModels = null;

				List<Stage> stages = block.stages;
				if (stages != null)
				{
					int size = stages.size();
					if (size > 0)
					{
						viewModels = new ArrayList<>(size);

						for (int i = 0; i < size; i++)
						{
							viewModels.add(new StageItemViewModel(
								repository,
								blockIndex, i));
						}

						model = block;
					}
				}

				itemViewModels.setValue(viewModels);
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

	public int getBlockIndex()
	{
		return blockIndex;
	}

	public String getBlockFileName()
	{
		return blockFileName;
	}
}

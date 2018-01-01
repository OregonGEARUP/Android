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
import org.oregongoestocollege.itsaplan.data.Stage;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class BlockViewModel extends AndroidViewModel implements CheckpointInterface.CheckpointCallback
{
	// service data
	private Block model;
	private int index;
	// view data
	private final CheckpointRepository checkpointRepository;
	private final SingleLiveEvent<Void> updateListEvent = new SingleLiveEvent<>();
	private final SingleLiveEvent<Stage> openStageEvent = new SingleLiveEvent<>();
	private List<BindingItem> items;
	public final ObservableBoolean dataLoading = new ObservableBoolean(false);

	public BlockViewModel(@NonNull Application context, @NonNull CheckpointRepository checkpointRepository)
	{
		// To avoid leaks, force use of application context
		super(context);

		checkNotNull(context);

		this.checkpointRepository = checkpointRepository;
	}

	public void start(int blockIndex)
	{
		this.index = blockIndex;

		dataLoading.set(true);

		checkpointRepository.loadBlock(this.getApplication(), this, index);
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
	public void onDataLoaded(boolean success)
	{
		if (success)
		{
			Block block = CheckpointRepository.getInstance().getBlock(index);
			if (block != null)
			{
				List<Stage> stages = block.stages;
				if (stages != null && !stages.isEmpty())
				{
					List<BindingItem> viewModels = new ArrayList<>(stages.size());

					for (Stage stage : stages)
						viewModels.add(new StageItemViewModel(this.getApplication(), stage, openStageEvent));

					items = viewModels;
					model = block;
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
			String.format(Locale.getDefault(), "%d. %s", index + 1, model.blocktitle) :
			null;
	}
}

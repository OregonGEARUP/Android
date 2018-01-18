package org.oregongoestocollege.itsaplan.viewmodel;

import java.util.Locale;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.view.View;

import org.oregongoestocollege.itsaplan.R;
import org.oregongoestocollege.itsaplan.SingleLiveEvent;
import org.oregongoestocollege.itsaplan.data.BlockInfo;
import org.oregongoestocollege.itsaplan.data.ChecklistState;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class BlockInfoItemViewModel implements BindingItem
{
	// service data
	private final int blockIndex;
	private final BlockInfo model;
	private final SingleLiveEvent<ChecklistState> openBlockEvent;
	// view data
	public final String title;
	public final ObservableField<String> count = new ObservableField<>();
	public final ObservableBoolean showCount = new ObservableBoolean();
	public final ObservableBoolean isEnabled = new ObservableBoolean();
	public final ObservableBoolean isDone = new ObservableBoolean();

	public BlockInfoItemViewModel(@NonNull Context context,
		@NonNull BlockInfo model, int blockIndex,
		SingleLiveEvent<ChecklistState> openBlockEvent)
	{
		checkNotNull(context);

		this.model = checkNotNull(model);
		this.blockIndex = blockIndex;
		this.title = String.format(Locale.getDefault(), "%d. %s", blockIndex + 1, model.title);
		this.openBlockEvent = openBlockEvent;

		if (model.available())
		{
			isEnabled.set(true);

			if (model.done())
			{
				isDone.set(true);
			}
			else if (model.stagesCompleted >= 0 && model.stageCount >= 0)
			{
				showCount.set(true);
				count.set(context.getString(R.string.x_of_y, model.stagesCompleted, model.stageCount));
			}
		}
	}

	@Override
	public int getLayoutId()
	{
		return R.layout.item_block_info;
	}

	public void onBlockClick(View view)
	{
		if (model.blockFileName != null && !model.blockFileName.isEmpty())
		{
			if (openBlockEvent != null)
				openBlockEvent.setValue(new ChecklistState(blockIndex));
		}
	}
}

package org.oregongoestocollege.itsaplan.viewmodel;

import java.util.Locale;

import android.content.Context;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.annotation.NonNull;

import org.oregongoestocollege.itsaplan.R;
import org.oregongoestocollege.itsaplan.data.BlockInfo;
import org.oregongoestocollege.itsaplan.support.BindingItem;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Oregon GEAR UP App
 * Copyright © 2020 Oregon GEAR UP. All rights reserved.
 */
public class BlockInfoItemViewModel implements BindingItem
{
	// service data
	private final int blockIndex;
	private final BlockInfo model;
	// view data
	public final ObservableField<String> title = new ObservableField<>();
	public final ObservableField<String> count = new ObservableField<>();
	public final ObservableBoolean showCount = new ObservableBoolean();
	public final ObservableBoolean isEnabled = new ObservableBoolean();
	public final ObservableBoolean isDone = new ObservableBoolean();

	public BlockInfoItemViewModel(@NonNull BlockInfo model, int blockIndex)
	{
		this.model = checkNotNull(model);
		this.blockIndex = blockIndex;
	}

	@Override
	public int getLayoutId()
	{
		return R.layout.item_block_info;
	}

	@Override
	public void onBind(@NonNull Context context)
	{
		title.set(String.format(Locale.getDefault(), "%d. %s", blockIndex + 1,
			model.getTitle()));

		boolean enabled = false;
		boolean done = false;
		boolean show = false;
		String showText = null;

		if (model.available())
		{
			enabled = true;

			if (model.done())
			{
				done = true;
			}
			else if (model.getStagesComplete() >= 0 && model.getStageCount() >= 0)
			{
				show = true;
				showText = context.getString(R.string.x_of_y, model.getStagesComplete(), model.getStageCount());
			}
		}

		isEnabled.set(enabled);
		isDone.set(done);
		showCount.set(show);
		count.set(showText);
	}

	public boolean clickable()
	{
		return model.available();
	}

	public String getBlockFileName()
	{
		return model.getBlockFileName();
	}

	public int getBlockIndex()
	{
		return blockIndex;
	}
}

package org.oregongoestocollege.itsaplan.viewmodel;

import java.util.Locale;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import org.oregongoestocollege.itsaplan.R;
import org.oregongoestocollege.itsaplan.data.BlockInfo;

/**
 * BlockInfoViewModel
 * Oregon GEAR UP App
 *
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class BlockInfoViewModel implements BaseViewModel
{
	public final String title;
	public final ObservableField<String> count = new ObservableField<>();
	public final ObservableBoolean isEnabled = new ObservableBoolean();

	private final BlockInfo model;

	public BlockInfoViewModel(@NonNull Context context, @NonNull BlockInfo model, int index)
	{
		this.model = model;

		title = String.format(Locale.getDefault(), "%d. %s", index, model.title);

		if (model.available())
		{
			isEnabled.set(true);
			count.set(context.getString(R.string.x_of_y, model.stagesCompleted, model.stageCount));
		}
	}

	@Override
	public int getLayoutId()
	{
		return R.layout.item_block_info;
	}
}

package org.oregongoestocollege.itsaplan.viewmodel;

import java.util.Locale;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import org.oregongoestocollege.itsaplan.R;
import org.oregongoestocollege.itsaplan.SingleLiveEvent;
import org.oregongoestocollege.itsaplan.data.BlockInfo;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class BlockInfoViewModel implements BaseViewModel
{
	// service data
	private final BlockInfo model;
	private final SingleLiveEvent<String> openBlockEvent;
	// view data
	public final String title;
	public final ObservableField<String> count = new ObservableField<>();
	public final ObservableBoolean isEnabled = new ObservableBoolean();

	public BlockInfoViewModel(@NonNull Context context, @NonNull BlockInfo model, int index, SingleLiveEvent<String> openBlockEvent)
	{
		checkNotNull(context);
		checkNotNull(model);

		this.model = model;
		this.title = String.format(Locale.getDefault(), "%d. %s", index, model.title);
		this.openBlockEvent = openBlockEvent;

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

	public void onBlockClick(View view)
	{
		if (model.blockFileName != null && !model.blockFileName.isEmpty())
		{
			Toast.makeText(view.getContext(), "got " + model.blockFileName, Toast.LENGTH_SHORT).show();

			if (openBlockEvent != null)
				openBlockEvent.setValue(model.blockFileName);
		}
	}
}

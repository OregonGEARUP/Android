package org.oregongoestocollege.itsaplan.viewmodel;

import java.util.List;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import org.oregongoestocollege.itsaplan.R;
import org.oregongoestocollege.itsaplan.data.Checkpoint;
import org.oregongoestocollege.itsaplan.data.CheckpointInterface;
import org.oregongoestocollege.itsaplan.data.EntryType;
import org.oregongoestocollege.itsaplan.data.Instance;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class CheckpointViewModel extends AndroidViewModel
{
	// service data
	private Checkpoint model;
	private int blockIndex;
	private int stageIndex;
	private int checkpointIndex;
	// view data
	private final CheckpointInterface repository;
	public String description;
	public int descriptionTextColor;
	public Drawable image;
	// for checkboxes / radio buttons
	private final int MAX_BUTTONS = 5;
	private int instanceCount;
	private List<Instance> instances;
	// for route / nextstage
	public String nextText;
	public boolean showNextText;
	public boolean showStars;


	public CheckpointViewModel(@NonNull Application context, @NonNull CheckpointInterface repository)
	{
		// To avoid leaks, force use of application context
		super(context);

		this.repository = checkNotNull(repository);
	}

	public void start(int blockIndex, int stageIndex, int checkpointIndex)
	{
		this.blockIndex = blockIndex;
		this.stageIndex = stageIndex;
		this.checkpointIndex = checkpointIndex;

		model = repository.getCheckpoint(blockIndex, stageIndex, checkpointIndex);
		if (model != null)
		{
			Context context = getApplication();
			Resources resources = getApplication().getResources();

			// setup defaults
			description = model.description;
			descriptionTextColor = ContextCompat.getColor(context, R.color.text_primary);

			switch (model.entryType)
			{
			case info:
				// no-op only uses description
				break;
			case checkbox:
			case radio:
				setupCheckboxAndRadioEntry();
				break;
			case field:
			case dateOnly:
				break;
			case dateAndText:
				break;
			case route:
			case nextstage:
				setupRouteEntry();
				break;
			}
		}
	}

	private void setupCheckboxAndRadioEntry()
	{
		List<Instance> modelInstances = model.instances;
		if (modelInstances != null)
		{
			int size = modelInstances.size();
			if (size > 0)
			{
				// limit instances to our maximum
				instanceCount = size > MAX_BUTTONS ? MAX_BUTTONS : size;
				instances = modelInstances.subList(0, instanceCount);
			}
		}
	}

	private void setupRouteEntry()
	{
		Context context = getApplication();
		Resources resources = getApplication().getResources();

		descriptionTextColor = ContextCompat.getColor(context, R.color.colorPrimary);
		showNextText = true;

		if (EntryType.route.equals(model.entryType))
		{
			showStars = true;
			image = ContextCompat.getDrawable(context, R.mipmap.stars);
			nextText = resources.getString(R.string.checkpoint_next_onward, blockIndex+2);
		}
		else
		{
			description = resources.getString(R.string.checkpoint_next_message);
			nextText = resources.getString(R.string.checkpoint_next_keep_going);
		}

		// don't show button if route CP for the last block
		showNextText = (!EntryType.route.equals(model.entryType) ||
			blockIndex != repository.getCountOfBlocks()-1);
	}

	public boolean showInstances()
	{
		return instances != null;
	}

	public boolean showInstance(int instance)
	{
		return instances != null && instance >= 0 && instance < instanceCount;
	}

	public String getInstancePrompt(int instance)
	{
		return showInstance(instance) ? instances.get(instance).getPrompt() : null;
	}
}

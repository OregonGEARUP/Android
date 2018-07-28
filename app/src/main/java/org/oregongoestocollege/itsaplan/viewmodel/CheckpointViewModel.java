package org.oregongoestocollege.itsaplan.viewmodel;

import java.util.List;
import java.util.Locale;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import org.oregongoestocollege.itsaplan.R;
import org.oregongoestocollege.itsaplan.SingleLiveEvent;
import org.oregongoestocollege.itsaplan.WebViewActivity;
import org.oregongoestocollege.itsaplan.data.ChecklistState;
import org.oregongoestocollege.itsaplan.data.Checkpoint;
import org.oregongoestocollege.itsaplan.data.CheckpointInterface;
import org.oregongoestocollege.itsaplan.data.EntryType;
import org.oregongoestocollege.itsaplan.data.Instance;
import org.oregongoestocollege.itsaplan.data.Stage;
import org.oregongoestocollege.itsaplan.data.UserEntries;
import org.oregongoestocollege.itsaplan.data.UserEntriesInterface;

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
	private final UserEntriesInterface entries;
	private final CheckpointInterface repository;
	private final SingleLiveEvent<ChecklistState> nextStageEvent = new SingleLiveEvent<>();
	private final SingleLiveEvent<ChecklistState> nextBlockEvent = new SingleLiveEvent<>();
	private final SingleLiveEvent<Instance> showDatePicker = new SingleLiveEvent<>();
	public String description;
	public int descriptionTextColor;
	public Drawable image;
	private int instanceCount;
	private List<Instance> instances;
	// for info
	public String urlText;
	// for route / next stage
	public String nextText;
	public boolean showNextText;
	public boolean showStars;

	public CheckpointViewModel(@NonNull Application context, @NonNull CheckpointInterface repository)
	{
		// To avoid leaks, force use of application context
		super(context);

		this.repository = checkNotNull(repository);
		this.entries = new UserEntries(context);
	}

	public void start(Context context, int blockIndex, int stageIndex, int checkpointIndex)
	{
		this.blockIndex = blockIndex;
		this.stageIndex = stageIndex;
		this.checkpointIndex = checkpointIndex;

		model = repository.getCheckpoint(blockIndex, stageIndex, checkpointIndex);
		if (model != null)
		{
			// setup defaults
			description = model.description;
			descriptionTextColor = ContextCompat.getColor(context, R.color.text_primary);
			// url / help
			urlText = model.getVerifiedUrlText();

			switch (model.entryType)
			{
			case info:
				// url only for now
				break;
			case field:
				setupFieldEntry();
				break;
			case checkbox:
			case radio:
				setupCheckboxAndRadioEntry();
				break;
			case dateOnly:
				setupDateOnlyEntry(context);
				break;
			case dateAndText:
				setupDateAndTextEntry(context);
				break;
			case route:
			case nextstage:
				setupRouteEntry();
				break;
			}
		}
	}

	public SingleLiveEvent<ChecklistState> getNextStageEvent()
	{
		return nextStageEvent;
	}

	public SingleLiveEvent<ChecklistState> getNextBlockEvent()
	{
		return nextBlockEvent;
	}

	public SingleLiveEvent<Instance> getPickDateEvent()
	{
		return showDatePicker;
	}

	private void setupFieldEntry()
	{
		List<Instance> modelInstances = model.instances;
		if (modelInstances != null)
		{
			int size = modelInstances.size();
			if (size > 0)
			{
				// for fields
				final int MAX_FIELDS = 5;

				// limit instances to our maximum
				instanceCount = size > MAX_FIELDS ? MAX_FIELDS : size;
				instances = modelInstances.subList(0, instanceCount);

				for (int i = 0; i < instances.size(); i++)
				{
					instances.get(i).textEntry.set(
						entries.getValue(repository.keyForBlockIndex(blockIndex, stageIndex, checkpointIndex, i)));
				}
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
				// for checkboxes / radio buttons
				final int MAX_BUTTONS = 5;

				// limit instances to our maximum
				instanceCount = size > MAX_BUTTONS ? MAX_BUTTONS : size;
				instances = modelInstances.subList(0, instanceCount);

				for (int i = 0; i < instances.size(); i++)
				{
					instances.get(i).isChecked = (
						entries.getValueAsBoolean(
							repository.keyForBlockIndex(blockIndex, stageIndex, checkpointIndex, i)));
				}
			}
		}
	}

	private void setupDateOnlyEntry(Context context)
	{
		List<Instance> modelInstances = model.instances;
		if (modelInstances != null && !modelInstances.isEmpty())
		{
			// we only use the first instance for date
			instanceCount = 1;
			instances = modelInstances;

			Instance instance = instances.get(0);
			String key = repository.keyForBlockIndex(blockIndex, stageIndex, checkpointIndex, 0) + "_date";
			long value = entries.getValueAsLong(key);

			instance.setDate(context, value);
		}
	}

	private void setupDateAndTextEntry(Context context)
	{
		List<Instance> modelInstances = model.instances;
		if (modelInstances != null && !modelInstances.isEmpty())
		{
			// we only use the first instance for date
			instanceCount = 1;
			instances = modelInstances;

			Instance instance = instances.get(0);
			String key = repository.keyForBlockIndex(blockIndex, stageIndex, checkpointIndex, 0) + "_date";
			long value = entries.getValueAsLong(key);

			instance.setDate(context, value);
			instance.textEntry.set(
				entries.getValue(repository.keyForBlockIndex(blockIndex, stageIndex, checkpointIndex, 0)));
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
			nextText = resources.getString(R.string.checkpoint_next_onward, blockIndex + 2);
		}
		else
		{
			description = resources.getString(R.string.checkpoint_next_message);
			nextText = resources.getString(R.string.checkpoint_next_keep_going);
		}

		// don't show button if route CP for the last block
		showNextText = (!EntryType.route.equals(model.entryType) ||
			blockIndex != repository.getCountOfBlocks() - 1);
	}

	public boolean showInstance(int instance)
	{
		return instances != null && instance >= 0 && instance < instanceCount;
	}

	public Instance getInstance(int instance)
	{
		return showInstance(instance) ? instances.get(instance) : null;
	}

	public String getInstancePrompt(int instance)
	{
		return showInstance(instance) ? instances.get(instance).getPrompt() : null;
	}

	public void onNextClick()
	{
		if (EntryType.route.equals(model.entryType))
		{
			// make sure we have a block file to go to
			if (!TextUtils.isEmpty(model.routeFileName))
			{
				repository.addTrace(String.format(Locale.US, "nextBlockEvent routing to %s", model.routeFileName));

				ChecklistState state = new ChecklistState(model.routeFileName, blockIndex + 1);
				nextBlockEvent.setValue(state);
			}
		}
		else
		{
			// make sure we have a Stage to go to
			Stage nextStage = repository.getStage(blockIndex, stageIndex + 1);
			if (nextStage != null)
			{
				repository.addTrace(repository.keyForBlockIndex(stageIndex + 1, 0));

				ChecklistState state = new ChecklistState(blockIndex, stageIndex + 1);
				nextStageEvent.setValue(state);
			}
		}
	}

	public boolean onShowDebug(View view)
	{
		Toast.makeText(view.getContext(),
			String.format(Locale.US, "Checkpoint key %s", repository.keyForBlockIndex(stageIndex, checkpointIndex)),
			Toast.LENGTH_LONG).
			show();

		return true;
	}

	public void onShowDatePicker(Instance instance)
	{
		if (instance != null)
			showDatePicker.setValue(instance);
	}

	public void setDate(@NonNull Context context, int year, int month, int day)
	{
		if (instances != null && !instances.isEmpty())
		{
			Instance instance = instances.get(0);
			instance.setDate(context, year, month, day);
		}
	}

	public void checkpointSelected()
	{
		repository.markVisited(stageIndex, checkpointIndex);
	}

	public void saveCheckpointEntries()
	{
		model = repository.getCheckpoint(blockIndex, stageIndex, checkpointIndex);
		if (model != null)
		{
			switch (model.entryType)
			{
			case info:
				// no-op
				break;
			case field:
			{
				if (instances != null)
				{
					for (int i = 0; i < instances.size(); i++)
					{
						entries.setValue(
							repository.keyForBlockIndex(blockIndex, stageIndex, checkpointIndex, i),
							instances.get(i).textEntry.get());
					}
				}
				break;
			}
			case checkbox:
			case radio:
			{
				if (instances != null)
				{
					for (int i = 0; i < instances.size(); i++)
					{
						entries.setValue(
							repository.keyForBlockIndex(blockIndex, stageIndex, checkpointIndex, i),
							instances.get(i).isChecked);
					}
				}
				break;
			}
			case dateOnly:
			{
				if (instances != null && !instances.isEmpty())
				{
					Instance instance = instances.get(0);

					String key = repository.keyForBlockIndex(blockIndex, stageIndex, checkpointIndex, 0) + "_date";
					entries.setValue(key, instance.dateValue);
				}
				break;
			}
			case dateAndText:
			{
				if (instances != null && !instances.isEmpty())
				{
					Instance instance = instances.get(0);

					String key = repository.keyForBlockIndex(blockIndex, stageIndex, checkpointIndex, 0);
					entries.setValue(key, instance.textEntry.get());
					key += "_date";
					entries.setValue(key, instance.dateValue);
				}
				break;
			}
			case route:
			case nextstage:
				break;
			}
		}
	}

	public boolean showEntryType(EntryType entryType, boolean verifyInstances)
	{
		boolean show = false;

		if (entryType != null && model != null)
		{
			show = entryType.equals(model.entryType);
			if (show && verifyInstances)
				show = (instances != null && !instances.isEmpty());
		}

		return show;
	}

	public void onUrlClick()
	{
		if (TextUtils.isEmpty(model.url))
			return;

		WebViewActivity.startActivity(getApplication(), model.url);
	}
}

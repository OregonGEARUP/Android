package org.oregongoestocollege.itsaplan.viewmodel;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.content.res.Resources;
import android.databinding.ObservableField;
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
import org.oregongoestocollege.itsaplan.data.NavigationState;
import org.oregongoestocollege.itsaplan.data.Stage;
import org.oregongoestocollege.itsaplan.data.UserEntries;
import org.oregongoestocollege.itsaplan.data.UserEntriesInterface;
import org.oregongoestocollege.itsaplan.data.dao.DateConverter;

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
	private final CheckpointInterface repository;
	// view data
	public final ObservableField<DateViewModel> dateOnlyVm = new ObservableField<>();
	public final ObservableField<DateViewModel> dateAndTextVm = new ObservableField<>();
	private final SingleLiveEvent<ChecklistState> nextStageEvent = new SingleLiveEvent<>();
	private final SingleLiveEvent<ChecklistState> nextBlockEvent = new SingleLiveEvent<>();
	private final SingleLiveEvent<NavigationState> navigationEvent = new SingleLiveEvent<>();
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
	}

	public void start(Context context, int blockIndex, int stageIndex, int checkpointIndex)
	{
		this.blockIndex = blockIndex;
		this.stageIndex = stageIndex;
		this.checkpointIndex = checkpointIndex;

		UserEntriesInterface entries = new UserEntries(context);

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
				setupFieldEntry(entries);
				break;
			case checkbox:
			case radio:
				setupCheckboxAndRadioEntry(entries);
				break;
			case dateOnly:
				setupDateOnlyEntry(context, entries);
				break;
			case dateAndText:
				setupDateAndTextEntry(context, entries);
				break;
			case route:
			case nextstage:
				setupRouteEntry(context);
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

	public SingleLiveEvent<NavigationState> getNavigationEvent() { return navigationEvent; }

	private void setupFieldEntry(@NonNull UserEntriesInterface entries)
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

	private void setupCheckboxAndRadioEntry(@NonNull UserEntriesInterface entries)
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

	private void setupDateOnlyEntry(Context context, @NonNull UserEntriesInterface entries)
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
			Date date = value > 0 ? DateConverter.toDate(value) : null;

			dateOnlyVm.set(DateViewModel.build(context, date, instance.getPrompt()));
		}
	}

	private void setupDateAndTextEntry(Context context, @NonNull UserEntriesInterface entries)
	{
		List<Instance> modelInstances = model.instances;
		if (modelInstances != null && !modelInstances.isEmpty())
		{
			// we only use the first instance for date
			instanceCount = 1;
			instances = modelInstances;

			Instance instance = instances.get(0);
			final String baseKey = repository.keyForBlockIndex(blockIndex, stageIndex, checkpointIndex, 0);

			long value = entries.getValueAsLong(baseKey + "_date");
			Date date = value > 0 ? DateConverter.toDate(value) : null;
			dateAndTextVm.set(DateViewModel.build(context, date, instance.getPrompt()));

			instance.textEntry.set(entries.getValue(baseKey + "_text"));
		}
	}

	private void setupRouteEntry(Context context)
	{
		Resources resources = context.getResources();

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

	public void checkpointSelected()
	{
		repository.markVisited(stageIndex, checkpointIndex);
	}

	public void saveCheckpointEntries(@NonNull UserEntriesInterface entries)
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
					DateViewModel vm = dateOnlyVm.get();
					if (vm != null && vm.isDirty())
					{
						Long date = DateConverter.toTimestamp(dateOnlyVm.get().getSelectedDate());
						String key = repository.keyForBlockIndex(blockIndex, stageIndex, checkpointIndex, 0) + "_date";
						entries.setValue(key, date != null ? date : 0);
					}
				}
				break;
			}
			case dateAndText:
			{
				if (instances != null && !instances.isEmpty())
				{
					final String baseKey = repository.keyForBlockIndex(blockIndex, stageIndex, checkpointIndex, 0);
					Instance instance = instances.get(0);

					DateViewModel vm = dateAndTextVm.get();
					if (vm != null && vm.isDirty())
					{
						Long date = DateConverter.toTimestamp(dateAndTextVm.get().getSelectedDate());
						entries.setValue(baseKey + "_date", date != null ? date : 0);
					}

					entries.setValue(baseKey + "_text", instance.textEntry.get());
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
		final String url = model.url;
		if (TextUtils.isEmpty(url))
			return;

		//  check for special app destination URLs first
		if (url.startsWith("itsaplan://myplan/"))
		{
			String option = MyPlanViewModel.getOptionFromUrl(url);

			// switch to My Plan tab
			navigationEvent.setValue(new NavigationState(1, option));
		}
		else if (url.startsWith("itsaplan://passwords"))
		{
			// switch to Passwords tab
			navigationEvent.setValue(new NavigationState(2, null));
		}
		else if (url.startsWith("itsaplan://info"))
		{
			// switch to Info tab
			navigationEvent.setValue(new NavigationState(3, null));
		}
		else
		{
			// open web page for URL
			WebViewActivity.startActivity(getApplication(), url);
		}
	}

	// workaround for android:onClick="@{(view) -> uxContent.dateAndTextVm.onChangeDate(view)}"
	// which caused dateAndTextVm to be null and using dateAndTextVm.get() works but gives compiler warning
	public void onChangeDateAndText(View view)
	{
		dateAndTextVm.get().onChangeDate(view);
	}
}

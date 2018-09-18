package org.oregongoestocollege.itsaplan.viewmodel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.content.res.Resources;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Toast;

import org.oregongoestocollege.itsaplan.R;
import org.oregongoestocollege.itsaplan.SingleLiveEvent;
import org.oregongoestocollege.itsaplan.Utils;
import org.oregongoestocollege.itsaplan.WebViewActivity;
import org.oregongoestocollege.itsaplan.data.ChecklistState;
import org.oregongoestocollege.itsaplan.data.Checkpoint;
import org.oregongoestocollege.itsaplan.data.CheckpointInterface;
import org.oregongoestocollege.itsaplan.data.CheckpointRepository;
import org.oregongoestocollege.itsaplan.data.EntryType;
import org.oregongoestocollege.itsaplan.data.Instance;
import org.oregongoestocollege.itsaplan.data.NavigationState;
import org.oregongoestocollege.itsaplan.data.Stage;
import org.oregongoestocollege.itsaplan.data.UserEntries;
import org.oregongoestocollege.itsaplan.data.UserEntriesInterface;
import org.oregongoestocollege.itsaplan.data.dao.DateConverter;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class CheckpointViewModel extends AndroidViewModel
{
	private static final String LOG_TAG = "GearUp_CheckpointViewModel";
	private static final Pattern PATTERN_TEMPLATE = Pattern.compile("##[^(##)]+##");
	private static final int MAX_DATES = 3;
	// service data
	private Checkpoint model;
	private int blockIndex;
	private int stageIndex;
	private int checkpointIndex;
	private final CheckpointInterface repository;
	// events
	private final SingleLiveEvent<ChecklistState> nextStageEvent = new SingleLiveEvent<>();
	private final SingleLiveEvent<ChecklistState> nextBlockEvent = new SingleLiveEvent<>();
	private final SingleLiveEvent<NavigationState> navigationEvent = new SingleLiveEvent<>();
	private boolean finalCheckpoint;
	private int entryLayout;
	// view data
	private List<InstanceFieldViewModel> fieldVms;
	private final List<ObservableField<DateViewModel>> dateOnlyVms;
	private final List<ObservableField<DateViewModel>> dateAndTextVms;
	public final ObservableBoolean showIncomplete = new ObservableBoolean();
	public String title;
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

	public CheckpointViewModel(@NonNull Application context)
	{
		// To avoid leaks, force use of application context
		super(context);

		this.repository = CheckpointRepository.getInstance(context);

		dateOnlyVms = new ArrayList<>(MAX_DATES);
		dateAndTextVms = new ArrayList<>(MAX_DATES);
	}

	public void init(Context context, int blockIndex, int stageIndex, int checkpointIndex)
	{
		// only need to init once, view model is associated to the fragment and retained
		if (this.blockIndex == blockIndex && this.stageIndex == stageIndex && this.checkpointIndex == checkpointIndex)
			return;

		this.blockIndex = blockIndex;
		this.stageIndex = stageIndex;
		this.checkpointIndex = checkpointIndex;

		Utils.d(LOG_TAG, "init blockIndex:%d, stageIndex:%d, checkpointIndex:%d",
			blockIndex, stageIndex, checkpointIndex);

		UserEntriesInterface entries = new UserEntries(context);

		model = repository.getCheckpoint(blockIndex, stageIndex, checkpointIndex);
		if (model == null)
			return;

		// setup defaults
		title = stringWithSubstitutions(model.title, entries);
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
			entryLayout = R.layout.layout_entry_field;
			setupFieldEntry(entries);
			break;
		case checkbox:
			entryLayout = R.layout.layout_entry_checkbox;
			setupCheckboxAndRadioEntry(entries);
			break;
		case radio:
			entryLayout = R.layout.layout_entry_radio;
			setupCheckboxAndRadioEntry(entries);
			break;
		case dateOnly:
			entryLayout = R.layout.layout_entry_date_only;
			setupDateOnlyEntry(context, entries);
			break;
		case dateAndText:
			entryLayout = R.layout.layout_entry_date_text;
			setupDateAndTextEntry(context, entries);
			break;
		case route:
		case nextstage:
			setupRouteEntry(context);
			break;
		}
	}

	private String stringWithSubstitutions(String original, UserEntriesInterface entries)
	{
		if (!TextUtils.isEmpty(original))
		{
			// replacing values with patterns such as
			// "##b2_s3_cp2_i1_text##",
			// "##b2_s3_cp2_i1_text##, due ##b2_s3_cp2_i1_date##"

			Matcher matcher = PATTERN_TEMPLATE.matcher(original);
			StringBuffer sb = new StringBuffer();
			while (matcher.find())
			{
				String substring = original.substring(matcher.start(), matcher.end());
				int length = substring.length();
				if (length > 4)
				{
					String key = substring.substring(2, length - 2);
					String replacement = null;

					if (key.endsWith("_date"))
					{
						long value = entries.getValueAsLong(key);
						Date date = value > 0 ? DateConverter.toDate(value) : null;
						if (date != null)
							replacement = DateFormat.getLongDateFormat(getApplication()).format(date);
					}
					else
						replacement = entries.getValue(key);

					if (TextUtils.isEmpty(replacement))
						replacement = String.format(Locale.getDefault(), "<< missing value for (%s) >>", key);

					matcher.appendReplacement(sb, replacement);
				}
			}
			matcher.appendTail(sb);
			if (sb.length() > 0)
				return sb.toString();
		}

		return original;
	}

	public boolean isFinalCheckpoint()
	{
		return finalCheckpoint;
	}

	public int getEntryLayout()
	{
		return entryLayout;
	}

	public SingleLiveEvent<ChecklistState> getNextStageEvent()
	{
		return nextStageEvent;
	}

	public SingleLiveEvent<ChecklistState> getNextBlockEvent()
	{
		return nextBlockEvent;
	}

	public SingleLiveEvent<NavigationState> getNavigationEvent()
	{
		return navigationEvent;
	}

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

				fieldVms = new ArrayList<>(instanceCount);

				for (int i = 0; i < instanceCount; i++)
				{
					String key = repository.keyForBlockIndex(blockIndex, stageIndex, checkpointIndex, i);
					fieldVms.add(new InstanceFieldViewModel(instances.get(i), key, entries.getValue(key)));
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

				for (int i = 0; i < instanceCount; i++)
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
			int size = modelInstances.size();
			if (size > 0)
			{
				Instance instance;

				// limit instances to our maximum
				instanceCount = size > MAX_DATES ? MAX_DATES : size;
				instances = modelInstances.subList(0, instanceCount);

				for (int i = 0; i < instanceCount; i++)
				{
					instance = instances.get(i);
					String key = repository.keyForBlockIndex(blockIndex, stageIndex, checkpointIndex, i) + "_date";
					long value = entries.getValueAsLong(key);
					Date date = value > 0 ? DateConverter.toDate(value) : null;

					dateOnlyVms.add(new ObservableField<>(DateViewModel.build(context, date, instance.getPrompt())));
				}
			}
		}
	}

	private void setupDateAndTextEntry(Context context, @NonNull UserEntriesInterface entries)
	{
		List<Instance> modelInstances = model.instances;
		if (modelInstances != null && !modelInstances.isEmpty())
		{
			int size = modelInstances.size();
			if (size > 0)
			{
				Instance instance;

				// limit instances to our maximum
				instanceCount = size > MAX_DATES ? MAX_DATES : size;
				instances = modelInstances.subList(0, instanceCount);

				fieldVms = new ArrayList<>(instanceCount);

				for (int i = 0; i < instanceCount; i++)
				{
					instance = instances.get(i);
					final String baseKey = repository.keyForBlockIndex(blockIndex, stageIndex, checkpointIndex, i);

					long value = entries.getValueAsLong(baseKey + "_date");
					Date date = value > 0 ? DateConverter.toDate(value) : null;
					dateAndTextVms.add(new ObservableField<>(DateViewModel.build(context, date, instance.getPrompt())));

					String textKey = baseKey + "_text";
					fieldVms.add(new InstanceFieldViewModel(instance, textKey, entries.getValue(textKey)));
				}
			}
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

		boolean lastBlock = (blockIndex == repository.getCountOfBlocks() - 1);

		// don't show button if route CP for the last block
		showNextText = (!EntryType.route.equals(model.entryType) || !lastBlock);

		// and set the flag specially for final block / stage / checkpoint
		finalCheckpoint = (EntryType.route == model.entryType && lastBlock);
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

	public boolean hasInstancePrompt(int instance)
	{
		return showInstance(instance) && !TextUtils.isEmpty(instances.get(instance).getPrompt());
	}

	// equivalent of iOS isCurrentCheckpointCompleted()
	public boolean isCompleted()
	{
		if (model == null || !model.required())
			return true;

		switch (model.entryType)
		{
		case info:
		case route:
		case nextstage:
			return true;
		case checkbox:
		case radio:
		{
			boolean oneSelected = false;
			for (int i = 0; i < instanceCount && !oneSelected; i++)
				oneSelected = instances.get(i).isChecked;
			return oneSelected;
		}
		case field:
		{
			boolean allFilled = true;
			for (int i = 0; i < instanceCount; i++)
			{
				allFilled = fieldVms.get(i).hasText();
				if (!allFilled)
					break;
			}
			return allFilled;
		}
		case dateOnly:
		{
			boolean allFilled = true;
			for (int i = 0; i < instanceCount; i++)
			{
				allFilled = dateOnlyVms.get(i).get().hasSelectedDate();
				if (!allFilled)
					break;
			}
			return allFilled;
		}
		case dateAndText:
		{
			boolean allFilled = true;
			for (int i = 0; i < instanceCount; i++)
			{
				allFilled = dateAndTextVms.get(i).get().hasSelectedDate() && fieldVms.get(i).hasText();
				if (!allFilled)
					break;
			}
			return allFilled;
		}
		}

		return true;
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
					for (int i = 0; i < instanceCount; i++)
					{
						InstanceFieldViewModel fieldVm = fieldVms.get(i);
						if (fieldVm.isDirty())
						{
							entries.setValue(fieldVm.key, fieldVm.text.get());
							fieldVm.saved();
						}
					}
				}
				break;
			}
			case checkbox:
			case radio:
			{
				if (instances != null)
				{
					for (int i = 0; i < instanceCount; i++)
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
				if (instances != null)
				{
					for (int i = 0; i < instanceCount; i++)
					{
						DateViewModel vm = dateOnlyVms.get(i).get();
						if (vm != null && vm.isDirty())
						{
							Long date = DateConverter.toTimestamp(vm.getSelectedDate());
							String key = repository.keyForBlockIndex(blockIndex, stageIndex, checkpointIndex, i) + "_date";
							entries.setValue(key, date != null ? date : 0);
						}
					}
				}
				break;
			}
			case dateAndText:
			{
				if (instances != null)
				{
					for (int i = 0; i < instanceCount; i++)
					{
						final String baseKey = repository.keyForBlockIndex(blockIndex, stageIndex, checkpointIndex, i);

						DateViewModel dateVm = dateAndTextVms.get(i).get();
						if (dateVm != null && dateVm.isDirty())
						{
							Long date = DateConverter.toTimestamp(dateVm.getSelectedDate());
							entries.setValue(baseKey + "_date", date != null ? date : 0);
						}

						InstanceFieldViewModel fieldVm = fieldVms.get(i);
						if (fieldVm.isDirty())
						{
							entries.setValue(fieldVm.key, fieldVm.text.get());
							fieldVm.saved();
						}
					}
				}
				break;
			}
			case route:
			case nextstage:
				break;
			}
		}
	}

	public void onUrlClick()
	{
		final String url = model.url;
		if (TextUtils.isEmpty(url))
			return;

		//  check for special app destination URLs first
		if (url.startsWith("itsaplan://myplan/"))
		{
			String option = MyPlanNavViewModel.getOptionFromUrl(url);

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

	// workaround for android:onClick="@{(view) -> uxContent.getDateAndTextViewModel(i).onChangeDate(view)}"
	// which caused dateAndTextVm to be null and using dateAndTextVm.get() works but gives compiler warning
	public void onChangeDateAndText(View view, int instance)
	{
		if (dateAndTextVms != null && instance >= 0 && instance < dateAndTextVms.size())
		{
			DateViewModel dateViewModel = dateAndTextVms.get(instance).get();
			dateViewModel.onChangeDate(view);
		}
	}

	public ObservableField<DateViewModel> getDateOnlyViewModel(int instance)
	{
		if (dateOnlyVms != null && instance >= 0 && instance < dateOnlyVms.size())
			return dateOnlyVms.get(instance);

		return null;
	}

	public ObservableField<DateViewModel> getDateAndTextViewModel(int instance)
	{
		if (dateAndTextVms != null && instance >= 0 && instance < dateAndTextVms.size())
			return dateAndTextVms.get(instance);

		return null;
	}

	public InstanceFieldViewModel getFieldViewModel(int instance)
	{
		if (fieldVms != null && instance >= 0 && instance < fieldVms.size())
			return fieldVms.get(instance);

		return null;
	}
}

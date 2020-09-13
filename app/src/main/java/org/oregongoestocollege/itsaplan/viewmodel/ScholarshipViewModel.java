package org.oregongoestocollege.itsaplan.viewmodel;

import android.content.Context;
import android.content.DialogInterface;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;

import org.oregongoestocollege.itsaplan.R;
import org.oregongoestocollege.itsaplan.Utils;
import org.oregongoestocollege.itsaplan.data.MyPlanRepository;
import org.oregongoestocollege.itsaplan.data.Scholarship;
import org.oregongoestocollege.itsaplan.support.BindingItem;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Oregon GEAR UP App
 * Copyright © 2020 Oregon GEAR UP. All rights reserved.
 */
public class ScholarshipViewModel implements BindingItem
{
	private static final String LOG_TAG = "GearUp_ScholarshipViewModel";
	private MyPlanRepository repository;
	private final Scholarship scholarship;
	private final boolean removable;
	// UX fields
	private boolean initialized = false;
	public final ObservableField<String> name = new ObservableField<>();
	public final ObservableField<DateViewModel> applicationDateVm = new ObservableField<>();
	public final ObservableField<String> website = new ObservableField<>();
	public final ObservableField<String> otherInfo = new ObservableField<>();
	public final ObservableBoolean essayDone = new ObservableBoolean();
	public final ObservableBoolean recommendationsDone = new ObservableBoolean();
	public final ObservableBoolean activitiesChartDone = new ObservableBoolean();
	public final ObservableBoolean testsDone = new ObservableBoolean();
	public final ObservableBoolean applicationDone = new ObservableBoolean();

	public ScholarshipViewModel(@NonNull MyPlanRepository repository, @NonNull Scholarship scholarship, boolean removable)
	{
		this.repository = checkNotNull(repository);
		this.scholarship = checkNotNull(scholarship);
		this.removable = removable;

		applicationDateVm.set(new DateViewModel(scholarship.getApplicationDate(), R.string.application_deadline));
	}

	private boolean isDirty()
	{
		return applicationDateVm.get().isDirty() ||
			!TextUtils.equals(name.get(), scholarship.getName()) ||
			!TextUtils.equals(website.get(), scholarship.getWebsite()) ||
			!TextUtils.equals(otherInfo.get(), scholarship.getOtherInfo()) ||
			essayDone.get() != scholarship.isEssayDone() ||
			recommendationsDone.get() != scholarship.isRecommendationsDone() ||
			activitiesChartDone.get() != scholarship.isActivitiesChartDone() ||
			testsDone.get() != scholarship.isTestsDone() ||
			applicationDone.get() != scholarship.isApplicationDone();
	}

	private void remove()
	{
		repository.delete(scholarship);
	}

	@Override
	public int getLayoutId()
	{
		return R.layout.item_scholarship;
	}

	@Override
	public void onBind(@NonNull Context context)
	{
		if (initialized)
			return;

		applicationDateVm.get().onBind(context);

		name.set(scholarship.getName());
		website.set(scholarship.getWebsite());
		otherInfo.set(scholarship.getOtherInfo());
		essayDone.set(scholarship.isEssayDone());
		recommendationsDone.set(scholarship.isRecommendationsDone());
		activitiesChartDone.set(scholarship.isActivitiesChartDone());
		testsDone.set(scholarship.isTestsDone());
		applicationDone.set(scholarship.isApplicationDone());

		initialized = true;
	}

	public void update()
	{
		if (!isDirty())
			return;

		if (Utils.DEBUG)
			Utils.d(LOG_TAG, "Saving %s to database", name.get());

		scholarship.setName(name.get());
		scholarship.setApplicationDate(applicationDateVm.get().getSelectedDate());
		scholarship.setWebsite(website.get());
		scholarship.setOtherInfo(otherInfo.get());
		scholarship.setEssayDone(essayDone.get());
		scholarship.setRecommendationsDone(recommendationsDone.get());
		scholarship.setActivitiesChartDone(activitiesChartDone.get());
		scholarship.setTestsDone(testsDone.get());
		scholarship.setApplicationDone(applicationDone.get());

		applicationDateVm.get().saved();

		repository.update(scholarship);
	}

	public boolean isNotificationDirty()
	{
		return applicationDateVm.get().isDirty() ||
			!TextUtils.equals(name.get(), scholarship.getName());
	}

	public boolean isRemovable()
	{
		return removable;
	}

	public void onRemoveScholarship(View view)
	{
		if (!removable)
			return;

		final Context context = view.getContext();

		new AlertDialog.Builder(context)
			.setTitle(context.getString(R.string.remove_scholarship))
			.setMessage(context.getString(R.string.remove_scholarship_confirm, scholarship.getName()))
			.setPositiveButton(context.getString(R.string.remove), new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int whichButton)
				{
					remove();
				}
			})
			.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int whichButton)
				{
				}
			})
			.show();
	}
}

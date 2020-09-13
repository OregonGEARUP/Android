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
import org.oregongoestocollege.itsaplan.data.College;
import org.oregongoestocollege.itsaplan.data.MyPlanRepository;
import org.oregongoestocollege.itsaplan.support.BindingItem;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Oregon GEAR UP App
 * Copyright © 2020 Oregon GEAR UP. All rights reserved.
 */
public class CollegeViewModel implements BindingItem
{
	static final String LOG_TAG = "GearUp_CollegeViewModel";
	private MyPlanRepository repository;
	private final College college;
	private final boolean removable;
	// UX fields
	private boolean initialized = false;
	public final ObservableField<String> name = new ObservableField<>();
	public final ObservableField<DateViewModel> applicationDateVm = new ObservableField<>();
	public final ObservableField<String> averageNetPrice = new ObservableField<>();
	public final ObservableField<String> applicationCost = new ObservableField<>();
	public final ObservableBoolean essayDone = new ObservableBoolean();
	public final ObservableBoolean recommendationsDone = new ObservableBoolean();
	public final ObservableBoolean activitiesChartDone = new ObservableBoolean();
	public final ObservableBoolean testsDone = new ObservableBoolean();
	public final ObservableBoolean addlFinancialAidDone = new ObservableBoolean();
	public final ObservableBoolean addlScholarshipDone = new ObservableBoolean();
	public final ObservableBoolean feeDeferralDone = new ObservableBoolean();
	public final ObservableBoolean applicationDone = new ObservableBoolean();

	public CollegeViewModel(@NonNull MyPlanRepository repository, @NonNull College college, boolean removable)
	{
		this.repository = checkNotNull(repository);
		this.college = checkNotNull(college);
		this.removable = removable;

		applicationDateVm.set(new DateViewModel(college.getApplicationDate(), R.string.application_deadline));
	}

	private boolean isDirty()
	{
		return applicationDateVm.get().isDirty() ||
			!TextUtils.equals(name.get(), college.getName()) ||
			!TextUtils.equals(averageNetPrice.get(), college.getAverageNetPrice()) ||
			!TextUtils.equals(applicationCost.get(), college.getApplicationCost()) ||
			essayDone.get() != college.isEssayDone() ||
			recommendationsDone.get() != college.isRecommendationsDone() ||
			activitiesChartDone.get() != college.isActivitiesChartDone() ||
			testsDone.get() != college.isTestsDone() ||
			addlFinancialAidDone.get() != college.isAddlFinancialAidDone() ||
			addlScholarshipDone.get() != college.isAddlScholarshipDone() ||
			feeDeferralDone.get() != college.isFeeDeferralDone() ||
			applicationDone.get() != college.isApplicationDone();
	}

	private void remove()
	{
		repository.delete(college);
	}

	@Override
	public int getLayoutId()
	{
		return R.layout.item_college;
	}

	@Override
	public void onBind(@NonNull Context context)
	{
		if (initialized)
			return;

		applicationDateVm.get().onBind(context);

		name.set(college.getName());
		averageNetPrice.set(college.getAverageNetPrice());
		applicationCost.set(college.getApplicationCost());
		essayDone.set(college.isEssayDone());
		recommendationsDone.set(college.isRecommendationsDone());
		activitiesChartDone.set(college.isActivitiesChartDone());
		testsDone.set(college.isTestsDone());
		addlFinancialAidDone.set(college.isAddlFinancialAidDone());
		addlScholarshipDone.set(college.isAddlScholarshipDone());
		feeDeferralDone.set(college.isFeeDeferralDone());
		applicationDone.set(college.isApplicationDone());

		initialized = true;
	}

	public void update()
	{
		if (!isDirty())
			return;

		if (Utils.DEBUG)
			Utils.d(LOG_TAG, "Saving %s to database", name.get());

		college.setName(name.get());
		college.setApplicationDate(applicationDateVm.get().getSelectedDate());
		college.setAverageNetPrice(averageNetPrice.get());
		college.setApplicationCost(applicationCost.get());
		college.setEssayDone(essayDone.get());
		college.setRecommendationsDone(recommendationsDone.get());
		college.setActivitiesChartDone(activitiesChartDone.get());
		college.setTestsDone(testsDone.get());
		college.setAddlFinancialAidDone(addlFinancialAidDone.get());
		college.setAddlScholarshipDone(addlScholarshipDone.get());
		college.setFeeDeferralDone(feeDeferralDone.get());
		college.setApplicationDone(applicationDone.get());

		applicationDateVm.get().saved();

		repository.update(college);
	}

	public boolean isNotificationDirty()
	{
		return applicationDateVm.get().isDirty() ||
			!TextUtils.equals(name.get(), college.getName());
	}

	public boolean isRemovable()
	{
		return removable;
	}

	public void onRemoveCollege(View view)
	{
		if (!removable)
			return;

		final Context context = view.getContext();

		new AlertDialog.Builder(context)
			.setTitle(context.getString(R.string.remove_college))
			.setMessage(context.getString(R.string.remove_college_confirm, college.getName()))
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

package org.oregongoestocollege.itsaplan.viewmodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;

import org.oregongoestocollege.itsaplan.AddLayoutView;
import org.oregongoestocollege.itsaplan.R;
import org.oregongoestocollege.itsaplan.Utils;
import org.oregongoestocollege.itsaplan.data.College;
import org.oregongoestocollege.itsaplan.data.MyPlanRepository;
import org.oregongoestocollege.itsaplan.data.UserEntries;
import org.oregongoestocollege.itsaplan.data.UserEntriesInterface;
import org.oregongoestocollege.itsaplan.data.dao.DateConverter;
import org.oregongoestocollege.itsaplan.support.BindingItem;

/**
 * Oregon GEAR UP App
 * Copyright © 2020 Oregon GEAR UP. All rights reserved.
 */
public class CollegesViewModel extends AndroidViewModel
{
	// debug
	private static int count = 0;
	private final int mycount;
	// data
	private final MyPlanRepository repository;
	private final LiveData<List<College>> allColleges;
	private List<CollegeViewModel> allViewModels;
	private boolean refreshNotifications;

	public CollegesViewModel(@NonNull Application application)
	{
		super(application);

		mycount = ++count;
		if (Utils.DEBUG)
			Utils.d(CollegeViewModel.LOG_TAG, "create %d", mycount);

		repository = MyPlanRepository.getInstance(application);
		allColleges = repository.getAllColleges();
	}

	@Override
	protected void onCleared()
	{
		if (Utils.DEBUG)
			Utils.d(CollegeViewModel.LOG_TAG, "onCleared() %d", mycount);
	}

	private void save(@Nullable String name)
	{
		if (!TextUtils.isEmpty(name))
		{
			// save it to our database which will trigger a reload
			repository.insertCollege(name);
		}
	}

	private boolean checkFirstCollege(@NonNull Context context, @NonNull College college)
	{
		UserEntriesInterface userEntries = new UserEntries(context);

		boolean dirty = false;

		String firstCollegePlaceholder = context.getString(R.string.college_1);

		// determine what college name was entered in the checkpoint
		String value = userEntries.getValue("b2_s3_cp2_i1_text");
		if (TextUtils.isEmpty(value))
			value = firstCollegePlaceholder;

		// fill in any missing pieces of the first college from the checkpoints
		String collegeName = college.getName();
		if (TextUtils.isEmpty(collegeName) ||
			(TextUtils.equals(collegeName, firstCollegePlaceholder) &&
				!TextUtils.equals(value, firstCollegePlaceholder) ))
		{
			college.setName(value);
			dirty = true;
		}

		long appDate = userEntries.getValueAsLong("b2_s3_cp2_i1_date");
		if (appDate > 0 && !college.hasApplicationDate())
		{
			college.setApplicationDate(DateConverter.toDate(appDate));
			dirty = true;
		}

		String netPrice = college.getAverageNetPrice();
		if (TextUtils.isEmpty(netPrice))
		{
			value = userEntries.getValue("b3citizen_s1_cp3_i1");
			if (!TextUtils.isEmpty(value))
			{
				college.setAverageNetPrice(value);
				dirty = true;
			}
			else if (!TextUtils.isEmpty(value = userEntries.getValue("b3undoc_s1_cp3_i1")))
			{
				college.setAverageNetPrice(value);
				dirty = true;
			}
			else if (!TextUtils.isEmpty(value = userEntries.getValue("b3visa_s1_cp3_i1")))
			{
				college.setAverageNetPrice(value);
				dirty = true;
			}
		}

		return dirty;
	}

	public boolean checkFirstCollege(@Nullable Context context, List<College> colleges)
	{
		if (context == null || colleges == null || colleges.isEmpty())
			return false;

		// if we have any user entered data that doesn't match the college data, update it
		College college = colleges.get(0);
		boolean dirty = checkFirstCollege(context, college);
		if (dirty)
			repository.update(college);

		return dirty;
	}

	public LiveData<List<College>> getAllColleges()
	{
		return allColleges;
	}

	public List<BindingItem> getItems(List<College> colleges)
	{
		List<BindingItem> itemViewModels;

		if (colleges != null && !colleges.isEmpty())
		{
			int size = colleges.size();
			itemViewModels = new ArrayList<>(size);
			allViewModels = new ArrayList<>(size);

			for (int i = 0; i < size; i++)
			{
				CollegeViewModel vm = new CollegeViewModel(repository, colleges.get(i), size > 1);
				itemViewModels.add(vm);
				allViewModels.add(vm);
			}
		}
		else
		{
			itemViewModels = Collections.emptyList();
			allViewModels = null;
		}

		return itemViewModels;
	}

	public void onAddCollege(View view)
	{
		final Context context = view.getContext();
		final AddLayoutView addView = new AddLayoutView(context, R.string.college_name_hint);

		new AlertDialog.Builder(context)
			.setTitle(context.getString(R.string.add_college))
			.setMessage(context.getString(R.string.add_college_message))
			.setView(addView)
			.setPositiveButton(context.getString(R.string.add), (dialog, whichButton) -> save(addView.getAddedText()))
			.setNegativeButton(context.getString(R.string.cancel), (dialog, whichButton) -> { })
			.show();
	}

	public void update()
	{
		if (allViewModels != null)
		{
			for (CollegeViewModel allViewModel : allViewModels)
			{
				if (!refreshNotifications && allViewModel.isNotificationDirty())
					refreshNotifications = true;

				allViewModel.update();
			}
		}
	}

	public void stop()
	{
		if (allViewModels != null)
		{
			// update anything that has changed
			update();

			// then update related notifications
			repository.updateCollegeNotifications(getApplication(), refreshNotifications);

			// reset flag till the next time
			refreshNotifications = false;
		}
	}

	public void insertFirstCollege(@Nullable Context context)
	{
		if (context == null)
			return;

		// setup the first college with any user entered data
		College college = new College();
		checkFirstCollege(context, college);

		// insert the first college initialized with any user entered info
		repository.insertCollege(college);
	}
}

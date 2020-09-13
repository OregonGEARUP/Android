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
import org.oregongoestocollege.itsaplan.data.MyPlanRepository;
import org.oregongoestocollege.itsaplan.data.Scholarship;
import org.oregongoestocollege.itsaplan.data.UserEntries;
import org.oregongoestocollege.itsaplan.data.UserEntriesInterface;
import org.oregongoestocollege.itsaplan.data.dao.DateConverter;
import org.oregongoestocollege.itsaplan.support.BindingItem;

/**
 * Oregon GEAR UP App
 * Copyright © 2020 Oregon GEAR UP. All rights reserved.
 */
public class ScholarshipsViewModel extends AndroidViewModel
{
	private MyPlanRepository repository;
	private LiveData<List<Scholarship>> allScholarships;
	private List<ScholarshipViewModel> allViewModels;
	private boolean refreshNotifications;

	public ScholarshipsViewModel(@NonNull Application application)
	{
		super(application);

		repository = MyPlanRepository.getInstance(application);
		allScholarships = repository.getAllScholarships();
	}

	private void save(@Nullable String name)
	{
		if (!TextUtils.isEmpty(name))
		{
			// save it to our database which will trigger a reload
			repository.insertScholarship(name);
		}
	}

	private boolean checkFirstScholarship(@NonNull Context context, @NonNull Scholarship scholarship)
	{
		UserEntriesInterface userEntries = new UserEntries(context);

		boolean dirty = false;
		String value;

		if (!TextUtils.isEmpty(value = userEntries.getValue("b3citizen_s2_cp2_i1_text")))
		{
			if (!TextUtils.equals(scholarship.getName(), value))
			{
				scholarship.setName(value);
				dirty = true;
			}

			long appDate = userEntries.getValueAsLong("b3citizen_s2_cp2_i1_date");
			if (appDate > 0 && !scholarship.hasApplicationDate())
			{
				scholarship.setApplicationDate(DateConverter.toDate(appDate));
				dirty = true;
			}
		}
		else if (!TextUtils.isEmpty(value = userEntries.getValue("b3undoc_s2_cp2_i1_text")))
		{
			if (!TextUtils.equals(scholarship.getName(), value))
			{
				scholarship.setName(value);
				dirty = true;
			}

			long appDate = userEntries.getValueAsLong("b3undoc_s2_cp2_i1_date");
			if (appDate > 0 && !scholarship.hasApplicationDate())
			{
				scholarship.setApplicationDate(DateConverter.toDate(appDate));
				dirty = true;
			}
		}
		else if (!TextUtils.isEmpty(value = userEntries.getValue("b3visa_s2_cp2_i1_text")))
		{
			if (!TextUtils.equals(scholarship.getName(), value))
			{
				scholarship.setName(value);
				dirty = true;
			}

			long appDate = userEntries.getValueAsLong("b3visa_s2_cp2_i1_date");
			if (appDate > 0 && !scholarship.hasApplicationDate())
			{
				scholarship.setApplicationDate(DateConverter.toDate(appDate));
				dirty = true;
			}
		}
		else
		{
			value = context.getString(R.string.scholarship_1);
			if (!TextUtils.equals(scholarship.getName(), value))
			{
				scholarship.setName(value);
				dirty = true;
			}
		}

		return dirty;
	}

	public boolean checkFirstScholarship(@Nullable Context context, List<Scholarship> scholarships)
	{
		if (context == null || scholarships == null || scholarships.isEmpty())
			return false;

		// if we have any user entered data that doesn't match the college data, update it
		Scholarship scholarship = scholarships.get(0);
		boolean dirty = checkFirstScholarship(context, scholarship);
		if (dirty)
			repository.update(scholarship);

		return dirty;
	}

	public LiveData<List<Scholarship>> getAllScholarships()
	{
		return allScholarships;
	}

	public List<BindingItem> getItems(List<Scholarship> scholarships)
	{
		List<BindingItem> itemViewModels;

		if (scholarships != null && !scholarships.isEmpty())
		{
			int size = scholarships.size();
			itemViewModels = new ArrayList<>(size);
			allViewModels = new ArrayList<>(size);

			for (int i = 0; i < size; i++)
			{
				ScholarshipViewModel vm = new ScholarshipViewModel(repository, scholarships.get(i), size > 1);
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

	public void onAddScholarship(View view)
	{
		final Context context = view.getContext();
		final AddLayoutView addView = new AddLayoutView(context, R.string.scholarship_name_hint);

		new AlertDialog.Builder(context)
			.setTitle(context.getString(R.string.add_scholarship))
			.setMessage(context.getString(R.string.add_scholarship_message))
			.setView(addView)
			.setPositiveButton(context.getString(R.string.add), (dialog, whichButton) -> save(addView.getAddedText()))
			.setNegativeButton(context.getString(R.string.cancel), (dialog, whichButton) -> { })
			.show();
	}

	public void update()
	{
		if (allViewModels != null)
		{
			for (ScholarshipViewModel allViewModel : allViewModels)
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
			repository.updateScholarshipNotifications(getApplication(), refreshNotifications);

			// reset flag till the next time
			refreshNotifications = false;
		}
	}

	public void insertFirstScholarship(@Nullable Context context)
	{
		if (context == null)
			return;

		// setup the first scholarship with any user entered data
		Scholarship scholarship = new Scholarship();
		checkFirstScholarship(context, scholarship);

		// insert the first scholarship initialized with any user entered info
		repository.insertScholarship(scholarship);
	}
}

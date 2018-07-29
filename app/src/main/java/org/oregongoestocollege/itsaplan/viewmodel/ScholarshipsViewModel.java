package org.oregongoestocollege.itsaplan.viewmodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;

import org.oregongoestocollege.itsaplan.AddLayoutView;
import org.oregongoestocollege.itsaplan.R;
import org.oregongoestocollege.itsaplan.data.MyPlanRepository;
import org.oregongoestocollege.itsaplan.data.Scholarship;
import org.oregongoestocollege.itsaplan.support.BindingItem;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class ScholarshipsViewModel extends AndroidViewModel
{
	private MyPlanRepository repository;
	private LiveData<List<Scholarship>> allScholarships;
	private List<ScholarshipViewModel> allViewModels;

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
			.setPositiveButton(context.getString(R.string.add), new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int whichButton)
				{
					save(addView.getAddedText());
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

	public void update()
	{
		if (allViewModels != null)
		{
			for (ScholarshipViewModel allViewModel : allViewModels)
				allViewModel.update();
		}
	}
}

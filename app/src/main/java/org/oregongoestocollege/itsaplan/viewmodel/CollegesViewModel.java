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
import org.oregongoestocollege.itsaplan.Utils;
import org.oregongoestocollege.itsaplan.data.College;
import org.oregongoestocollege.itsaplan.data.MyPlanDatabase;
import org.oregongoestocollege.itsaplan.data.MyPlanRepository;
import org.oregongoestocollege.itsaplan.support.BindingItem;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class CollegesViewModel extends AndroidViewModel
{
	// debug
	private static int count = 0;
	private int mycount;
	// data
	private MyPlanRepository repository;
	private LiveData<List<College>> allColleges;
	private List<CollegeViewModel> allViewModels;

	public CollegesViewModel(@NonNull Application application)
	{
		super(application);

		mycount = ++count;
		Utils.d(CollegeViewModel.LOG_TAG, "create %d", mycount);

		repository = MyPlanRepository.getInstance(MyPlanDatabase.getDatabase(application));
		allColleges = repository.getAllColleges();
	}

	@Override
	protected void onCleared()
	{
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
			for (CollegeViewModel allViewModel : allViewModels)
				allViewModel.update();
		}
	}
}

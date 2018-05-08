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
import android.widget.EditText;

import org.oregongoestocollege.itsaplan.R;
import org.oregongoestocollege.itsaplan.data.College;
import org.oregongoestocollege.itsaplan.data.MyPlanRepository;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class CollegesViewModel extends AndroidViewModel
{
	private MyPlanRepository repository;
	private LiveData<List<College>> allColleges;
	private List<BindingItem> items;

	public CollegesViewModel(@NonNull Application application)
	{
		super(application);

		repository = new MyPlanRepository(application);
		allColleges = repository.getAllColleges();
	}

	protected void save(@Nullable String name)
	{
		if (!TextUtils.isEmpty(name))
		{
			College college = new College();
			college.setName(name);

			// save it to our database which will trigger a reload
			repository.save(college);
		}
	}

	public LiveData<List<College>> getAllColleges()
	{
		return allColleges;
	}

	public List<BindingItem> getItems(List<College> colleges)
	{
		if (colleges != null && !colleges.isEmpty())
		{
			int size = colleges.size();
			List<BindingItem> viewModels = new ArrayList<>(size);

			for (int i = 0; i < size; i++)
			{
				viewModels.add(new CollegeViewModel(repository, colleges.get(i), size > 1));
			}

			items = viewModels;
		}
		else
			items = Collections.emptyList();

		return items;
	}

	public void onAddCollege(View view)
	{
		final Context context = view.getContext();
		final EditText editText = new EditText(context);
		editText.setHint(R.string.college_name_hint);

		new AlertDialog.Builder(context)
			.setTitle(context.getString(R.string.add_college))
			.setMessage(context.getString(R.string.add_college_message))
			.setView(editText)
			.setPositiveButton(context.getString(R.string.add), new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int whichButton)
				{
					save(editText.getText().toString());
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

package org.oregongoestocollege.itsaplan;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.oregongoestocollege.itsaplan.viewmodel.MyPlanNavViewModel;

/**
 * Oregon GEAR UP App
 * Copyright © 2020 Oregon GEAR UP. All rights reserved.
 */
public class MyPlanOptionsFragment extends Fragment implements View.OnClickListener
{
	private MyPlanNavViewModel viewModel;
	TextView textViewColleges;
	TextView textViewScholarships;
	TextView textViewTests;
	TextView textViewResidency;
	TextView textViewCalendar;

	public MyPlanOptionsFragment()
	{
		// Required empty public constructor
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		viewModel = ViewModelProviders.of(getActivity()).get(MyPlanNavViewModel.class);
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
		@Nullable Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_my_plan_options, container, false);

		textViewColleges = view.findViewById(R.id.topic_colleges);
		textViewScholarships = view.findViewById(R.id.topic_scholarships);
		textViewTests = view.findViewById(R.id.topic_tests);
		textViewResidency = view.findViewById(R.id.topic_residency);
		textViewCalendar = view.findViewById(R.id.topic_calendar);

		textViewColleges.setOnClickListener(this);
		textViewScholarships.setOnClickListener(this);
		textViewTests.setOnClickListener(this);
		textViewResidency.setOnClickListener(this);
		textViewCalendar.setOnClickListener(this);

		return view;
	}

	@Override
	public void onClick(View view)
	{
		String task = null;

		if (view == textViewColleges)
			task = MyPlanNavViewModel.MY_PLAN_COLLEGES;
		else if (view == textViewScholarships)
			task = MyPlanNavViewModel.MY_PLAN_SCHOLARSHIPS;
		else if (view == textViewTests)
			task = MyPlanNavViewModel.MY_PLAN_TESTS;
		else if (view == textViewResidency)
			task = MyPlanNavViewModel.MY_PLAN_RESIDENCY;
		else if (view == textViewCalendar)
			task = MyPlanNavViewModel.MY_PLAN_CALENDAR;

		if (!TextUtils.isEmpty(task))
			viewModel.setCurrentTask(task);
	}
}

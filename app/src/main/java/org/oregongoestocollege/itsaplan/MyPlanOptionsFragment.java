package org.oregongoestocollege.itsaplan;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class MyPlanOptionsFragment extends Fragment implements View.OnClickListener
{
	private final SingleLiveEvent<String> clickOptionEvent = new SingleLiveEvent<>();
	// bind to views using Butterknife, need to unbind when used in Fragment
	private Unbinder unbinder;
	@BindView(R.id.topic_colleges)
	TextView textViewColleges;
	@BindView(R.id.topic_scholarships)
	TextView textViewScholarships;
	@BindView(R.id.topic_actsat)
	TextView textViewActSat;
	@BindView(R.id.topic_residency)
	TextView textViewResidency;
	@BindView(R.id.topic_calendar)
	TextView textViewCalendar;

	public MyPlanOptionsFragment()
	{
		// Required empty public constructor
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
		@Nullable Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_my_plan_options, container, false);
		unbinder = ButterKnife.bind(this, view);

		textViewColleges.setOnClickListener(this);
		textViewScholarships.setOnClickListener(this);
		textViewActSat.setOnClickListener(this);
		textViewResidency.setOnClickListener(this);
		textViewCalendar.setOnClickListener(this);

		return view;
	}

	@Override
	public void onDestroyView()
	{
		super.onDestroyView();
		unbinder.unbind();
	}

	@Override
	public void onClick(View view)
	{
		String optionName = null;

		if (view == textViewColleges)
			optionName = MyPlanFragment.MY_PLAN_COLLEGES;
		else if (view == textViewScholarships)
			optionName = MyPlanFragment.MY_PLAN_SCHOLARSHIPS;
		else if (view == textViewActSat)
			optionName = MyPlanFragment.MY_PLAN_ACTSAT;
		else if (view == textViewResidency)
			optionName = MyPlanFragment.MY_PLAN_RESIDENCY;
		else if (view == textViewCalendar)
			optionName = MyPlanFragment.MY_PLAN_CALENDAR;

		if (!TextUtils.isEmpty(optionName))
			clickOptionEvent.setValue(optionName);
	}

	public SingleLiveEvent<String> getClickOptionEvent()
	{
		return clickOptionEvent;
	}
}

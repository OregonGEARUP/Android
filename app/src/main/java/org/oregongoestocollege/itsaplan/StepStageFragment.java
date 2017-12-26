package org.oregongoestocollege.itsaplan;

import java.lang.ref.WeakReference;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.oregongoestocollege.itsaplan.data.Stage;

/**
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class StepStageFragment extends Fragment
{
	private WeakReference<OnChecklistInteraction> listener;
	private OnFragmentInteractionListener mListener;
	private Stage stage;

	public StepStageFragment()
	{
		// Required empty public constructor
	}

	public void init(OnChecklistInteraction listener, Stage stage)
	{
		this.listener = new WeakReference<>(listener);
		this.stage = stage;
	}

	/**
	 * Use this factory method to create a new instance of this fragment.
	 *
	 * @return A new instance of fragment StepStageFragment.
	 */
	public static StepStageFragment newInstance()
	{
		return new StepStageFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_step_stage, container, false);

		TextView textView = v.findViewById(R.id.text);
		textView.setText(stage.title);

		return v;
	}
}

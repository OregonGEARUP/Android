package org.oregongoestocollege.itsaplan.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import org.oregongoestocollege.itsaplan.SingleLiveEvent;
import org.oregongoestocollege.itsaplan.Utils;
import org.oregongoestocollege.itsaplan.data.ChecklistState;

import static com.google.common.base.Verify.verifyNotNull;

/**
 * Use for navigation between checklist tasks such as block, stage, etc. When creating this
 * view model it should always be scoped to the activity so that the current task stays in sync.
 * Eg: {@link androidx.lifecycle.ViewModelProviders#of(FragmentActivity)
 * <p>
 *  This class works slightly differently than {@link MyPlanNavViewModel}. For the checklist we pop
 *  the backstack without a nav state change so we can get out of sync with the old / new state.
 *  Therefore we all <em>clearing</em> the state and the observer should ignore events with null.
 * </p>
 * Oregon GEAR UP App
 * Copyright © 2020 Oregon GEAR UP. All rights reserved.
 */
public class ChecklistNavViewModel extends ViewModel
{
	private static final String LOG_TAG = "GearUp_ChecklistNavViewModel";
	private static int count = 0;
	// the current Checklist state
	private final SingleLiveEvent<ChecklistState> currentState = new SingleLiveEvent<>();

	public ChecklistNavViewModel()
	{
		if (Utils.DEBUG)
			Utils.d(LOG_TAG, "create %d", ++count);
	}

	public LiveData<ChecklistState> getCurrentState()
	{
		return currentState;
	}

	public void setCurrentState(@NonNull ChecklistState newState)
	{
		verifyNotNull(newState);

		ChecklistState oldState = currentState.getValue();

		if (oldState == null)
		{
			if (Utils.DEBUG)
				Utils.d(LOG_TAG, "setCurrentState dirty=true");

			currentState.setValue(newState);
		}
		else if (!oldState.equals(newState))
		{
			if (Utils.DEBUG)
				Utils.d(LOG_TAG, "setCurrentState dirty=true");

			currentState.setValue(newState);
		}
		else
		{
			if (Utils.DEBUG)
				Utils.d(LOG_TAG, "setCurrentState dirty=false");
		}
	}

	public void clear()
	{
		if (currentState.getValue() != null)
			currentState.setValue(null);
	}
}

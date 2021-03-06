package org.oregongoestocollege.itsaplan;

import androidx.annotation.Nullable;

/**
 * OnFragmentInteractionListener
 * Oregon GEAR UP App
 * <p>
 * This interface is implemented by {@link MainActivity} in case we need to communicate
 * to the activity and potentially other fragments contained in that activity.
 * <p>
 * See the Android Training lesson <a href="http://developer.android.com/training/basics/fragments/communicating.html">
 * Communicating with Other Fragments</a> for more information.
 * <p>
 * Copyright © 2020 Oregon GEAR UP. All rights reserved.
 */
public interface OnFragmentInteractionListener
{
	default void onNavigate(int index, @Nullable String option) { }

	default void handleTabChanged(boolean hidden) { }

	default boolean handleBackPressed()
	{
		return false;
	}

	default boolean canHandleBackPressed()
	{
		return false;
	}
}

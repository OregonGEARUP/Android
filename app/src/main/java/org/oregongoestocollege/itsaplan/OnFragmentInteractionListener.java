package org.oregongoestocollege.itsaplan;

import android.support.annotation.Nullable;

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
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public interface OnFragmentInteractionListener
{
	default void onShowBlock(int blockIndex, String blockFileName) { }

	default void onShowStage(int blockIndex, int stageIndex) { }

	default void onNavigate(int index, @Nullable String option) { }

	boolean handleBackPressed();

	boolean canHandleBackPressed();
}

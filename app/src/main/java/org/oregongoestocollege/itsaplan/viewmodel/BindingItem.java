package org.oregongoestocollege.itsaplan.viewmodel;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * BaseViewModel
 * Oregon GEAR UP App
 *
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public interface BindingItem
{
	int getLayoutId();

	/**
	 * Initialize the ViewModel onBind
	 * @param context context of the view contained in the ViewHolder
	 */
	void onBind(@NonNull Context context);
}

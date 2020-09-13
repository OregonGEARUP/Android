package org.oregongoestocollege.itsaplan.support;

import android.content.Context;
import androidx.annotation.NonNull;

/**
 * BaseViewModel
 * Oregon GEAR UP App
 *
 * Copyright © 2020 Oregon GEAR UP. All rights reserved.
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

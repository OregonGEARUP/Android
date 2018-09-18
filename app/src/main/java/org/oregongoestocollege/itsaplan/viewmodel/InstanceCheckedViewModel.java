package org.oregongoestocollege.itsaplan.viewmodel;

import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;

import org.oregongoestocollege.itsaplan.data.Instance;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class InstanceCheckedViewModel
{
	private Instance instance;
	private boolean originalChecked;
	/**
	 * Key used to store user entered data
	 */
	public final String key;
	/**
	 * Used in checkpoints to store user entered text data.
	 */
	public final ObservableBoolean isChecked = new ObservableBoolean();

	public InstanceCheckedViewModel(@NonNull Instance instance, @NonNull String key, boolean originalChecked)
	{
		this.instance = instance;
		this.key = key;
		this.originalChecked = originalChecked;

		isChecked.set(originalChecked);
	}

	/**
	 * @return true if the stored data is different than the user entered data
	 */
	public boolean isDirty()
	{
		return originalChecked != isChecked.get();
	}

	/**
	 * Reset the data so the stored data is the same as the user entered data
	 */
	public void saved()
	{
		originalChecked = isChecked.get();
	}

	public String getPlaceholder()
	{
		return instance.getPlaceholder();
	}

//	public void onInstanceChecked(boolean checked)
//	{
//		isChecked = checked;
//	}
}

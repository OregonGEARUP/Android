package org.oregongoestocollege.itsaplan.viewmodel;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.oregongoestocollege.itsaplan.data.Instance;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class InstanceFieldViewModel
{
	private Instance instance;
	private String originalData;
	/**
	 * Key used to store user entered data
	 */
	public final String key;
	/**
	 * Used in checkpoints to store user entered text data.
	 */
	public final ObservableField<String> text = new ObservableField<>();

	public InstanceFieldViewModel(@NonNull Instance instance, @NonNull String key, @Nullable String originalData)
	{
		this.instance = instance;
		this.key = key;
		this.originalData = originalData;

		text.set(originalData);
	}

	public boolean isDirty()
	{
		// the binding code seems to set to "" versus null, check for it
		String currentData = text.get();
		if (TextUtils.isEmpty(currentData))
			currentData = null;

		return !TextUtils.equals(currentData, originalData);
	}

	public boolean hasText()
	{
		return !TextUtils.isEmpty(text.get());
	}

	public void saved()
	{
		originalData = text.get();
	}

	public String getPlaceholder()
	{
		return instance.getPlaceholder();
	}
}

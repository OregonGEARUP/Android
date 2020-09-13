package org.oregongoestocollege.itsaplan.viewmodel;

import androidx.databinding.ObservableField;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;

import org.oregongoestocollege.itsaplan.data.Instance;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class InstanceFieldViewModel
{
	private Instance instance;
	private String originalText;
	/**
	 * Key used to store user entered data
	 */
	public final String key;
	/**
	 * Used in checkpoints to store user entered text data.
	 */
	public final ObservableField<String> text = new ObservableField<>();

	public InstanceFieldViewModel(@NonNull Instance instance, @NonNull String key, @Nullable String originalText)
	{
		this.instance = instance;
		this.key = key;
		this.originalText = originalText;

		text.set(originalText);
	}

	/**
	 * @return true if the stored data is different than the user entered data
	 */
	public boolean isDirty()
	{
		// the binding code seems to set to "" versus null, check for it
		String currentData = text.get();
		if (TextUtils.isEmpty(currentData))
			currentData = null;

		return !TextUtils.equals(currentData, originalText);
	}

	/**
	 * Reset the data so the stored data is the same as the user entered data
	 */
	public void saved()
	{
		originalText = text.get();
	}

	public boolean hasText()
	{
		return !TextUtils.isEmpty(text.get());
	}

	public String getPlaceholder()
	{
		return instance.getPlaceholder();
	}
}

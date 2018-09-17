package org.oregongoestocollege.itsaplan.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.TextUtils;
import android.text.method.TransformationMethod;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class SecureInfoViewModel
{
	public final ObservableField<String> text = new ObservableField<>();
	public final ObservableField<TransformationMethod> transformation = new ObservableField<>();
	public final ObservableBoolean isEnabled = new ObservableBoolean();
	public final String key;
	private String originalData;

	public SecureInfoViewModel(String key, String originalData)
	{
		this.key = key;
		this.originalData = originalData;
		this.text.set(originalData);
	}

	public boolean isDirty()
	{
		// the binding code seems to set to "" versus null, check for it
		String currentData = text.get();
		if (TextUtils.isEmpty(currentData))
			currentData = null;

		return !TextUtils.equals(currentData, originalData);
	}

	public void saved()
	{
		originalData = text.get();
	}
}

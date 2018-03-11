package org.oregongoestocollege.itsaplan.data;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

/**
 * Instance
 * Oregon GEAR UP App
 *
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class Instance
{
	// data returned by network request
	private String id;
	private String prompt;
	private String placeholder;
	// stored data
	public long dateValue;
	// fields to support UX display
	public final ObservableBoolean isChecked = new ObservableBoolean();
	public final ObservableField<String> textEntry = new ObservableField<>();

	public String getId()
	{
		return id;
	}

	public String getPrompt()
	{
		return prompt;
	}

	public String getPlaceholder()
	{
		return placeholder;
	}
}

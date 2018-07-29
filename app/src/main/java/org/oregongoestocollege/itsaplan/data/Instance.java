package org.oregongoestocollege.itsaplan.data;

import android.databinding.ObservableField;

/**
 * Instance
 * Oregon GEAR UP App
 *
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class Instance
{
	private String id;
	private String prompt;
	private String placeholder;
	// fields to support UX display
	public boolean isChecked;
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

	public void onInstanceChecked(boolean checked)
	{
		isChecked = checked;
	}
}

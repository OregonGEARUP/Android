package org.oregongoestocollege.itsaplan;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputEditText;
import android.widget.FrameLayout;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class AddLayoutView extends FrameLayout
{
	private TextInputEditText editText;

	public AddLayoutView(Context context)
	{
		super(context);
		inflate(context, R.layout.add_layout, this);
	}

	public AddLayoutView(Context context, @StringRes int hintMsg)
	{
		super(context);
		inflate(context, R.layout.add_layout, this);
		editText = findViewById(R.id.add_edit_text);
		editText.setHint(hintMsg);
	}

	public String getAddedText()
	{
		return editText.getText().toString();
	}
}

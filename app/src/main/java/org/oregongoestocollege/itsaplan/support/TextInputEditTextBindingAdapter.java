package org.oregongoestocollege.itsaplan.support;

import android.databinding.BindingAdapter;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.text.method.TransformationMethod;

/**
 * TextInputEditTextBindingAdapter
 *
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class TextInputEditTextBindingAdapter
{
	/**
	 * Binding Adapter that is called when attribute 'transformation' is used on a {@link TextInputEditText}.
	 *
	 * @param view instance of view for which transformation is applied
	 * @param transformationMethod the transformation to apply or null to remove transformations
	 */
	@BindingAdapter("transformation")
	public static void transformation(TextInputEditText view, @Nullable TransformationMethod transformationMethod)
	{
		if (view == null)
			return;

		view.setTransformationMethod(transformationMethod);
	}
}

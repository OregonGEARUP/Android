package org.oregongoestocollege.itsaplan.support;

import java.util.List;

import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.oregongoestocollege.itsaplan.BR;

/**
 * ViewGroupBindingAdapter
 *
 * Oregon GEAR UP App
 * Copyright © 2020 Oregon GEAR UP. All rights reserved.
 */
public class ViewGroupBindingAdapter
{
	/**
	 * Binding adapter called when attribute 'app:contents' and 'app:layout' is used in a view group
	 * Create and add list of layout to view group and sets each layout data bound content value
	 *
	 * @param viewGroup to add list of layout as mentioned in {@param layoutId}
	 * @param contents List of {@link BindingItem} to add to
	 */
	@BindingAdapter(value = {"contents"}, requireAll = false)
	public static <T extends BindingItem> void setContents(ViewGroup viewGroup, final List<T> contents)
	{
		viewGroup.removeAllViews();
		if (contents != null)
		{
			final LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
			for (BindingItem content : contents)
			{
				final ViewDataBinding
					viewBinding = DataBindingUtil.inflate(inflater, content.getLayoutId(), viewGroup, true);
				viewBinding.setVariable(BR.viewModel, content);
			}
		}
	}
}

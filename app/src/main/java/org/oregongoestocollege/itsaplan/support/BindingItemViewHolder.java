package org.oregongoestocollege.itsaplan.support;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

import org.oregongoestocollege.itsaplan.BR;
import org.oregongoestocollege.itsaplan.viewmodel.BindingItem;

/**
 * BindingItemViewHolder
 * Oregon GEAR UP App
 *
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class BindingItemViewHolder extends RecyclerView.ViewHolder
{
	private final ViewDataBinding viewDataBinding;

	public BindingItemViewHolder(ViewDataBinding viewDataBinding)
	{
		super(viewDataBinding.getRoot());
		this.viewDataBinding = viewDataBinding;
	}

	public void onBindView(int position, BindingItem item)
	{
		viewDataBinding.setVariable(BR.viewModel, item);
		viewDataBinding.executePendingBindings();
	}
}

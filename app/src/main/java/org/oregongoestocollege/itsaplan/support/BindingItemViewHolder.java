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
class BindingItemViewHolder extends RecyclerView.ViewHolder
{
	private final ViewDataBinding viewDataBinding;

	BindingItemViewHolder(ViewDataBinding viewDataBinding)
	{
		super(viewDataBinding.getRoot());
		this.viewDataBinding = viewDataBinding;
	}

	void onBindView(int position, BindingItem item)
	{
		// give the item a chance to do any initialization before it's displayed
		item.onBind(viewDataBinding.getRoot().getContext());

		viewDataBinding.setVariable(BR.viewModel, item);
		viewDataBinding.executePendingBindings();
	}
}

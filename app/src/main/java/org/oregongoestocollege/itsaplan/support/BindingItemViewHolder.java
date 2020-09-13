package org.oregongoestocollege.itsaplan.support;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import org.oregongoestocollege.itsaplan.BR;

/**
 * BindingItemViewHolder
 * Oregon GEAR UP App
 *
 * Copyright © 2020 Oregon GEAR UP. All rights reserved.
 */
class BindingItemViewHolder extends RecyclerView.ViewHolder
{
	private final ViewDataBinding viewDataBinding;

	BindingItemViewHolder(ViewDataBinding viewDataBinding, ItemClickCallback itemClickCallback)
	{
		super(viewDataBinding.getRoot());
		this.viewDataBinding = viewDataBinding;
		this.viewDataBinding.setVariable(BR.uxItemClickCallback, itemClickCallback);
	}

	void onBindView(int position, BindingItem item)
	{
		// give the item a chance to do any initialization before it's displayed
		item.onBind(viewDataBinding.getRoot().getContext());

		viewDataBinding.setVariable(BR.viewModel, item);
		viewDataBinding.executePendingBindings();
	}
}

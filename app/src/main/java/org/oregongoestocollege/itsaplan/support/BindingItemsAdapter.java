package org.oregongoestocollege.itsaplan.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * BindingItemsAdapter
 * Oregon GEAR UP App
 *
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class BindingItemsAdapter extends RecyclerView.Adapter<BindingItemViewHolder>
{
	private final List<BindingItem> items;
	@Nullable
	private final ItemClickCallback itemClickCallback;

	/**
	 * Constructor for Adapter.
	 */
	public BindingItemsAdapter(ItemClickCallback itemClickCallback)
	{
		this.items = new ArrayList<>();
		this.itemClickCallback = itemClickCallback;
	}

	/**
	 * Removes all items in the list and notify the observers
	 */
	public void clear()
	{
		clear(false);
	}

	/**
	 * Appends all items from the provided list
	 *
	 * @param items list of items to be appended
	 * @throws NullPointerException      if the specified element is null and
	 *                                   this list does not permit null elements
	 * @throws IllegalArgumentException  if list is null or some property of the specified
	 *                                   element prevents it from being added to this list
	 * @throws IndexOutOfBoundsException if the index is out of range
	 *                                   (<tt>index &lt; 0 || index &gt; size()</tt>)
	 */
	public void addAll(@NonNull List<BindingItem> items)
	{
		addAll(items, false);
	}

	/**
	 * Add all items from the provided list at a given index
	 *
	 * @param index at which to insert the list
	 * @param items list of items to insert
	 * @throws NullPointerException      if the specified element is null and
	 *                                   this list does not permit null elements
	 * @throws IllegalArgumentException  if list is null or some property of the specified
	 *                                   element prevents it from being added to this list
	 * @throws IndexOutOfBoundsException if the index is out of range
	 *                                   (<tt>index &lt; 0 || index &gt; size()</tt>)
	 */
	public void addAll(int index, @NonNull List<BindingItem> items)
	{
		this.items.addAll(index, items);
		notifyItemRangeInserted(index, items.size());
	}

	/**
	 * append item to the adapter list
	 *
	 * @param item to add
	 */
	public void add(@NonNull BindingItem item)
	{
		int index = items.size();
		items.add(item);
		notifyItemInserted(index);
	}

	/**
	 * Add item at the specified position
	 *
	 * @param index position at which to insert the item
	 * @param item to be inserted
	 * @throws NullPointerException      if the specified element is null and
	 *                                   this list does not permit null elements
	 * @throws IllegalArgumentException  if list is null or some property of the specified
	 *                                   element prevents it from being added to this list
	 * @throws IndexOutOfBoundsException if the index is out of range
	 *                                   (<tt>index &lt; 0 || index &gt; size()</tt>)
	 */
	public void add(int index, @NonNull BindingItem item)
	{
		items.add(index, item);
		notifyItemInserted(index);
	}

	/**
	 * Remove specified item
	 *
	 * @param item to be removed
	 * @throws IllegalArgumentException if list is null or some property of the specified
	 *                                  element prevents it from being added to this list
	 */
	public boolean remove(@NonNull BindingItem item)
	{
		int index = items.indexOf(item);
		if (index >= 0)
		{
			items.remove(index);
			notifyItemRemoved(index);
			return true;
		}
		return false;
	}

	/**
	 * Remove item at position {@code index}
	 *
	 * @param index position at which to remove the item
	 * @return The object that was removed or null
	 */
	public BindingItem remove(int index)
	{
		BindingItem removedItem = items.remove(index);
		notifyItemRemoved(index);
		return removedItem;
	}

	/**
	 * Swap items from their respective positions
	 *
	 * @param fromPosition current position of item
	 * @param toPosition new position of the item
	 * @return true if the item was moved, false otherwise
	 */
	public boolean moveItem(int fromPosition, int toPosition)
	{
		if (fromPosition < toPosition)
		{
			for (int i = fromPosition; i < toPosition; i++)
				Collections.swap(items, i, i + 1);
		}
		else
		{
			for (int i = fromPosition; i > toPosition; i--)
				Collections.swap(items, i, i - 1);
		}
		notifyItemMoved(fromPosition, toPosition);
		return true;
	}

	/**
	 * return the item at given position
	 *
	 * @param position of item to be retrived
	 * @return item at the position
	 * @throws IndexOutOfBoundsException if the index is out of range
	 *                                   (<tt>index &lt; 0 || index &gt; size()</tt>)
	 */
	public BindingItem getItem(int position)
	{
		return items.get(position);
	}

	/**
	 * Retrieve the item position
	 *
	 * @param item current item
	 * @return get the position of item
	 */
	public int getItemPosition(@NonNull BindingItem item)
	{
		return items.indexOf(item);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getItemViewType(int position)
	{
		//get the layout from the ViewModel. This is to support multi layouts
		return getItem(position).getLayoutId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getItemCount()
	{
		return items.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BindingItemViewHolder onCreateViewHolder(ViewGroup parent, int layoutId)
	{
		LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

		// Get ViewDataBinding from the layout file and use it to bind the view model
		ViewDataBinding viewBinding = DataBindingUtil.inflate(layoutInflater, layoutId, parent, false);

		return new BindingItemViewHolder(viewBinding, itemClickCallback);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onBindViewHolder(BindingItemViewHolder holder, int position)
	{
		if (holder != null)
			holder.onBindView(position, getItem(position));
	}

	protected void addAll(@NonNull List<BindingItem> items, boolean notifyItemAdded)
	{
		int index = this.items.size();
		this.items.addAll(items);
		if (notifyItemAdded)
			notifyItemRangeChanged(index, items.size());
		else
			notifyDataSetChanged();
	}

	protected void clear(boolean notifyItemRemoved)
	{
		int size = items.size();
		items.clear();
		if (notifyItemRemoved)
			notifyItemRangeRemoved(0, size);
		else
			notifyDataSetChanged();
	}
}

package org.oregongoestocollege.itsaplan.support;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * NoSwipePager
 * Oregon GEAR UP App
 *
 * Copyright © 2020 Oregon GEAR UP. All rights reserved.
 */
public class NoSwipePager extends ViewPager
{
	private boolean enabled;

	public NoSwipePager(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		this.enabled = true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		return enabled && super.onTouchEvent(event);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event)
	{
		return enabled && super.onInterceptTouchEvent(event);
	}

	@Override
	public boolean canScrollHorizontally(int direction)
	{
		return enabled && super.canScrollHorizontally(direction);
	}

	public void setPagingEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	/**
	 * This override is required to support left/right arrow buttons
	 * on tablets with fuller keyboards.
	 */
	@Override
	public boolean executeKeyEvent(KeyEvent event)
	{
		return (enabled && super.executeKeyEvent(event));
	}
}
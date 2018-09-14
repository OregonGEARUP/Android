package org.oregongoestocollege.itsaplan;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class BaseFragment extends Fragment
{
	protected void setHomeAsUpEnabled(boolean enabled)
	{
		FragmentActivity activity = getActivity();
		if (activity instanceof AppCompatActivity)
		{
			ActionBar actionBar = ((AppCompatActivity)activity).getSupportActionBar();
			if (actionBar != null)
				actionBar.setDisplayHomeAsUpEnabled(enabled);
		}
	}
}

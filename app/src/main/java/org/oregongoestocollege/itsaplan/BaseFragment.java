package org.oregongoestocollege.itsaplan;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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

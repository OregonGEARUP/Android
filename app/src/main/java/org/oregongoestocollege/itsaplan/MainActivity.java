package org.oregongoestocollege.itsaplan;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import org.oregongoestocollege.itsaplan.support.BottomBarAdapter;
import org.oregongoestocollege.itsaplan.support.NoSwipePager;

/**
 * MainActivity
 * Oregon GEAR UP App
 * <p>
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener
{
	public static final String LOG_TAG = "GearUpMainActivity";
	private int lastSelectedPosition = 0;
	private NoSwipePager viewPager;
	private BottomBarAdapter pagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		BottomNavigationView navigation = findViewById(R.id.navigation);
		navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
		{
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem item)
			{
				int position;
				String title;
				switch (item.getItemId())
				{
				case R.id.navigation_info:
					position = 3;
					title = getResources().getString(R.string.title_info);
					break;
				case R.id.navigation_passwords:
					position = 2;
					title = getResources().getString(R.string.title_passwords);
					break;
				case R.id.navigation_myplan:
					position = 1;
					title = getResources().getString(R.string.title_myplan);
					break;
				case R.id.navigation_checklist:
				default:
					position = 0;
					title = getResources().getString(R.string.app_name);
					break;
				}
				viewPager.setCurrentItem(position);
				setTitle(title);
				return true;
			}
		});

		pagerAdapter = new BottomBarAdapter(getSupportFragmentManager());

		// we want to disable swipe, on change fragments via bottom bar
		viewPager = findViewById(R.id.viewpager);
		viewPager.setPagingEnabled(false);
		viewPager.setAdapter(pagerAdapter);
		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
		{
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
			{
				// no-op
			}

			@Override
			public void onPageSelected(int position)
			{
				if (Utils.DEBUG)
					Utils.d(LOG_TAG, "onPageSelected last:%d current:%d", lastSelectedPosition, position);

				lastSelectedPosition = position;

				Fragment fragment = pagerAdapter.getRegisteredFragment(lastSelectedPosition);
				boolean showBack =
					(fragment instanceof OnFragmentInteractionListener) && ((OnFragmentInteractionListener)fragment).canHandleBackPressed();
				getSupportActionBar().setDisplayHomeAsUpEnabled(showBack);
			}

			@Override
			public void onPageScrollStateChanged(int state)
			{
				// no-op
			}
		});
	}

	@Override
	public void onShowBlock(int blockIndex, String blockFileName)
	{
		if (lastSelectedPosition != -1)
		{
			Fragment fragment = pagerAdapter.getRegisteredFragment(lastSelectedPosition);
			if (fragment instanceof OnFragmentInteractionListener)
				((OnFragmentInteractionListener)fragment).onShowBlock(blockIndex, blockFileName);
		}
	}

	@Override
	public void onShowStage(int blockIndex, int stageIndex)
	{
		if (lastSelectedPosition != -1)
		{
			Fragment fragment = pagerAdapter.getRegisteredFragment(lastSelectedPosition);
			if (fragment instanceof OnFragmentInteractionListener)
				((OnFragmentInteractionListener)fragment).onShowStage(blockIndex, stageIndex);
		}
	}

	@Override
	public boolean handleBackPressed()
	{
		// no-op
		return false;
	}

	@Override
	public boolean canHandleBackPressed()
	{
		// no-op
		return false;
	}

	@Override
	public void onBackPressed()
	{
		boolean handled = false;

		if (lastSelectedPosition != -1)
		{
			Fragment fragment = pagerAdapter.getRegisteredFragment(lastSelectedPosition);
			if (fragment instanceof OnFragmentInteractionListener)
				handled = ((OnFragmentInteractionListener)fragment).handleBackPressed();
		}

		if (!handled)
			super.onBackPressed();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		boolean handled = false;

		if (item.getItemId() == android.R.id.home && lastSelectedPosition != -1)
		{
			Fragment fragment = pagerAdapter.getRegisteredFragment(lastSelectedPosition);
			if (fragment instanceof OnFragmentInteractionListener)
				handled = ((OnFragmentInteractionListener)fragment).handleBackPressed();
		}

		return handled;
	}
}

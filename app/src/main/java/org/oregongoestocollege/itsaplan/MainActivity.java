package org.oregongoestocollege.itsaplan;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;

import org.oregongoestocollege.itsaplan.data.MyPlanRepository;
import org.oregongoestocollege.itsaplan.support.BottomBarAdapter;
import org.oregongoestocollege.itsaplan.support.NoSwipePager;
import org.oregongoestocollege.itsaplan.viewmodel.MyPlanNavViewModel;

/**
 * MainActivity
 * Oregon GEAR UP App
 * <p>
 * Copyright © 2020 Oregon GEAR UP. All rights reserved.
 */
public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener,
	ViewPager.OnPageChangeListener
{
	public static final String LOG_TAG = "GearUp_MainActivity";
	private int lastSelectedPosition = 0;
	private NoSwipePager viewPager;
	private BottomBarAdapter pagerAdapter;
	private MyPlanNavViewModel viewModel;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		BottomNavigationView navigation = findViewById(R.id.navigation);
		navigation.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);

		pagerAdapter = new BottomBarAdapter(getSupportFragmentManager());

		// we want to disable swipe, on change fragments via bottom bar
		viewPager = findViewById(R.id.viewpager);
		viewPager.setPagingEnabled(false);
		viewPager.setAdapter(pagerAdapter);
		viewPager.addOnPageChangeListener(this);

		viewModel = ViewModelProviders.of(this).get(MyPlanNavViewModel.class);
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		// pre-load calendar event data so it's available to schedule notifications
		MyPlanRepository.getInstance(this).preFetchCalenderEvents(this);
	}

	boolean onNavigationItemSelected(@NonNull MenuItem item)
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

		// only set if it's not the current position, 2nd tap resets title
		if (viewPager.getCurrentItem() != position)
		{
			if (Utils.DEBUG)
				Utils.d(LOG_TAG, "onNavigationItemSelected setTitle=%s", title);
			setTitle(title);
			viewPager.setCurrentItem(position);
		}
		return true;
	}

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

		// commit any changes from the previous tab since we share data across tabs
		if (lastSelectedPosition != -1)
		{
			Fragment fragment = pagerAdapter.getRegisteredFragment(lastSelectedPosition);
			if (fragment instanceof OnFragmentInteractionListener)
				((OnFragmentInteractionListener)fragment).handleTabChanged(true);
		}

		lastSelectedPosition = position;

		// setup for new fragment
		Fragment fragment = pagerAdapter.getRegisteredFragment(lastSelectedPosition);
		if (fragment != null)
		{
			OnFragmentInteractionListener listener = null;
			if (fragment instanceof OnFragmentInteractionListener)
				listener = (OnFragmentInteractionListener)fragment;

			if (listener != null)
			{
				listener.handleTabChanged(false);

				boolean showBack = listener.canHandleBackPressed();
				ActionBar actionBar = getSupportActionBar();
				if (actionBar != null)
					actionBar.setDisplayHomeAsUpEnabled(showBack);
			}

			// see if we need to set our title from a child fragment
			Utils.updateTitleOnBackStackChanged(fragment, LOG_TAG);
		}
	}

	@Override
	public void onPageScrollStateChanged(int state)
	{
		// no-op
	}

	@Override
	public void onNavigate(int index, @Nullable String task)
	{
		if (!getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED))
			return;

		int id;
		switch (index)
		{
		case 3:
			id = R.id.navigation_info;
			break;
		case 2:
			id = R.id.navigation_passwords;
			break;
		case 1:
			id = R.id.navigation_myplan;
			viewModel.setCurrentTask(task);
			break;
		default:
			id = R.id.navigation_checklist;
			break;
		}
		BottomNavigationView navigation = findViewById(R.id.navigation);
		navigation.setSelectedItemId(id);
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

/**
 * TODO
 * - scroll / focus issue, see @+id/focus_view
 * - network error messages
 * - scope view model to fragment, activity?
 * - BindingItem default implementation of getLayout
 * - Text input  https://issuetracker.google.com/issues/62834931
 */

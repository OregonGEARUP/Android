package org.oregongoestocollege.itsaplan;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import org.oregongoestocollege.itsaplan.common.BottomBarAdapter;
import org.oregongoestocollege.itsaplan.common.NoSwipePager;

/**
 * MainActivity
 * Oregon GEAR UP App
 * <p>
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener
{
    private NoSwipePager viewPager;
    private BottomBarAdapter pagerAdapter;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener()
    {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item)
        {
            int position;
            switch (item.getItemId())
            {
                case R.id.navigation_info:
                    position = 3;
                    break;
                case R.id.navigation_passwords:
                    position = 2;
                    break;
                case R.id.navigation_myplan:
                    position = 1;
                    break;
                case R.id.navigation_checklist:
                default:
                    position = 0;
                    break;
            }
            viewPager.setCurrentItem(position);
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        pagerAdapter = new BottomBarAdapter(getSupportFragmentManager());
        pagerAdapter.addFragments(new ChecklistFragment());
        pagerAdapter.addFragments(new MyPlanFragment());
        pagerAdapter.addFragments(new PasswordsFragment());
        pagerAdapter.addFragments(new InfoFragment());

        // we want to disable swipe, on change fragments via bottom bar
        viewPager = findViewById(R.id.viewpager);
        viewPager.setPagingEnabled(false);
        viewPager.setAdapter(pagerAdapter);
    }

    @Override public void onFragmentInteraction()
    {
        // no-op for now
    }
}

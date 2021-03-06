package org.oregongoestocollege.itsaplan;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

/**
 * SplashActivity
 * Oregon GEAR UP App
 * <p>
 * Copyright © 2020 Oregon GEAR UP. All rights reserved.
 */
public class SplashActivity extends AppCompatActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}
}
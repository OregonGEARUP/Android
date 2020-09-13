package org.oregongoestocollege.itsaplan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;

import static com.google.common.base.Verify.verifyNotNull;

public class WebViewActivity extends AppCompatActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment);

		Fragment newFragment = InfoFragment.newInstance(getIntent().getStringExtra(InfoFragment.EXTRA_URL), true);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.add(R.id.fragment_container, newFragment);
		transaction.commit();

		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null)
			actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		boolean handled = false;

		if (item.getItemId() == android.R.id.home)
		{
			handled = true;
			finish();
		}

		return handled;
	}

	public static void startActivity(@NonNull Context context, @NonNull String url)
	{
		verifyNotNull(context);
		verifyNotNull(url);

		Intent i = new Intent(context, WebViewActivity.class);
		i.putExtra(InfoFragment.EXTRA_URL, url);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
	}
}

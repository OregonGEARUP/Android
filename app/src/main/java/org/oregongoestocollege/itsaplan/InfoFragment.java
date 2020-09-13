package org.oregongoestocollege.itsaplan;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * InfoFragment
 * Oregon GEAR UP App
 *
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class InfoFragment extends Fragment implements OnFragmentInteractionListener
{
	public static final String EXTRA_URL = "extra_url";
	public static final String EXTRA_FLAG_LOAD = "extra_flag_load";
	private static final String LOG_TAG = "GearUp_InfoFragment";
	private final String GEAR_UP_WEBSITE = "https://oregongoestocollege.org/5-things";
	private WebView mWebView;
	private String url;
	private boolean urlLoaded;

	public InfoFragment()
	{
		// Required empty public constructor
	}

	private void loadUrl()
	{
		if (Utils.DEBUG)
			Utils.d(LOG_TAG, "loadUrl() urlLoaded:%s", urlLoaded);

		if (!urlLoaded && mWebView != null)
		{
			mWebView.loadUrl(url);
			urlLoaded = true;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_info, container, false);

		Bundle bundle = getArguments();
		if (bundle != null)
			url = bundle.getString(EXTRA_URL, GEAR_UP_WEBSITE);
		else
			url = GEAR_UP_WEBSITE;

		// some of the external links GearUp uses need JS
		mWebView = v.findViewById(R.id.info_web_view);
		mWebView.getSettings().setJavaScriptEnabled(true);

		// depending on how we use this fragment, we may want to load the URL
		// immediately or wait till the fragment is shown
		if (bundle != null && bundle.getBoolean(EXTRA_FLAG_LOAD, false))
			loadUrl();

		return v;
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu)
	{
		super.onPrepareOptionsMenu(menu);

		Utils.disableMenu(menu);
	}

	@Override
	public void handleTabChanged(boolean hidden)
	{
		// since this fragment can get pre-loaded via the pager wait till
		// we are displayed before loading the URL
		loadUrl();
	}

	/**
	 * Create a new instance of InfoFragment, initialized to show the specified Url.
	 */
	public static InfoFragment newInstance(@NonNull String url, boolean showImmediate)
	{
		InfoFragment f = new InfoFragment();

		// Supply url input as an argument.
		Bundle args = new Bundle();
		args.putString(EXTRA_URL, url);
		args.putBoolean(EXTRA_FLAG_LOAD, showImmediate);
		f.setArguments(args);

		return f;
	}
}

package org.oregongoestocollege.itsaplan;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
	private static final String LOG_TAG = "GearUp_InfoFragment";
	private final String GEAR_UP_WEBSITE = "https://oregongoestocollege.org/5-things";
	private WebView mWebView;
	private String url;
	private boolean urlLoaded;

	public InfoFragment()
	{
		// Required empty public constructor
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
		if (!urlLoaded && mWebView != null)
		{
			Utils.d(LOG_TAG, "handleTabChanged URL loaded");
			mWebView.loadUrl(url);
			urlLoaded = true;
		}
	}

	/**
	 * Create a new instance of InfoFragment, initialized to show the specified Url.
	 */
	public static InfoFragment newInstance(@NonNull String url)
	{
		InfoFragment f = new InfoFragment();

		// Supply url input as an argument.
		Bundle args = new Bundle();
		args.putString(EXTRA_URL, url);
		f.setArguments(args);

		return f;
	}
}

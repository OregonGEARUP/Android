package org.oregongoestocollege.itsaplan;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * InfoFragment
 * Oregon GEAR UP App
 *
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class InfoFragment extends Fragment
{
	public static final String EXTRA_URL = "extra_url";
	private final String GEAR_UP_WEBSITE = "https://oregongoestocollege.org/5-things";
	private WebView mWebView;

	public InfoFragment()
	{
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_info, container, false);

		Bundle bundle = getArguments();
		String url;
		if (bundle != null)
			url = bundle.getString(EXTRA_URL, GEAR_UP_WEBSITE);
		else
			url = GEAR_UP_WEBSITE;

		// TODO wait till fragment is displayed before loading web page
		mWebView = v.findViewById(R.id.info_web_view);
		mWebView.loadUrl(url);

		return v;
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

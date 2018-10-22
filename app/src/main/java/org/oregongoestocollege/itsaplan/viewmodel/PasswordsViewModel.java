package org.oregongoestocollege.itsaplan.viewmodel;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;

import org.oregongoestocollege.itsaplan.Utils;
import org.oregongoestocollege.itsaplan.support.CryptoUtil;
import org.oregongoestocollege.itsaplan.support.GearUpSharedPreferences;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class PasswordsViewModel extends AndroidViewModel
{
	private static final String LOG_TAG = "GearUp_PasswordsViewModel";
	private static final String EDIT_SSN_KEY = "edit_ssn";
	private static final String EDIT_SSN_1_KEY = "edit_ssn1";
	private static final String EDIT_SSN_2_KEY = "edit_ssn2";
	private static final String EDIT_DRIVER_LIC_KEY = "edit_driver_lic";
	private static final String FSA_USERNAME_KEY = "fsa_username";
	private static final String FSA_PASSWORD_KEY = "fsa_password";
	private static final String APPLICATION_EMAIL_KEY = "scholarship_email";
	private static final String APPLICATION_PASSWORD_KEY = "scholarship_password";
	private static final String ORSAA_USERNAME_KEY = "orsaa_username";
	private static final String ORSAA_PASSWORD_KEY = "orsaa_password";
	private static final String CSS_USERNAME_KEY = "css_username";
	private static final String CSS_PASSWORD_KEY = "css_password";
	private static final String EXTRA_LOGIN_1_ORGANIZATION = "extra_1_organization";
	private static final String EXTRA_LOGIN_1_USERNAME = "extra_1_username";
	private static final String EXTRA_LOGIN_1_PASSWORD = "extra_1_password";
	private static final String EXTRA_LOGIN_2_ORGANIZATION = "extra_2_organization";
	private static final String EXTRA_LOGIN_2_USERNAME = "extra_2_username";
	private static final String EXTRA_LOGIN_2_PASSWORD = "extra_2_password";
	private static final String OSAC_USERNAME = "osac_username";
	private static final String OSAC_PASSWORD = "osac_password";
	private SharedPreferences prefs;
	private CryptoUtil cryptoUtil;
	private Map<String, SecureInfoViewModel> secureInfoMap;
	private boolean isLocked;
	private long timestamp;

	public PasswordsViewModel(@NonNull Application application)
	{
		super(application);

		cryptoUtil = new CryptoUtil();
	}

	private void lockItemViewModels(boolean locked)
	{
		// remember our current state
		isLocked = locked;

		TransformationMethod transformationMethod = locked ? new PasswordTransformationMethod() : null;
		boolean enabled = !locked;

		for (Map.Entry<String, SecureInfoViewModel> e : secureInfoMap.entrySet())
		{
			SecureInfoViewModel viewModel = e.getValue();
			viewModel.transformation.set(transformationMethod);
			viewModel.isEnabled.set(enabled);
		}

		if (Utils.DEBUG)
			Utils.d(LOG_TAG, "lockItemViewModels locked:%s", isLocked);
	}

	private void addToMap(String key)
	{
		secureInfoMap.put(key,
			new SecureInfoViewModel(key, cryptoUtil.safeDecrypt(getApplication(), prefs.getString(key, null))));
	}

	public void init()
	{
		// only need to initialize the item view models once
		if (secureInfoMap == null)
		{
			prefs = GearUpSharedPreferences.getSharedPreferences(getApplication());
			secureInfoMap = new HashMap<>(25);

			addToMap(EDIT_SSN_KEY);
			addToMap(EDIT_SSN_1_KEY);
			addToMap(EDIT_SSN_2_KEY);
			addToMap(EDIT_DRIVER_LIC_KEY);
			addToMap(FSA_USERNAME_KEY);
			addToMap(FSA_PASSWORD_KEY);
			addToMap(APPLICATION_EMAIL_KEY);
			addToMap(APPLICATION_PASSWORD_KEY);
			addToMap(ORSAA_USERNAME_KEY);
			addToMap(ORSAA_PASSWORD_KEY);
			addToMap(CSS_USERNAME_KEY);
			addToMap(CSS_PASSWORD_KEY);
			addToMap(EXTRA_LOGIN_1_ORGANIZATION);
			addToMap(EXTRA_LOGIN_1_USERNAME);
			addToMap(EXTRA_LOGIN_1_PASSWORD);
			addToMap(EXTRA_LOGIN_2_ORGANIZATION);
			addToMap(EXTRA_LOGIN_2_USERNAME);
			addToMap(EXTRA_LOGIN_2_PASSWORD);
			addToMap(OSAC_USERNAME);
			addToMap(OSAC_PASSWORD);

			// then make sure our state is in sync
			lockItemViewModels(true);
		}
	}

	public boolean isLocked()
	{
		return isLocked;
	}

	public void lockAll(boolean lock)
	{
		// only lock / unlock if needed
		if (secureInfoMap != null  && lock != isLocked)
			lockItemViewModels(lock);

		if (isLocked())
			timestamp = 0;
	}

	public void onPause()
	{
		timestamp = Calendar.getInstance().getTimeInMillis();
	}

	public void onResume()
	{
		if (!isLocked() && timestamp != 0)
		{
			// if user left the app for more than 30 seconds, lock the data
			long now = Calendar.getInstance().getTimeInMillis();
			if (now - timestamp > 30000)
				lockAll(true);
		}
	}

	public void saveAll()
	{
		if (secureInfoMap != null)
		{
			Context context = getApplication();

			final SharedPreferences prefs = GearUpSharedPreferences.getSharedPreferences(context);
			final SharedPreferences.Editor editor = prefs.edit();
			boolean dirty = false;

			for (Map.Entry<String, SecureInfoViewModel> e : secureInfoMap.entrySet())
			{
				SecureInfoViewModel viewModel = e.getValue();
				if (viewModel.isDirty())
				{
					String value = cryptoUtil.safeEncrypt(context, viewModel.text.get());
					if (TextUtils.isEmpty(value))
						editor.remove(viewModel.key);
					else
						editor.putString(viewModel.key, value);
					viewModel.saved();

					dirty = true;
				}
			}

			if (dirty)
				editor.apply();

			if (Utils.DEBUG)
				Utils.d(LOG_TAG, "saveAll dirty=%s", dirty);
		}
	}

	public SecureInfoViewModel getSsnInfo()
	{
		return secureInfoMap.get(EDIT_SSN_KEY);
	}

	public SecureInfoViewModel getSsn1Info()
	{
		return secureInfoMap.get(EDIT_SSN_1_KEY);
	}

	public SecureInfoViewModel getSsn2Info()
	{
		return secureInfoMap.get(EDIT_SSN_2_KEY);
	}

	public SecureInfoViewModel getDriversLicense()
	{
		return secureInfoMap.get(EDIT_DRIVER_LIC_KEY);
	}

	public SecureInfoViewModel getFsaUsername()
	{
		return secureInfoMap.get(FSA_USERNAME_KEY);
	}

	public SecureInfoViewModel getFsaPassword()
	{
		return secureInfoMap.get(FSA_PASSWORD_KEY);
	}

	public SecureInfoViewModel getAppEmail()
	{
		return secureInfoMap.get(APPLICATION_EMAIL_KEY);
	}

	public SecureInfoViewModel getAppPassword()
	{
		return secureInfoMap.get(APPLICATION_PASSWORD_KEY);
	}

	public SecureInfoViewModel getOrsaaUsername()
	{
		return secureInfoMap.get(ORSAA_USERNAME_KEY);
	}

	public SecureInfoViewModel getOrsaaPassword()
	{
		return secureInfoMap.get(ORSAA_PASSWORD_KEY);
	}

	public SecureInfoViewModel getCssUsername()
	{
		return secureInfoMap.get(CSS_USERNAME_KEY);
	}

	public SecureInfoViewModel getCssPassword()
	{
		return secureInfoMap.get(CSS_PASSWORD_KEY);
	}

	public SecureInfoViewModel getExtraLogin1Organization()
	{
		return secureInfoMap.get(EXTRA_LOGIN_1_ORGANIZATION);
	}

	public SecureInfoViewModel getExtraLogin1Username()
	{
		return secureInfoMap.get(EXTRA_LOGIN_1_USERNAME);
	}

	public SecureInfoViewModel getExtraLogin1Password()
	{
		return secureInfoMap.get(EXTRA_LOGIN_1_PASSWORD);
	}

	public SecureInfoViewModel getExtraLogin2Organization()
	{
		return secureInfoMap.get(EXTRA_LOGIN_2_ORGANIZATION);
	}

	public SecureInfoViewModel getExtraLogin2Username()
	{
		return secureInfoMap.get(EXTRA_LOGIN_2_USERNAME);
	}

	public SecureInfoViewModel getExtraLogin2Password()
	{
		return secureInfoMap.get(EXTRA_LOGIN_2_PASSWORD);
	}

	public SecureInfoViewModel getOsacUsername()
	{
		return secureInfoMap.get(OSAC_USERNAME);
	}

	public SecureInfoViewModel getOsacPassword()
	{
		return secureInfoMap.get(OSAC_PASSWORD);
	}
}

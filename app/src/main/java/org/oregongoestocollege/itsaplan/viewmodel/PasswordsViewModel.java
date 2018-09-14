package org.oregongoestocollege.itsaplan.viewmodel;

import java.util.HashMap;
import java.util.Map;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;

import org.oregongoestocollege.itsaplan.support.CryptoUtil;
import org.oregongoestocollege.itsaplan.support.GearUpSharedPreferences;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class PasswordsViewModel extends ViewModel
{
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

	public PasswordsViewModel()
	{
		cryptoUtil = new CryptoUtil();
	}

	private void lockItemViewModels(boolean locked)
	{
		TransformationMethod transformationMethod = new PasswordTransformationMethod();

		for (Map.Entry<String, SecureInfoViewModel> e : secureInfoMap.entrySet())
		{
			SecureInfoViewModel viewModel = e.getValue();
			if (locked)
			{
				viewModel.transformation.set(transformationMethod);
				viewModel.isEnabled.set(false);
			}
			else
			{
				viewModel.transformation.set(null);
				viewModel.isEnabled.set(true);
			}
		}
	}

	private void addToMap(Context context, String key)
	{
		secureInfoMap.put(key,
			new SecureInfoViewModel(key, cryptoUtil.safeDecrypt(context, prefs.getString(key, null))));
	}

	public void init(Context context, boolean locked)
	{
		// only need to initialize the item view models once
		if (secureInfoMap == null)
		{
			prefs = GearUpSharedPreferences.getSharedPreferences(context);
			secureInfoMap = new HashMap<>(25);

			addToMap(context, EDIT_SSN_KEY);
			addToMap(context, EDIT_SSN_1_KEY);
			addToMap(context, EDIT_SSN_2_KEY);
			addToMap(context, EDIT_DRIVER_LIC_KEY);
			addToMap(context, FSA_USERNAME_KEY);
			addToMap(context, FSA_PASSWORD_KEY);
			addToMap(context, APPLICATION_EMAIL_KEY);
			addToMap(context, APPLICATION_PASSWORD_KEY);
			addToMap(context, ORSAA_USERNAME_KEY);
			addToMap(context, ORSAA_PASSWORD_KEY);
			addToMap(context, CSS_USERNAME_KEY);
			addToMap(context, CSS_PASSWORD_KEY);
			addToMap(context, EXTRA_LOGIN_1_ORGANIZATION);
			addToMap(context, EXTRA_LOGIN_1_USERNAME);
			addToMap(context, EXTRA_LOGIN_1_PASSWORD);
			addToMap(context, EXTRA_LOGIN_2_ORGANIZATION);
			addToMap(context, EXTRA_LOGIN_2_USERNAME);
			addToMap(context, EXTRA_LOGIN_2_PASSWORD);
			addToMap(context, OSAC_USERNAME);
			addToMap(context, OSAC_PASSWORD);
		}

		// then make sure our state is in sync
		lockItemViewModels(locked);
	}

	public void lockSecureInfo()
	{
		if (secureInfoMap != null)
			lockItemViewModels(true);
	}

	public void save(Context context)
	{
		if (secureInfoMap != null)
		{
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

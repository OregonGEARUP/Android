package org.oregongoestocollege.itsaplan.support;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import android.text.TextUtils;

/**
 * Using preferences in private mode for additional security of user's info.
 *
 * Oregon GEAR UP App
 * Copyright © 2020 Oregon GEAR UP. All rights reserved.
 */
public class GearUpSharedPreferences
{
	private static final String PREFS_NAME = "gear_up_shared_prefs";
	private static final String PREFS_KEY_PASSWORDS_PIN = "key_passwords_pin";
	public static final String PREFS_KEY_CRYPTO = "key_crypto_lollipop";

	public static SharedPreferences getSharedPreferences(@NonNull Context context)
	{
		return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
	}

	public static boolean isPasswordsPinCreated(@NonNull Context context)
	{
		final SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		final String pin = prefs.getString(PREFS_KEY_PASSWORDS_PIN, null);
		return !TextUtils.isEmpty(pin);
	}

	public static String getPasswordsPin(@NonNull Context context)
	{
		final SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		return prefs.getString(PREFS_KEY_PASSWORDS_PIN, null);
	}

	public static void putPasswordsPin(@NonNull Context context, @NonNull String value)
	{
		final SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		final SharedPreferences.Editor editor = prefs.edit();

		editor.putString(PREFS_KEY_PASSWORDS_PIN, value);
		editor.apply();
	}
}

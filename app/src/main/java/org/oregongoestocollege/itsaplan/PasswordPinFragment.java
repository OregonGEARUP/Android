package org.oregongoestocollege.itsaplan;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.oregongoestocollege.itsaplan.support.CryptoUtil;
import org.oregongoestocollege.itsaplan.support.GearUpSharedPreferences;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class PasswordPinFragment extends Fragment implements View.OnClickListener, TextWatcher
{
	public static final String ARG_PIN_MODE_CREATE = "argument_pin_mode_create";

	public interface PasswordPinListener
	{
		void onPinCreated();

		void onPinEntered();
	}

	private String storedPin;
	private boolean modeCreate;
	private Button pinButton;
	private EditText pinEditText;
	private PasswordPinListener callback;

	public PasswordPinFragment()
	{
		// Required empty public constructor
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.fragment_password_pin, container, false);

		// determine our mode, should never be null since we only use newInstance()
		modeCreate = getArguments().getBoolean(ARG_PIN_MODE_CREATE);

		return v;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		pinButton = view.findViewById(R.id.button_set_pin);

		TextView textView = view.findViewById(R.id.text_view_pin_message);
		if (modeCreate)
		{
			pinButton.setEnabled(false);
			pinButton.setOnClickListener(this);
			textView.setText(R.string.pin_message_create);
		}
		else
		{
			pinButton.setVisibility(View.GONE);
			textView.setText(R.string.pin_message_enter);
		}

		pinEditText = view.findViewById(R.id.edit_text_pin);
		pinEditText.addTextChangedListener(this);
	}

	@Override
	public void onClick(View view)
	{
		if (modeCreate)
		{
			Context context = getContext();
			String pin = pinEditText.getText().toString();

			// shouldn't get here without a PIN
			if (context != null && !TextUtils.isEmpty(pin))
			{
				CryptoUtil cryptoUtil = new CryptoUtil();
				String encryptedPin = cryptoUtil.safeEncrypt(context, pin);

				if (TextUtils.isEmpty(encryptedPin))
				{
					// shouldn't happen unless CryptoUtil has issues on specific platforms
					// pop a message in case we get any feedback and we can figure out why
					Toast.makeText(context,
						context.getString(R.string.pin_encrypt_error),
						Toast.LENGTH_LONG).
						show();
				}
				else
				{
					GearUpSharedPreferences.putPasswordsPin(context, encryptedPin);

					if (callback != null)
						callback.onPinCreated();
				}
			}
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after)
	{
		// no-op
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count)
	{
		//no-op
	}

	@Override
	public void afterTextChanged(Editable s)
	{
		if (modeCreate)
		{
			pinButton.setEnabled(s.length() == 4);
		}
		else
		{
			if (s.length() == 4)
			{
				if (confirmEnteredPin(pinEditText.getText().toString()))
				{
					// need to dismiss the keyboard for the parent fragment to display the passwords
					// test against phone landscape, TextInputEditText goes into a wizard / next mode...
					InputMethodManager imm =
						(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
					if (imm != null)
						imm.hideSoftInputFromWindow(pinEditText.getWindowToken(), 0);

					if (callback != null)
						callback.onPinEntered();
				}
				else
				{
					pinEditText.setError(getString(R.string.pin_error));
				}
			}
			else
				pinEditText.setError(null);
		}
	}

	private boolean confirmEnteredPin(String enteredPin)
	{
		Context context = getContext();
		if (context == null)
			return false;

		if (TextUtils.isEmpty(storedPin))
		{
			String encryptedPin = GearUpSharedPreferences.getPasswordsPin(context);
			if (!TextUtils.isEmpty(encryptedPin))
			{
				CryptoUtil cryptoUtil = new CryptoUtil();
				storedPin = cryptoUtil.safeDecrypt(context, encryptedPin);
			}
		}

		return TextUtils.equals(enteredPin, storedPin);
	}

	public void setCallback(PasswordPinListener callback)
	{
		this.callback = callback;
	}

	public boolean isModeCreate()
	{
		return modeCreate;
	}

	public static PasswordPinFragment newInstance(boolean createPin)
	{
		Bundle args = new Bundle();
		args.putBoolean(ARG_PIN_MODE_CREATE, createPin);

		PasswordPinFragment fragment = new PasswordPinFragment();
		fragment.setArguments(args);
		return fragment;
	}
}

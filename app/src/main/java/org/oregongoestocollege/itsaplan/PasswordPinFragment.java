package org.oregongoestocollege.itsaplan;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class PasswordPinFragment extends Fragment implements View.OnClickListener, TextWatcher
{
	public static final String ARG_PIN_MODE_CREATE = "argument_pin_mode_create";

	public interface PasswordPinListener
	{
		void onPinCreated(String pin);

		void onPinEntered();
	}

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

		// determine our mode
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
		if (modeCreate && callback != null)
			callback.onPinCreated(pinEditText.getText().toString());
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
				SharedPreferences preferences =
					getContext().getSharedPreferences(PasswordContainerFragment.GEAR_UP_PREFERENCES,
						Context.MODE_PRIVATE);
				String storedPin = preferences.getString(PasswordContainerFragment.PIN_PREFERENCES_KEY, null);

				if (pinEditText.getText().toString().equals(storedPin))
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

	public void setCallback(PasswordPinListener callback)
	{
		this.callback = callback;
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

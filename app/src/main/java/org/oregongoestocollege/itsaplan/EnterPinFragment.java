package org.oregongoestocollege.itsaplan;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class EnterPinFragment extends Fragment
{
	public interface OnPinEnteredListener
	{
		void onPinEntered();
	}

	OnPinEnteredListener callback;

	Button setPinButton;
	TextInputEditText pinEditText;

	public EnterPinFragment()
	{}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
		@Nullable Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.enter_pin, container, false);

		view.findViewById(R.id.setup_pin_message).setVisibility(View.INVISIBLE);
		setPinButton = view.findViewById(R.id.set_pin_button);
		setPinButton.setText(android.R.string.ok);
		setPinButton.setEnabled(false);
		setPinButton.setOnClickListener(v ->
		{
			SharedPreferences preferences = getActivity().getSharedPreferences(PasswordsFragment.GEAR_UP_PREFERENCES,
				Context.MODE_PRIVATE);
			String storedPin = preferences.getString(PasswordContainerFragment.PIN_PREFERENCES_KEY, null);

			if (pinEditText.getText().toString().equals(storedPin))
			{
				callback.onPinEntered();
			}
			else
			{
				pinEditText.setError("PIN doesn't match");
			}

		});

		pinEditText = view.findViewById(R.id.enter_pin);
		pinEditText.addTextChangedListener(new TextWatcher()
		{
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
				if (s.length() == 4)
					setPinButton.setEnabled(true);
				else
					setPinButton.setEnabled(false);
			}
		});

		return view;
	}

	public void setCallback(OnPinEnteredListener callback)
	{
		this.callback = callback;
	}
}

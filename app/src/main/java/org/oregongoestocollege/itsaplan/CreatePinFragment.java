package org.oregongoestocollege.itsaplan;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class CreatePinFragment extends Fragment
{

	public interface OnPinCreatedListener
	{
		void onPinCreated(String pin);
	}

	OnPinCreatedListener callback;

	Button setPinButton;
	EditText pinEditText;

	public CreatePinFragment()
	{}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
		@Nullable Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.enter_pin, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		setPinButton = view.findViewById(R.id.set_pin_button);
		setPinButton.setEnabled(false);
		setPinButton.setOnClickListener(v ->
		{
			if (callback != null)
				callback.onPinCreated(pinEditText.getText().toString());
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
	}

	public void setCallback(OnPinCreatedListener callback)
	{
		this.callback = callback;
	}
}

package org.oregongoestocollege.itsaplan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckpointFragment extends Fragment
{
	private static final String CHECKPOINT_DESC = "desc";
	private String description;

	public CheckpointFragment()
	{
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of this fragment.
	 *
	 * @return A new instance of fragment CheckpointFragment.
	 */
	public static CheckpointFragment newInstance(String description)
	{
		CheckpointFragment fragment = new CheckpointFragment();
		Bundle args = new Bundle();
		args.putString(CHECKPOINT_DESC, description);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_checkpoint, container, false);

		TextView textView = v.findViewById(R.id.text);
		textView.setText(description);

		return v;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if (getArguments() != null)
		{
			description = getArguments().getString(CHECKPOINT_DESC);
		}
	}
}

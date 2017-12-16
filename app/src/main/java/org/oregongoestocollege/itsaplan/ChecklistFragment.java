package org.oregongoestocollege.itsaplan;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.oregongoestocollege.itsaplan.databinding.FragmentChecklistBinding;
import org.oregongoestocollege.itsaplan.support.BindingItemsAdapter;
import org.oregongoestocollege.itsaplan.viewmodel.BlockInfoListViewModel;

/**
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class ChecklistFragment extends Fragment
{
	private OnFragmentInteractionListener mListener;
	private RecyclerView recyclerView;
	private BindingItemsAdapter adapter;
	private BlockInfoListViewModel blockInfoListViewModel;


	public ChecklistFragment()
	{
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of this fragment.
	 *
	 * @return A new instance of fragment ChecklistFragment.
	 */
	public static ChecklistFragment newInstance()
	{
		return new ChecklistFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		FragmentChecklistBinding
			binding = DataBindingUtil.inflate(inflater, R.layout.fragment_checklist, container, false);
		View v = binding.getRoot();

		adapter = new BindingItemsAdapter();

		recyclerView = v.findViewById(R.id.recycler_view);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

		blockInfoListViewModel = obtainViewModel(getActivity());
		binding.setUxContext(blockInfoListViewModel);

		blockInfoListViewModel.getUpdateListEvent().observe(this, new Observer<String>()
		{
			@Override
			public void onChanged(@Nullable String s)
			{
				if (adapter.getItemCount() != 0)
					adapter.clear();

				adapter.addAll(blockInfoListViewModel.getItems());
			}
		});

		return v;
	}

	@Override
	public void onResume()
	{
		super.onResume();
		blockInfoListViewModel.start();
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri)
	{
		if (mListener != null)
		{
			mListener.onFragmentInteraction();
		}
	}

	@Override
	public void onAttach(Context context)
	{
		super.onAttach(context);
		if (context instanceof OnFragmentInteractionListener)
		{
			mListener = (OnFragmentInteractionListener)context;
		}
		else
		{
			throw new RuntimeException(context.toString()
				+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach()
	{
		super.onDetach();
		mListener = null;
	}

	public static BlockInfoListViewModel obtainViewModel(Activity activity)
	{
		// Use a Factory to inject dependencies into the ViewModel
		ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());

		// future - could use ViewModelProviders

		return factory.create(BlockInfoListViewModel.class);
	}
}

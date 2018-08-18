package org.oregongoestocollege.itsaplan;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.oregongoestocollege.itsaplan.databinding.FragmentChecklistBlockBinding;
import org.oregongoestocollege.itsaplan.support.BindingItem;
import org.oregongoestocollege.itsaplan.support.BindingItemsAdapter;
import org.oregongoestocollege.itsaplan.support.ItemClickCallback;
import org.oregongoestocollege.itsaplan.viewmodel.BlockViewModel;
import org.oregongoestocollege.itsaplan.viewmodel.StageItemViewModel;

/**
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class ChecklistBlockFragment extends Fragment implements ItemClickCallback
{
	private static final String LOG_TAG = "GearUp_ChecklistBlockFrag";
	private OnFragmentInteractionListener listener;
	private int blockIndex = Utils.NO_INDEX;
	private String blockFileName;
	private RecyclerView recyclerView;
	private BindingItemsAdapter adapter;
	private BlockViewModel blockViewModel;

	public ChecklistBlockFragment()
	{
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of this fragment.
	 *
	 * @return A new instance of fragment ChecklistBlockFragment.
	 */
	public static ChecklistBlockFragment newInstance(int blockIndex, String blockFileName)
	{
		ChecklistBlockFragment fragment = new ChecklistBlockFragment();
		Bundle args = new Bundle();
		args.putString(Utils.PARAM_BLOCK_FILE_NAME, blockFileName);
		args.putInt(Utils.PARAM_BLOCK_INDEX, blockIndex);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);

		if (!TextUtils.isEmpty(blockFileName))
			outState.putString(Utils.PARAM_BLOCK_FILE_NAME, blockFileName);
		if (blockIndex != Utils.NO_INDEX)
			outState.putInt(Utils.PARAM_BLOCK_INDEX, blockIndex);
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		if (getArguments() != null)
		{
			blockFileName = getArguments().getString(Utils.PARAM_BLOCK_FILE_NAME);
			blockIndex = getArguments().getInt(Utils.PARAM_BLOCK_INDEX);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		FragmentChecklistBlockBinding binding =
			DataBindingUtil.inflate(inflater, R.layout.fragment_checklist_block, container, false);
		View v = binding.getRoot();

		if (savedInstanceState != null)
		{
			blockFileName = savedInstanceState.getString(Utils.PARAM_BLOCK_FILE_NAME);
			blockIndex = savedInstanceState.getInt(Utils.PARAM_BLOCK_INDEX, Utils.NO_INDEX);
		}

		adapter = new BindingItemsAdapter(this);

		recyclerView = v.findViewById(R.id.recycler_view);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

		blockViewModel = ViewModelProviders.of(this).get(BlockViewModel.class);
		binding.setUxContext(blockViewModel);

		blockViewModel.getUpdateListEvent().observe(this, new Observer<Void>()
		{
			@Override
			public void onChanged(@Nullable Void aVoid)
			{
				if (adapter.getItemCount() != 0)
					adapter.clear();

				adapter.addAll(blockViewModel.getItems());

				getActivity().setTitle(blockViewModel.getTitle());
			}
		});

		return v;
	}

	@Override
	public void onResume()
	{
		super.onResume();
		blockViewModel.start(blockIndex, blockFileName);
	}

	@Override
	public void onAttach(Context context)
	{
		super.onAttach(context);

		if (context instanceof OnFragmentInteractionListener)
			listener = (OnFragmentInteractionListener)context;
		else
			throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");

		Utils.d(LOG_TAG, "onAttach");
	}

	@Override
	public void onDetach()
	{
		super.onDetach();
		listener = null;
	}

	@Override
	public void onClick(BindingItem item)
	{
		if (!(item instanceof StageItemViewModel))
			return;

		if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED) && listener != null)
		{
			StageItemViewModel vm = (StageItemViewModel)item;
			listener.onShowStage(vm.getBlockIndex(), vm.getStageIndex());
		}
	}
}

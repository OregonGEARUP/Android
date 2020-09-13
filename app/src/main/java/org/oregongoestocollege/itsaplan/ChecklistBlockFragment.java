package org.oregongoestocollege.itsaplan;

import java.util.List;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.oregongoestocollege.itsaplan.data.ChecklistState;
import org.oregongoestocollege.itsaplan.databinding.FragmentChecklistBlockBinding;
import org.oregongoestocollege.itsaplan.support.BindingItem;
import org.oregongoestocollege.itsaplan.support.BindingItemsAdapter;
import org.oregongoestocollege.itsaplan.support.ItemClickCallback;
import org.oregongoestocollege.itsaplan.viewmodel.BlockViewModel;
import org.oregongoestocollege.itsaplan.viewmodel.ChecklistNavViewModel;
import org.oregongoestocollege.itsaplan.viewmodel.StageItemViewModel;

/**
 * Oregon GEAR UP App
 * Copyright © 2020 Oregon GEAR UP. All rights reserved.
 */
public class ChecklistBlockFragment extends Fragment implements ItemClickCallback
{
	private static final String LOG_TAG = "GearUp_ChecklistBlockFrag";
	private BindingItemsAdapter adapter;
	private BlockViewModel viewModel;
	private int blockIndex = Utils.NO_INDEX;
	private String blockFileName;

	public ChecklistBlockFragment()
	{
		// Required empty public constructor
	}

	private void onItemsChanged(List<BindingItem> items)
	{
		if (Utils.DEBUG)
			Utils.d(LOG_TAG, "onItemsChanged()");

		// Update the list when the data changes
		if (adapter.getItemCount() != 0)
			adapter.clear();

		if (items != null && !items.isEmpty())
			adapter.addAll(items);
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState)
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

		if (Utils.DEBUG)
			Utils.d(LOG_TAG, "onCreate");

		if (getArguments() != null)
		{
			blockFileName = getArguments().getString(Utils.PARAM_BLOCK_FILE_NAME);
			blockIndex = getArguments().getInt(Utils.PARAM_BLOCK_INDEX);
		}

		viewModel = ViewModelProviders.of(this).get(BlockViewModel.class);
		viewModel.getBlockItems().observe(this, this::onItemsChanged);
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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

		RecyclerView recyclerView = v.findViewById(R.id.recycler_view);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

		binding.setUxContext(viewModel);

		return v;
	}

	@Override
	public void onResume()
	{
		super.onResume();
		viewModel.start(blockIndex, blockFileName);
	}

	@Override
	public void onClick(BindingItem item)
	{
		if (!(item instanceof StageItemViewModel))
			return;

		if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED))
		{
			FragmentActivity activity = getActivity();
			if (activity == null)
				return;

			StageItemViewModel itemViewModel = (StageItemViewModel)item;

			ChecklistState state = new ChecklistState(viewModel.getBlockFileName(),
				viewModel.getBlockIndex(), itemViewModel.getStageIndex());

			// trigger a state change to load the correct fragment
			ChecklistNavViewModel cvm = ViewModelProviders.of(activity).get(ChecklistNavViewModel.class);
			cvm.setCurrentState(state);
		}
	}

	/**
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
}

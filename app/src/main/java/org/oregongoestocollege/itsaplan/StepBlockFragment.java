package org.oregongoestocollege.itsaplan;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.oregongoestocollege.itsaplan.data.Stage;
import org.oregongoestocollege.itsaplan.databinding.FragmentStepBlockBinding;
import org.oregongoestocollege.itsaplan.support.BindingItemsAdapter;
import org.oregongoestocollege.itsaplan.viewmodel.BlockViewModel;

/**
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class StepBlockFragment extends Fragment
{
	private WeakReference<OnChecklistInteraction> listener;
	private String blockFileName;
	private RecyclerView recyclerView;
	private BindingItemsAdapter adapter;
	private BlockViewModel blockViewModel;

	public StepBlockFragment()
	{
		// Required empty public constructor
	}

	public void init(OnChecklistInteraction listener, String blockFileName)
	{
		this.listener = new WeakReference<>(listener);
		this.blockFileName = blockFileName;
	}

	/**
	 * Use this factory method to create a new instance of this fragment.
	 *
	 * @return A new instance of fragment StepBlockFragment.
	 */
	public static StepBlockFragment newInstance()
	{
		return new StepBlockFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		FragmentStepBlockBinding binding =
			DataBindingUtil.inflate(inflater, R.layout.fragment_step_block, container, false);
		View v = binding.getRoot();

		adapter = new BindingItemsAdapter();

		recyclerView = v.findViewById(R.id.recycler_view);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

		blockViewModel = obtainViewModel(getActivity());
		binding.setUxContext(blockViewModel);

		blockViewModel.getUpdateListEvent().observe(this, new Observer<Void>()
		{
			@Override
			public void onChanged(@Nullable Void aVoid)
			{
				if (adapter.getItemCount() != 0)
					adapter.clear();

				adapter.addAll(blockViewModel.getItems());
			}
		});

		blockViewModel.getOpenStageEvent().observe(this, new Observer<Stage>()
		{
			@Override
			public void onChanged(@Nullable Stage stage)
			{
				if (listener != null)
					listener.get().onShowStage(stage);
			}
		});

		return v;
	}

	@Override
	public void onResume()
	{
		super.onResume();
		blockViewModel.start(blockFileName);
	}

	public static BlockViewModel obtainViewModel(Activity activity)
	{
		// Use a Factory to inject dependencies into the ViewModel
		ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());

		// future - could use ViewModelProviders

		return factory.create(BlockViewModel.class);
	}
}

package org.oregongoestocollege.itsaplan;

import java.util.List;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.oregongoestocollege.itsaplan.data.Scholarship;
import org.oregongoestocollege.itsaplan.databinding.FragmentMyPlanScholarshipsBinding;
import org.oregongoestocollege.itsaplan.support.BindingItemsAdapter;
import org.oregongoestocollege.itsaplan.viewmodel.BindingItem;
import org.oregongoestocollege.itsaplan.viewmodel.ScholarshipViewModel;
import org.oregongoestocollege.itsaplan.viewmodel.ScholarshipsViewModel;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class MyPlanScholarshipsFragment extends Fragment
{
	private RecyclerView recyclerView;
	private BindingItemsAdapter adapter;
	private ScholarshipsViewModel viewModel;

	public MyPlanScholarshipsFragment()
	{
		// Required empty public constructor
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
		@Nullable Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		FragmentMyPlanScholarshipsBinding binding =
			DataBindingUtil.inflate(inflater, R.layout.fragment_my_plan_scholarships, container, false);
		View v = binding.getRoot();

		adapter = new BindingItemsAdapter();

		recyclerView = v.findViewById(R.id.recycler_view);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

		viewModel = ViewModelProviders.of(this).get(ScholarshipsViewModel.class);
		viewModel.getAllScholarships().observe(this, new Observer<List<Scholarship>>()
		{
			@Override
			public void onChanged(@Nullable List<Scholarship> scholarships)
			{
				Utils.d(ScholarshipViewModel.LOG_TAG, "All scholarships changed");

				List<BindingItem> items = viewModel.getItems(scholarships);

				if (adapter.getItemCount() != 0)
					adapter.clear();

				adapter.addAll(items);
			}
		});

		// bind to fragment to get onClick for floating actions
		binding.setUxContext(viewModel);

		return v;
	}

	@Override
	public void onDetach()
	{
		if (viewModel != null)
			viewModel.update();

		super.onDetach();
	}
}

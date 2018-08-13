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
import org.oregongoestocollege.itsaplan.support.BindingItem;
import org.oregongoestocollege.itsaplan.support.BindingItemsAdapter;
import org.oregongoestocollege.itsaplan.viewmodel.ScholarshipViewModel;
import org.oregongoestocollege.itsaplan.viewmodel.ScholarshipsViewModel;

/**
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class MyPlanScholarshipsFragment extends Fragment
{
	private FragmentMyPlanScholarshipsBinding binding;
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
		binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_plan_scholarships, container, false);
		View v = binding.getRoot();

		adapter = new BindingItemsAdapter(null);

		RecyclerView recyclerView = v.findViewById(R.id.recycler_view);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

		return v;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		Utils.d(ScholarshipViewModel.LOG_TAG, "onActivityCreated");

		viewModel = ViewModelProviders.of(this).get(ScholarshipsViewModel.class);

		// observe live data
		viewModel.getAllScholarships().removeObservers(this);
		viewModel.getAllScholarships().observe(this, new Observer<List<Scholarship>>()
		{
			@Override
			public void onChanged(@Nullable List<Scholarship> scholarships)
			{
				Utils.d(ScholarshipViewModel.LOG_TAG, String.format("All scholarhips changed hasData:%s",
					scholarships != null ? "true" : "false"));

				if (scholarships != null)
				{
					if (scholarships.isEmpty())
					{
						// keep the loading indicator while we insert the first entry
						// will get another onChanged() when insert is complete
						viewModel.insertFirstScholarship(getContext());
					}
					else
					{
						binding.setIsLoading(false);

						List<BindingItem> items = viewModel.getItems(scholarships);
						if (adapter.getItemCount() != 0)
							adapter.clear();
						adapter.addAll(items);
					}
				}
				else
				{
					binding.setIsLoading(true);
				}
			}
		});

		// bind to fragment to get isLoading/onClick for floating actions
		binding.setUxContext(viewModel);
	}

	@Override
	public void onDetach()
	{
		if (viewModel != null)
			viewModel.update();

		super.onDetach();
	}
}

package org.oregongoestocollege.itsaplan;

import java.util.List;

import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.oregongoestocollege.itsaplan.databinding.FragmentMyPlanScholarshipsBinding;
import org.oregongoestocollege.itsaplan.support.BindingItem;
import org.oregongoestocollege.itsaplan.support.BindingItemsAdapter;
import org.oregongoestocollege.itsaplan.viewmodel.ScholarshipsViewModel;

/**
 * Oregon GEAR UP App
 * Copyright © 2020 Oregon GEAR UP. All rights reserved.
 */
public class MyPlanScholarshipsFragment extends Fragment
{
	private static final String LOG_TAG = "GearUp_ScholarshipFrag";
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

		if (Utils.DEBUG)
			Utils.d(LOG_TAG, "onActivityCreated");

		viewModel = ViewModelProviders.of(this).get(ScholarshipsViewModel.class);

		// observe live data
		viewModel.getAllScholarships().removeObservers(this);
		viewModel.getAllScholarships().observe(getViewLifecycleOwner(), scholarships ->
		{
			if (scholarships != null)
			{
				if (scholarships.isEmpty())
				{
					if (Utils.DEBUG)
						Utils.d(LOG_TAG, "All scholarships changed - inserting first scholarhip");

					// keep the loading indicator while we insert the first entry
					// will get another onChanged() when insert is complete
					viewModel.insertFirstScholarship(getContext());
				}
				else if (viewModel.checkFirstScholarship(getContext(), scholarships))
				{
					// keep loading indicator while we update the first entry with user entered
					// data. will get another onChanged() when update is complete
					if (Utils.DEBUG)
						Utils.d(LOG_TAG, "All scholarships changed - updating first scholarhip");
				}
				else
				{
					if (Utils.DEBUG)
						Utils.d(LOG_TAG, "All scholarships changed - displaying scholarships");

					binding.setIsLoading(false);

					List<BindingItem> items = viewModel.getItems(scholarships);
					if (adapter.getItemCount() != 0)
						adapter.clear();
					adapter.addAll(items);
				}
			}
			else
			{
				if (Utils.DEBUG)
					Utils.d(LOG_TAG, "All scholarships changed - loading scholarships");

				binding.setIsLoading(true);
			}
		});

		// bind to fragment to get isLoading/onClick for floating actions
		binding.setUxContext(viewModel);
	}

	@Override
	public void onDestroyView()
	{
		super.onDestroyView();

		binding = null;
	}

	@Override
	public void onStop()
	{
		super.onStop();

		if (Utils.DEBUG)
			Utils.d(LOG_TAG, "onStop");

		if (viewModel != null)
			viewModel.stop();
	}
}

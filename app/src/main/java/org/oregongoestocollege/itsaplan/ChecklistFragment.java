package org.oregongoestocollege.itsaplan;

import java.util.Locale;

import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.oregongoestocollege.itsaplan.data.BlockInfo;
import org.oregongoestocollege.itsaplan.data.ChecklistState;
import org.oregongoestocollege.itsaplan.data.CheckpointRepository;
import org.oregongoestocollege.itsaplan.data.Stage;
import org.oregongoestocollege.itsaplan.viewmodel.ChecklistNavViewModel;

/**
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class ChecklistFragment extends BaseFragment implements OnFragmentInteractionListener
{
	private static final String LOG_TAG = "GearUp_ChecklistFrag";
	private static final String FRAG_OVERVIEW = "frag-overview";
	private static final String FRAG_BLOCK = "frag-block";
	private static final String FRAG_STAGE = "frag-stage";
	private ChecklistNavViewModel navViewModel;
	private ViewGroup welcomeContainer;
	private View welcomeView;

	public ChecklistFragment()
	{
		// Required empty public constructor
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		if (Utils.DEBUG)
			Utils.d(LOG_TAG, "onCreate");

		navViewModel = ViewModelProviders.of(getActivity()).get(ChecklistNavViewModel.class);
		navViewModel.getCurrentState().observe(this, this::onChecklistNavStateChanged);

		getChildFragmentManager().addOnBackStackChangedListener(this::onBackStackChanged);

		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
		@Nullable Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_checklist, container, false);

		showWelcome(inflater, (ViewGroup)v);

		if (savedInstanceState == null)
		{
			showOverview();

			// TODO - start where we left off...
			/*if (currentBlockIndex >= 0 && currentStageIndex >= 0)
			showStage(currentBlockIndex, currentStageIndex);
		else if (currentBlockIndex >= 0)
			showBlock(currentBlockIndex, currentBlockFileName);
		else
			showBlocks();*/
		}

		return v;
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu)
	{
		super.onPrepareOptionsMenu(menu);

		Utils.disableMenu(menu);
	}

	void onBackStackChanged()
	{
		// see if we need to set our title from a child fragment
		int backStackCount = Utils.updateTitleOnBackStackChanged(this, LOG_TAG);

		// show / hide the back button as appropriate
		setHomeAsUpEnabled(backStackCount > 0);

		// clear the last nav we might have seen since it's no longer accurate
		navViewModel.clear();
	}

	void onChecklistNavStateChanged(ChecklistState state)
	{
		// if the state was cleared, do nothing
		if (state == null)
			return;

		if (state.hasBlockAndStageIndex())
		{
			if (Utils.DEBUG)
				Utils.d(LOG_TAG, "onChecklistNavStateChanged Stage");

			showStage(state.blockIndex, state.stageIndex);
		}
		else if (state.hasBlockIndexAndFile())
		{
			if (Utils.DEBUG)
				Utils.d(LOG_TAG, "onChecklistNavStateChanged Block");

			showBlock(state.blockIndex, state.blockFileName);
		}
		else
		{
			if (Utils.DEBUG)
				Utils.d(LOG_TAG, "onChecklistNavStateChanged Overview");

			showOverview();
		}
	}

	private void showOverview()
	{
		if (Utils.DEBUG)
			Utils.d(LOG_TAG, "showOverview");

		// make sure to use getChildFragmentManager versus getSupportFragmentManager
		FragmentManager manager = getChildFragmentManager();

		if (manager.getBackStackEntryCount() <= 0)
		{
			FragmentTransaction transaction = manager.beginTransaction();
			transaction.replace(R.id.fragment_container_checklist, ChecklistOverviewFragment.newInstance());
			transaction.setBreadCrumbTitle(R.string.app_name);
			transaction.addToBackStack(FRAG_OVERVIEW);
			transaction.commit();
		}
		else
			manager.popBackStack(FRAG_OVERVIEW, 0);

		// show / hide the back button as appropriate
		setHomeAsUpEnabled(false);
	}

	private void showBlock(int blockIndex, String blockFileName)
	{
		if (Utils.DEBUG)
			Utils.d(LOG_TAG, "showBlock");

		// make sure to use getChildFragmentManager versus getSupportFragmentManager
		FragmentManager manager = getChildFragmentManager();

		// if we have more than 1 entry, pop back to overview
		if (manager.getBackStackEntryCount() > 1)
			manager.popBackStack(FRAG_OVERVIEW, 0);

		// match the fragment title to the block title
		BlockInfo blockInfo = CheckpointRepository.getInstance(getContext()).getBlockInfo(blockIndex);
		String title = null;
		if (blockInfo != null)
			title = String.format(Locale.getDefault(), "%d. %s", blockIndex + 1, blockInfo.getTitle());

		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(R.id.fragment_container_checklist,
			ChecklistBlockFragment.newInstance(blockIndex, blockFileName));
		if (!TextUtils.isEmpty(title))
			transaction.setBreadCrumbTitle(title);
		transaction.addToBackStack(FRAG_BLOCK);
		transaction.commit();

		// show / hide the back button as appropriate
		setHomeAsUpEnabled(true);
	}

	private void showStage(int blockIndex, int stageIndex)
	{
		if (Utils.DEBUG)
			Utils.d(LOG_TAG, "showStage");

		// make sure to use getChildFragmentManager versus getSupportFragmentManager
		FragmentManager manager = getChildFragmentManager();

		// if we have more than 2 entries, pop back to block
		if (manager.getBackStackEntryCount() > 2)
			manager.popBackStack(FRAG_BLOCK, 0);

		// match the fragment title to the stage title
		Stage stage = CheckpointRepository.getInstance(getContext()).getStage(blockIndex, stageIndex);
		String title = stage != null ? stage.title : null;

		// then add our new one
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(R.id.fragment_container_checklist,
			ChecklistStageFragment.newInstance(blockIndex, stageIndex));
		if (!TextUtils.isEmpty(title))
			transaction.setBreadCrumbTitle(title);
		transaction.addToBackStack(FRAG_STAGE);
		transaction.commit();

		// show / hide the back button as appropriate
		setHomeAsUpEnabled(true);
	}

	private void showWelcome(@NonNull LayoutInflater inflater, @Nullable ViewGroup container)
	{
		if (container == null || welcomeView != null)
			return;

		// have we shown the welcome screen?
		Context context = getContext();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		if (!prefs.getBoolean("welcome_done", false))
		{
			welcomeView = inflater.inflate(R.layout.layout_welcome, null);
			// apply green to the number 10
			TextView textView = welcomeView.findViewById(R.id.text2);
			String msg = context.getString(R.string.welcome_message);
			int startIndex = msg.indexOf("10");
			int endIndex = startIndex + 2;
			SpannableString ss = new SpannableString(textView.getText());
			ss.setSpan(new TextAppearanceSpan(context, R.style.GearUpWelcomeCountGreen), startIndex, endIndex, 0);
			textView.setText(ss);

			Button btn = welcomeView.findViewById(R.id.button);
			btn.setOnClickListener(this::hideWelcome);
			container.addView(welcomeView);
			welcomeContainer = container;
		}
	}

	private void hideWelcome(View view)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(view.getContext());
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("welcome_done", true).apply();

		if (welcomeView != null)
		{
			welcomeContainer.removeView(welcomeView);
			welcomeView = null;
			welcomeContainer = null;
		}
	}

	@Override
	public boolean handleBackPressed()
	{
		if (Utils.DEBUG)
			Utils.d(LOG_TAG, "handleBackPressed");

		FragmentManager manager = getChildFragmentManager();
		if (manager.getBackStackEntryCount() > 1)
		{
			manager.popBackStack();
			return true;
		}

		return false;
	}

	@Override
	public boolean canHandleBackPressed()
	{
		if (Utils.DEBUG)
			Utils.d(LOG_TAG, "canHandleBackPressed");

		FragmentManager manager = getChildFragmentManager();
		return manager.getBackStackEntryCount() > 1;
	}
}

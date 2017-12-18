package org.oregongoestocollege.itsaplan;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import org.oregongoestocollege.itsaplan.data.CheckpointRepository;
import org.oregongoestocollege.itsaplan.viewmodel.BlockInfoListViewModel;
import org.oregongoestocollege.itsaplan.viewmodel.BlockViewModel;

/**
 * A creator for our view models that could be extended to inject dependencies
 * into our ViewModels.
 *
 * Oregon GEAR UP App
 * Copyright © 2017 Oregon GEAR UP. All rights reserved.
 */
public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory
{
	@SuppressLint("StaticFieldLeak")
	private static volatile ViewModelFactory instance;
	private final Application application;

	public static ViewModelFactory getInstance(Application application)
	{
		if (instance == null)
		{
			synchronized (ViewModelFactory.class)
			{
				if (instance == null)
				{
					instance = new ViewModelFactory(application);
				}
			}
		}
		return instance;
	}

	@VisibleForTesting
	public static void destroyInstance()
	{
		instance = null;
	}

	private ViewModelFactory(Application application)
	{
		this.application = application;
	}

	@Override
	public <T extends ViewModel> T create(@NonNull Class<T> modelClass)
	{
		if (modelClass.isAssignableFrom(BlockInfoListViewModel.class))
		{
			//noinspection unchecked
			return (T)new BlockInfoListViewModel(application, CheckpointRepository.getInstance());
		}
		if (modelClass.isAssignableFrom(BlockViewModel.class))
		{
			//noinspection unchecked
			return (T)new BlockViewModel(application, CheckpointRepository.getInstance());
		}
		throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
	}
}

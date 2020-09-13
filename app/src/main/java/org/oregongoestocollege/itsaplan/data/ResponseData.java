package org.oregongoestocollege.itsaplan.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * A generic class that contains data and status about loading data.
 *
 * Oregon GEAR UP App
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
public class ResponseData<T>
{
	@NonNull
	public final Status status;
	@Nullable
	public final T data;
	@Nullable
	public final String message;

	public enum Status
	{
		SUCCESS, ERROR, LOADING
	}

	private ResponseData(@NonNull Status status, @Nullable T data,
		@Nullable String message)
	{
		this.status = status;
		this.data = data;
		this.message = message;
	}

	public static <T> ResponseData<T> success(@NonNull T data)
	{
		return new ResponseData<>(Status.SUCCESS, data, null);
	}

	public static <T> ResponseData<T> error(String msg, @Nullable T data)
	{
		return new ResponseData<>(Status.ERROR, data, msg);
	}

	public static <T> ResponseData<T> loading(@Nullable T data)
	{
		return new ResponseData<>(Status.LOADING, data, null);
	}
}

package org.oregongoestocollege.itsaplan.data.dao;

import java.util.List;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import org.oregongoestocollege.itsaplan.data.Password;


/**
 * College
 * Oregon GEAR UP App
 *
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
@Dao
public interface PasswordsDao
{
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insert(Password password);

	@Update
	void update(Password password);

	@Delete
	void delete(Password password);

	@Query("SELECT * FROM password_table")
	LiveData<List<Password>> getAll();
}

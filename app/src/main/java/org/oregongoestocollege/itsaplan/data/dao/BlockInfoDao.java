package org.oregongoestocollege.itsaplan.data.dao;

import java.util.List;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import org.oregongoestocollege.itsaplan.data.BlockInfo;

/**
 * BlockInfo
 * Oregon GEAR UP App
 *
 * Copyright © 2018 Oregon GEAR UP. All rights reserved.
 */
@Dao
public interface BlockInfoDao
{
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insertAll(BlockInfo... blockInfos);

	@Update
	void update(BlockInfo blockInfo);

	@Query("SELECT * from block_info_table")
	LiveData<List<BlockInfo>> getAll();
}

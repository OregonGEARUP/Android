package org.oregongoestocollege.itsaplan.data.dao;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import org.oregongoestocollege.itsaplan.data.BlockInfo;

/**
 * BlockInfo
 * Oregon GEAR UP App
 *
 * Copyright © 2020 Oregon GEAR UP. All rights reserved.
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

	@Query("SELECT * from block_info_table")
	BlockInfo[] getAllDirect();
}

package com.websarva.wings.dostudy_android.model.Room.PlatformData

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PlatformDataDao {
    @Insert
    suspend fun insert(platformDataTable: PlatformDataTable)

    @Update
    suspend fun update(platformDataTable: PlatformDataTable)

    @Delete
    suspend fun delete(platformDataTable: PlatformDataTable)

    @Query("SELECT * FROM platformdatatable")
    suspend fun getAllPlatformData(): List<PlatformDataTable>
}
package com.websarva.wings.dostudy_android.model.Room.ResultData

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ResultDataDao {
    @Insert
    suspend fun insert(resultDataTable: ResultDataTable)

    @Update
    suspend fun update(resultDataTable: ResultDataTable)

    @Delete
    suspend fun delete(resultDataTable: ResultDataTable)

    @Query("SELECT * FROM resultdatatable")
    suspend fun getAllResultData(): List<ResultDataTable>
}
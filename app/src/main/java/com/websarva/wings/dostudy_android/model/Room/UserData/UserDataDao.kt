package com.websarva.wings.dostudy_android.model.Room.UserData

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDataDao {
    @Insert
    suspend fun insert(userDataTable: UserDataTable)

    @Update
    suspend fun update(userDataTable: UserDataTable)

    @Delete
    suspend fun delete(userDataTable: UserDataTable)

    @Query("SELECT * FROM userdatatable WHERE id = 0 LIMIT 1") // idが0のデータを取得
    suspend fun getCurrentUser(): UserDataTable?
}
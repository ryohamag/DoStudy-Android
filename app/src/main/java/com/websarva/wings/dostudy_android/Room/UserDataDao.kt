package com.websarva.wings.dostudy_android.Room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDataDao {
    @Insert
    suspend fun insert(userDataTable: UserDataTable)

    @Update
    suspend fun update(userDataTable: UserDataTable)

    @Query("SELECT * FROM userdatatable LIMIT 1") // 最初の1件を取得
    suspend fun getCurrentUser(): UserDataTable?
}
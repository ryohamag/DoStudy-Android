package com.websarva.wings.dostudy_android.model.Room.ToDoData

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ToDoDataDao {
    @Insert
    suspend fun insert(toDoDataTable: ToDoDataTable)

    @Update
    suspend fun update(toDoDataTable: ToDoDataTable)

    @Delete
    suspend fun delete(toDoDataTable: ToDoDataTable)

    @Query("SELECT * FROM ToDoDataTable")
    suspend fun getAllToDoData(): List<ToDoDataTable>
}
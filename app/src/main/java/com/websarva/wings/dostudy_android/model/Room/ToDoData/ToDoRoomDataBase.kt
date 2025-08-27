package com.websarva.wings.dostudy_android.model.Room.ToDoData

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.websarva.wings.dostudy_android.model.Room.Converters

@Database(entities = [ToDoDataTable::class], version = 1)
@TypeConverters(Converters::class)
abstract class ToDoRoomDataBase() : RoomDatabase() {
    abstract fun toDoDataDao(): ToDoDataDao
}

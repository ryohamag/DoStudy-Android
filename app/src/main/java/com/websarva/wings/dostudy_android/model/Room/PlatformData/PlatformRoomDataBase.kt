package com.websarva.wings.dostudy_android.model.Room.PlatformData

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.websarva.wings.dostudy_android.model.Room.Converters

@Database(entities = [PlatformDataTable::class], version = 1)
@TypeConverters(Converters::class)
abstract class PlatformRoomDataBase : RoomDatabase() {
    abstract fun platformDataDao(): PlatformDataDao
}
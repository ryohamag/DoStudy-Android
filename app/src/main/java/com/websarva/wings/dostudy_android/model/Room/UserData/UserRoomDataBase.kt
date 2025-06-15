package com.websarva.wings.dostudy_android.model.Room.UserData

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.websarva.wings.dostudy_android.model.Room.Converters

@Database(entities = [UserDataTable::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class UserRoomDataBase: RoomDatabase() {
    abstract fun userDataDao(): UserDataDao
}

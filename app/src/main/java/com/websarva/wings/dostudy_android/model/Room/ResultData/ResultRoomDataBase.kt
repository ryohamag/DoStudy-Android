package com.websarva.wings.dostudy_android.model.Room.ResultData

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.websarva.wings.dostudy_android.model.Room.Converters
import com.websarva.wings.dostudy_android.model.Room.UserData.UserDataTable

@Database(entities = [ResultDataTable::class, UserDataTable::class], version = 1)
@TypeConverters(Converters::class)
abstract class ResultRoomDataBase(): RoomDatabase() {
    abstract fun resultDataDao(): ResultDataDao
}

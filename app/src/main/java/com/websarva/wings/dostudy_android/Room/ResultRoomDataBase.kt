package com.websarva.wings.dostudy_android.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [ResultDataTable::class, UserDataTable::class], version = 1)
@TypeConverters(Converters::class)
abstract class ResultRoomDataBase(): RoomDatabase() {
    abstract fun resultDataDao(): ResultDataDao

    companion object {
        @Volatile
        private var Instance: ResultRoomDataBase? = null

        fun getResultRoomDataBase(context: Context): ResultRoomDataBase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context = context,
                    klass = ResultRoomDataBase::class.java,
                    name = "result_database"
                )
                .build()
                .also { Instance = it }
            }
        }
    }

}

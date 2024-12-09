package com.websarva.wings.dostudy_android.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserDataTable::class], version = 1, exportSchema = false)
abstract class UserRoomDataBase: RoomDatabase() {
    abstract fun userDataDao(): UserDataDao

    companion object {
        @Volatile
        private var Instance: UserRoomDataBase? = null

        fun getUserRoomDataBase(context: Context): UserRoomDataBase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context = context,
                    klass = UserRoomDataBase::class.java,
                    name = "user_database"
                )
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
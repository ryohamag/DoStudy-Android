package com.websarva.wings.dostudy_android.Room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserDataTable::class], version = 1, exportSchema = false)
abstract class UserRoomDataBase: RoomDatabase() {
    abstract fun userDataDao(): UserDataDao

    companion object {
        @Volatile
        private var INSTANCE: UserRoomDataBase? = null
        fun getDatabase(context: android.content.Context): UserRoomDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    UserRoomDataBase::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
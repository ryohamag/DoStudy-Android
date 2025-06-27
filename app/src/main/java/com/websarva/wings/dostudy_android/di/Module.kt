package com.websarva.wings.dostudy_android.di

import android.content.Context
import androidx.room.Room
import com.websarva.wings.dostudy_android.OrientationSensor
import com.websarva.wings.dostudy_android.model.Room.ResultData.ResultDataDao
import com.websarva.wings.dostudy_android.model.Room.ResultData.ResultRoomDataBase
import com.websarva.wings.dostudy_android.model.Room.ToDoData.ToDoDataDao
import com.websarva.wings.dostudy_android.model.Room.ToDoData.ToDoRoomDataBase
import com.websarva.wings.dostudy_android.model.Room.UserData.UserDataDao
import com.websarva.wings.dostudy_android.model.Room.UserData.UserRoomDataBase
import com.websarva.wings.dostudy_android.model.notification.NotificationHelper
import com.websarva.wings.dostudy_android.model.repository.Repository
import com.websarva.wings.dostudy_android.viewmodel.MainViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @Singleton
    fun provideNotificationHelper(@ApplicationContext context: Context): NotificationHelper {
        return NotificationHelper(context)
    }

    @Provides
    fun provideMainViewModel(
        repository: Repository, orientationSensor: OrientationSensor
    ): MainViewModel {
        return MainViewModel(repository, orientationSensor)
    }

    @Provides
    @Singleton
    fun provideOrientationSensor(@ApplicationContext context: Context): OrientationSensor {
        return OrientationSensor(context)
    }

    @Provides
    @Singleton
    fun provideResultDataDao(db: ResultRoomDataBase) = db.resultDataDao()

    @Provides
    @Singleton
    fun provideUserDataDao(db: UserRoomDataBase) = db.userDataDao()

    @Provides
    @Singleton
    fun provideToDoDataDao(db: ToDoRoomDataBase) = db.toDoDataDao()

    @Provides
    @Singleton
    fun provideRepository(
        resultDataDao: ResultDataDao,
        userDataDao: UserDataDao,
        toDoDataDao: ToDoDataDao
    ): Repository {
        return Repository(resultDataDao, userDataDao, toDoDataDao)
    }

    @Provides
    @Singleton
    fun provideUserRoomDataBase(@ApplicationContext context: Context): UserRoomDataBase =
        Room.databaseBuilder(
            context,
            UserRoomDataBase::class.java,
            "user_database"
        ).build()

    @Provides
    @Singleton
    fun provideResultRoomDataBase(@ApplicationContext context: Context): ResultRoomDataBase =
        Room.databaseBuilder(
            context,
            ResultRoomDataBase::class.java,
            "result_database"
        ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideToDoDataBase(@ApplicationContext context: Context): ToDoRoomDataBase {
        return Room.databaseBuilder(
            context,
            ToDoRoomDataBase::class.java,
            "todo_database"
        ).fallbackToDestructiveMigration().build()
    }
}
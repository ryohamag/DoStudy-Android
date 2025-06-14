package com.websarva.wings.dostudy_android.di

import android.content.Context
import androidx.room.Room
import com.websarva.wings.dostudy_android.OrientationSensor
import com.websarva.wings.dostudy_android.model.Room.ResultDataDao
import com.websarva.wings.dostudy_android.model.Room.ResultRoomDataBase
import com.websarva.wings.dostudy_android.model.Room.UserDataDao
import com.websarva.wings.dostudy_android.model.Room.UserRoomDataBase
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
    fun provideMainViewModel(repository: Repository, orientationSensor: OrientationSensor): MainViewModel {
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
    fun provideRepository(
        resultDataDao: ResultDataDao,
        userDataDao: UserDataDao
    ): Repository {
        return Repository(resultDataDao, userDataDao)
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
}
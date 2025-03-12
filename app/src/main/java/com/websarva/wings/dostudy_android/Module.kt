package com.websarva.wings.dostudy_android

import android.content.Context
import com.websarva.wings.dostudy_android.viewmodels.MainScreenViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object Module {
    @Provides
    fun provideMainViewModel(@ApplicationContext context: Context): MainScreenViewModel {
        return MainScreenViewModel(context = context)
    }
}
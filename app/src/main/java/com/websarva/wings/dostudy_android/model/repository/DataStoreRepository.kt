package com.websarva.wings.dostudy_android.model.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "settings")

@Singleton
class DataStoreRepository @Inject constructor(
    private val context: Context
) {
    companion object {
        private val DAILY_LIMIT_KEY = intPreferencesKey("daily_limit")
        private val SELECTED_FONT_KEY = intPreferencesKey("selected_font")
    }

    suspend fun saveDailyLimit(limit: Int) {
        context.dataStore.edit { preferences ->
            preferences[DAILY_LIMIT_KEY] = limit
        }
    }

    fun getDailyLimit(): Flow<Int> {
        return context.dataStore.data.map { preferences ->
            preferences[DAILY_LIMIT_KEY] ?: 120 // デフォルト値120分
        }
    }

    suspend fun saveSelectedFont(fontIndex: Int) {
        context.dataStore.edit { preferences ->
            preferences[SELECTED_FONT_KEY] = fontIndex
        }
    }

    fun getSelectedFont(): Flow<Int> {
        return context.dataStore.data.map { preferences ->
            preferences[SELECTED_FONT_KEY] ?: 0 // デフォルト値0
        }
    }
}

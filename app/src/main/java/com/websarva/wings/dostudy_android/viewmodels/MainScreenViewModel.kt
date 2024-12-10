package com.websarva.wings.dostudy_android.viewmodels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.websarva.wings.dostudy_android.Room.UserDataTable
import com.websarva.wings.dostudy_android.Room.UserRoomDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainScreenViewModel(context: Context) : ViewModel() {
    private val db = UserRoomDataBase.getUserRoomDataBase(context)
    private val userDataDao = db.userDataDao()

    var isSettingsDialogOpen by mutableStateOf(false)
    var isFirstStartup by mutableStateOf(false)
    var username by mutableStateOf("")
    var channelId by mutableStateOf("")
    var isTimerMode by mutableStateOf(false)
    var isShowTimerSetMenu by mutableStateOf(false)

    init {
        viewModelScope.launch {
            getCurrentUserData()
        }
    }

    private suspend fun getCurrentUserData() {
        val userData = withContext(Dispatchers.IO) {
            userDataDao.getCurrentUser()
        }
        Log.d("MainScreenViewModel", "userData: $userData")
        if(userData == null) {
            isFirstStartup = true
        }
        username = userData?.username ?: ""
        channelId = userData?.channelId ?: ""
    }

    fun createUserData() {
        viewModelScope.launch {
            val newUserData = UserDataTable(username = username, channelId = channelId)
            try {
                userDataDao.insert(newUserData)
                Log.d("MainScreenViewModel", "Data inserted successfully: $newUserData")
            } catch (e: Exception) {
                Log.e("MainScreenViewModel", "Error inserting data", e)
            }
        }
    }

    fun updateUserData() {
        viewModelScope.launch {
            val updatedUserData = UserDataTable(username = username, channelId = channelId)
            try {
                userDataDao.update(updatedUserData)
                Log.d("MainScreenViewModel", "Data updated successfully: $updatedUserData")
            } catch (e: Exception) {
                Log.e("MainScreenViewModel", "Error updating data", e)
            }
        }
    }
}
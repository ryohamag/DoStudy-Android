package com.websarva.wings.dostudy_android.viewmodels

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.websarva.wings.dostudy_android.Room.UserDataTable
import com.websarva.wings.dostudy_android.Room.UserRoomDataBase
import kotlinx.coroutines.launch

class MainScreenViewModel(context: Context) : ViewModel() {
    private val db = UserRoomDataBase.getUserRoomDataBase(context)
    private val userDataDao = db.userDataDao()

    var isSettingsDialogOpen by mutableStateOf(false)
    var username by mutableStateOf("")
    var channelId by mutableStateOf("")

    fun getCurrentUserData() {
        viewModelScope.launch {
            val userData = userDataDao.getCurrentUser()
            if (userData == null) {
                username = ""
                channelId = ""
            } else {
                username = userData.username
                channelId = userData.channelId
            }
        }
    }

    fun createUserData() {
        viewModelScope.launch {
            val newUserData = UserDataTable(username = username, channelId = channelId)
            userDataDao.insert(newUserData)
        }
    }
}
package com.websarva.wings.dostudy_android.viewmodels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.websarva.wings.dostudy_android.OrientationSensor
import com.websarva.wings.dostudy_android.Room.UserDataTable
import com.websarva.wings.dostudy_android.Room.UserRoomDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    var isStudyStarted by mutableStateOf(false)
    val orientationSensor = OrientationSensor(context)
    var seconds by mutableIntStateOf(0)
    private var _timerList = MutableStateFlow(listOf(1800, 3600, 7200, 10800))
    var timerList: StateFlow<List<Int>> = _timerList.asStateFlow()
    var addedTimerList by mutableStateOf<List<Int>>(listOf())
    var selectedTimer by mutableStateOf<Int?>(null)
//    var inputTimer by mutableStateOf(TextFieldValue("00h00m00s"))
    var inputTimer by mutableIntStateOf(0)
    var isShowTimerAddingDialog by mutableStateOf(false)
    var isShowFailedDialog by mutableStateOf(false)
    var isShowSuccessDialog by mutableStateOf(false)
    var responseMessage by mutableStateOf("")

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
        } else {
            username = userData.username
            channelId = userData.channelId
            addedTimerList = userData.addedTimerList
        }
    }

    fun createUserData() {
        viewModelScope.launch {
            val newUserData = UserDataTable(username = username, channelId = channelId, addedTimerList = addedTimerList)
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
            val updatedUserData = UserDataTable(username = username, channelId = channelId, addedTimerList = addedTimerList)
            try {
                userDataDao.update(updatedUserData)
                Log.d("MainScreenViewModel", "Data updated successfully: $updatedUserData")
            } catch (e: Exception) {
                Log.e("MainScreenViewModel", "Error updating data", e)
            }
        }
    }

    fun addTimer(seconds: Int) {
        _timerList.value += seconds // 新しい時間を追加
    }

    fun deleteTimer(timerToDelete: Int) {
        addedTimerList = addedTimerList.filter { it != timerToDelete }
        _timerList.value = _timerList.value.filter { it != timerToDelete }
        updateUserData()
    }

    fun reset() {
        seconds = 0
        isTimerMode = false
        isStudyStarted = false
        selectedTimer = null
    }
}
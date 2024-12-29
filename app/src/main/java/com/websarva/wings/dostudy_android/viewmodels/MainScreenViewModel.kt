package com.websarva.wings.dostudy_android.viewmodels

import android.content.Context
import android.security.identity.ResultData
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.websarva.wings.dostudy_android.OrientationSensor
import com.websarva.wings.dostudy_android.Room.ResultDataTable
import com.websarva.wings.dostudy_android.Room.ResultRoomDataBase
import com.websarva.wings.dostudy_android.Room.UserDataTable
import com.websarva.wings.dostudy_android.Room.UserRoomDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.util.concurrent.TimeUnit

class MainScreenViewModel(context: Context) : ViewModel() {
    private val db = UserRoomDataBase.getUserRoomDataBase(context)
    private val userDataDao = db.userDataDao()

    private val resultDb = ResultRoomDataBase.getResultRoomDataBase(context)
    private val resultDataDao = resultDb.resultDataDao()

    val orientationSensor = OrientationSensor(context) //画面の向きを検知するクラス

    var isSettingsDialogOpen by mutableStateOf(false) //設定ダイアログを開くかどうか
    var isFirstStartup by mutableStateOf(false) //初回起動かどうか
    var username by mutableStateOf("") //ユーザー名
    var channelId by mutableStateOf("") //チャンネルID
    var isTimerMode by mutableStateOf(false) //タイマーモードかどうか
    var isStudyStarted by mutableStateOf(false) //勉強が始まっているかどうか
    var seconds by mutableIntStateOf(0) //経過時間
    private var _timerList = MutableStateFlow(listOf(1800, 3600, 7200, 10800)) //タイマーリスト
    var timerList: StateFlow<List<Int>> = _timerList.asStateFlow() //タイマーリスト
    var addedTimerList by mutableStateOf<List<Int>>(listOf()) //追加したタイマーリスト
    var selectedTimer by mutableStateOf<Int?>(null) //選択されたタイマー
    var setTimer by mutableStateOf<Int?>(null) //設定されたタイマー
    var isShowTimerAddingDialog by mutableStateOf(false) //タイマー追加ダイアログを表示するかどうか
    var isShowFailedDialog by mutableStateOf(false) //失敗ダイアログを表示するかどうか
    var isShowSuccessDialog by mutableStateOf(false) //成功ダイアログを表示するかどうか
    var responseMessage by mutableStateOf("") //レスポンスメッセージ
    var resultDataList by mutableStateOf<List<ResultDataTable>>(listOf()) //結果データリスト
    var isShowStopTimerDialog by mutableStateOf(false) //タイマーを止めるダイアログを表示するかどうか
    var isShowChart by mutableStateOf(true) //チャートを表示するかどうか
    var isShowAdScreen by mutableStateOf(false) //広告画面を表示するかどうか

    //初期化
    init {
        viewModelScope.launch {
            getCurrentUserData()
            getAllResultData()
        }
    }

    //ユーザーデータを取得
    private suspend fun getCurrentUserData() {
        val userData = withContext(Dispatchers.IO) {
            userDataDao.getCurrentUser()
        }
        if(userData == null) {
            isFirstStartup = true
        } else {
            username = userData.username
            channelId = userData.channelId
            addedTimerList = userData.addedTimerList
        }
    }

    //ユーザーデータを作成
    fun createUserData() {
        viewModelScope.launch {
            val newUserData = UserDataTable(username = username, channelId = channelId, addedTimerList = addedTimerList)
            try {
                userDataDao.insert(newUserData)
            } catch (e: Exception) {
                Log.e("MainScreenViewModel", "Error inserting data", e)
            }
        }
    }

    //ユーザーデータを更新
    fun updateUserData() {
        viewModelScope.launch {
            val updatedUserData = UserDataTable(username = username, channelId = channelId, addedTimerList = addedTimerList)
            try {
                userDataDao.update(updatedUserData)
            } catch (e: Exception) {
                Log.e("MainScreenViewModel", "Error updating data", e)
            }
        }
    }

    //結果データを追加
    fun addResultData(status: Boolean) {
        val currentDate: LocalDate = LocalDate.now()
        val setTimer = setTimer?.let {
            val setHours = TimeUnit.SECONDS.toHours(it.toLong()).toString().padStart(2, '0')
            val setMinutes = (TimeUnit.SECONDS.toMinutes(it.toLong()) % 60).toString().padStart(2, '0')
            val setSeconds = (it % 60).toString().padStart(2, '0')
            "${setHours}:${setMinutes}:${setSeconds}"
        }

        val seconds = seconds.let {
            val hours = TimeUnit.SECONDS.toHours(it.toLong()).toString().padStart(2, '0')
            val minutes = (TimeUnit.SECONDS.toMinutes(it.toLong()) % 60).toString().padStart(2, '0')
            val seconds = (it % 60).toString().padStart(2, '0')
            "${hours}:${minutes}:${seconds}"
        }


        viewModelScope.launch {
            val resultData = ResultDataTable(date = currentDate.toString(), setTimer = setTimer, studyTime = seconds, status = status)
            try {
                resultDataDao.insert(resultData)
                resultDataList += resultData
            } catch (e: Exception) {
                Log.e("MainScreenViewModel", "Error inserting data", e)
            }
        }
    }

    //結果データを全件取得
    private fun getAllResultData() {
        viewModelScope.launch {
            resultDataList = withContext(Dispatchers.IO) {
                resultDataDao.getAllResultData()
            }
        }
    }

    //タイマーを追加
    fun addTimer(time: String) {
        val seconds = time.chunked(2).map { it.toInt() }.let { (hours, minutes, seconds) ->
            hours * 3600 + minutes * 60 + seconds
        }
        addedTimerList += seconds // 新しい時間を追加
    }

    //タイマーを削除
    fun deleteTimer(timerToDelete: Int) {
        addedTimerList = addedTimerList.filter { it != timerToDelete }
        _timerList.value = _timerList.value.filter { it != timerToDelete }
        updateUserData()
    }

    //変数をリセット
    fun reset() {
        isTimerMode = false
        seconds = 0
        isStudyStarted = false
        selectedTimer = null
        setTimer = null
    }
}
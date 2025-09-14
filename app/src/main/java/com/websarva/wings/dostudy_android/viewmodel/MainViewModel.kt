package com.websarva.wings.dostudy_android.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.websarva.wings.dostudy_android.OrientationSensor
import com.websarva.wings.dostudy_android.functions.orientSensor
import com.websarva.wings.dostudy_android.model.Room.PlatformData.PlatformDataTable
import com.websarva.wings.dostudy_android.model.Room.ResultData.ResultDataTable
import com.websarva.wings.dostudy_android.model.Room.ToDoData.ToDoDataTable
import com.websarva.wings.dostudy_android.model.Room.UserData.UserDataTable
import com.websarva.wings.dostudy_android.model.repository.DataStoreRepository
import com.websarva.wings.dostudy_android.model.repository.PlatformDataRepository
import com.websarva.wings.dostudy_android.model.repository.ResultDataRepository
import com.websarva.wings.dostudy_android.model.repository.ScreenTimeRepository
import com.websarva.wings.dostudy_android.model.repository.ToDoDataRepository
import com.websarva.wings.dostudy_android.model.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val orientationSensor: OrientationSensor,
    private val resultDataRepository: ResultDataRepository,
    private val userDataRepository: UserDataRepository,
    private val toDoDataRepository: ToDoDataRepository,
    private val platformDataRepository: PlatformDataRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val screenTimeRepository: ScreenTimeRepository
) : ViewModel() {

    // ===== センサー関連 StateFlow =====
    private var _orientation: MutableStateFlow<FloatArray> = orientationSensor.orientation as MutableStateFlow<FloatArray>
    var orientation: StateFlow<FloatArray> = _orientation.asStateFlow()

    // ===== タイマー関連 StateFlow =====
    private val _seconds = MutableStateFlow(0)
    val seconds: StateFlow<Int> = _seconds.asStateFlow()

    private val _addedTimerList = MutableStateFlow<List<Int>>(listOf())
    val addedTimerList: StateFlow<List<Int>> = _addedTimerList.asStateFlow()

    private val _selectedTimer = MutableStateFlow<Int?>(null)
    val selectedTimer: StateFlow<Int?> = _selectedTimer.asStateFlow()

    private val _setTimer = MutableStateFlow<Int?>(null)
    var setTimer: StateFlow<Int?> = _setTimer.asStateFlow()

    // ===== データ関連 StateFlow =====
    private val _resultDataList = MutableStateFlow<List<ResultDataTable>>(listOf())
    val resultDataList: StateFlow<List<ResultDataTable>> = _resultDataList.asStateFlow()

    private val _todoList = MutableStateFlow<List<ToDoDataTable>>(listOf())
    val todoList: StateFlow<List<ToDoDataTable>> = _todoList.asStateFlow()

    private val _screenTimeData = MutableStateFlow<List<Pair<String, Long>>>(emptyList())
    val screenTimeData: StateFlow<List<Pair<String, Long>>> = _screenTimeData.asStateFlow()

    private val _platformData = MutableStateFlow<List<PlatformDataTable>>(emptyList())
    val platformData: StateFlow<List<PlatformDataTable>> = _platformData.asStateFlow()

    private val _selectedFont = MutableStateFlow(0)
    val selectedFont: StateFlow<Int> = _selectedFont.asStateFlow()

    // ===== 勉強・タイマー関連 状態変数 =====
    var isTimerMode by mutableStateOf(false) // タイマーモードかどうか
    var studyTitle by mutableStateOf("") // 勉強タイトル
    var isStudyStarted by mutableStateOf(false) // 勉強が始まっているかどうか

    // ===== UI関連 状態変数 =====
    var isFirstStartup by mutableStateOf(false) // 起動時だけの一時的なフラグ
    var isShowTimerAddingDialog by mutableStateOf(false)
    var isShowFailedDialog by mutableStateOf(false)
    var isShowSuccessDialog by mutableStateOf(false)
    var isShowStopTimerDialog by mutableStateOf(false)
    var isShowChart by mutableStateOf(true)
    var isShowAdScreen by mutableStateOf(false)
    var isShowStudyTitleDialog by mutableStateOf(false)
    var responseMessage by mutableStateOf("")

    // ===== ユーザー関連 状態変数 =====
    var username by mutableStateOf("")
    var channelId by mutableStateOf("")

    // ===== Job管理 =====
    private var timerJob: Job? = null
    private var orientationJob: Job? = null

    // ===== 初期化 =====
    init {
        viewModelScope.launch {
            // 結果データを全件取得
            _resultDataList.value = withContext(Dispatchers.IO) {
                resultDataRepository.getAllResultData()
            }

            // ユーザーデータを取得
            val userData = withContext(Dispatchers.IO) {
                userDataRepository.getCurrentUser()
            }
            if (userData == null) {
                isFirstStartup = true
            } else {
                username = userData.username
                channelId = userData.channelId
                _addedTimerList.value = userData.addedTimerList
            }

            launch {
                dataStoreRepository.getSelectedFont().collect { font ->
                    _selectedFont.value = font
                }
            }
        }
    }

    // ===== 勉強セッション制御 =====
    fun startStudy() {
        isStudyStarted = true
        startTimer()
        startOrientationMonitoring()
    }

    fun stopStudy() {
        isStudyStarted = false
        stopTimer()
        reset()
        stopOrientationMonitoring()
    }

    //変数をリセット
    fun reset() {
        isTimerMode = false
        _seconds.value = 0
        isStudyStarted = false
        _selectedTimer.value = null
        _setTimer.value = null
    }

    // ===== タイマー制御 =====
    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (isStudyStarted) {
                delay(1000)
                if (!isTimerMode || selectedTimer.value == null) {
                    incrementSeconds()
                } else {
                    // カウントダウン(タイマー)モード
                    decrementSeconds()
                    incrementSeconds()
                    if (selectedTimer.value == 0) {
                        // カウントが0になったら成功ダイアログを表示
                        isShowSuccessDialog = true
                        stopStudy() // 自動停止
                        break
                    }
                }
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    //1秒ずつ増やす
    fun incrementSeconds() {
        if(isStudyStarted) _seconds.value++
    }

    fun decrementSeconds() {
        _selectedTimer.value = _selectedTimer.value!! - 1
    }

    fun setTimer(time: Int) {
        _selectedTimer.value = time
        _setTimer.value = time
    }

    fun resetTimer() {
        _selectedTimer.value = null
        _setTimer.value = null
    }

    //タイマーを追加
    fun addTimer(time: String) {
        val seconds = time.chunked(2).map { it.toInt() }.let { (hours, minutes, seconds) ->
            hours * 3600 + minutes * 60 + seconds
        }
        _addedTimerList.value += seconds // 新しい時間を追加
    }

    //タイマーを削除
    fun deleteTimer(timerToDelete: Int) {
        _addedTimerList.value = _addedTimerList.value.filter { it != timerToDelete }
        updateUserData()
    }

    // ===== センサー制御 =====
    private fun startOrientationMonitoring() {
        orientationSensor.start()
        orientationJob = viewModelScope.launch {
            while (isStudyStarted) {
                delay(5000) // 5秒ごとに角度を計測
                orientSensor(orientation.value, this@MainViewModel)
            }
        }
    }

    private fun stopOrientationMonitoring() {
        orientationSensor.stop()
        orientationJob?.cancel()
        orientationJob = null
    }

    // ===== データ操作（結果データ） =====
    //結果データを追加
    fun addResultData(status: Boolean) {
        val currentDate: LocalDate = LocalDate.now()
        val setTimer = _setTimer.value?.let {
            val setHours = TimeUnit.SECONDS.toHours(it.toLong()).toString().padStart(2, '0')
            val setMinutes = (TimeUnit.SECONDS.toMinutes(it.toLong()) % 60).toString().padStart(2, '0')
            val setSeconds = (it % 60).toString().padStart(2, '0')
            "${setHours}:${setMinutes}:${setSeconds}"
        }

        Log.d("MainScreenViewModel", "setTimer: ${_setTimer.value}")
        Log.d("MainScreenViewModel", "seconds: ${_seconds.value}")

        val seconds = _seconds.value.let {
            val hours = TimeUnit.SECONDS.toHours(it.toLong()).toString().padStart(2, '0')
            val minutes = (TimeUnit.SECONDS.toMinutes(it.toLong()) % 60).toString().padStart(2, '0')
            val seconds = (it % 60).toString().padStart(2, '0')
            "${hours}:${minutes}:${seconds}"
        }


        viewModelScope.launch {
            val resultData = ResultDataTable(
                date = currentDate.toString(), setTimer = setTimer, studyTime = seconds,
                status = status, studyTitle = studyTitle
            )
            try {
                resultDataRepository.insertResultData(resultData)
                _resultDataList.value += resultData
            } catch (e: Exception) {
                Log.e("MainScreenViewModel", "Error inserting data", e)
            }
        }
    }

    // ===== データ操作（ユーザーデータ） =====
    //ユーザーデータを更新
    fun updateUserData() {
        viewModelScope.launch {
            val updatedUserData = UserDataTable(
                username = username, channelId = channelId, addedTimerList = addedTimerList.value
            )
            try {
                userDataRepository.updateUserData(updatedUserData)
            } catch (e: Exception) {
                Log.e("MainScreenViewModel", "Error updating data", e)
            }
        }
    }

    fun getUserData() {
        viewModelScope.launch {
            val userData = withContext(Dispatchers.IO) {
                userDataRepository.getCurrentUser()
            }
            if (userData != null) {
                username = userData.username
                channelId = userData.channelId
                _addedTimerList.value = userData.addedTimerList
            }
        }
    }

    // ===== データ取得 =====
    fun getToDoList() {
        viewModelScope.launch {
            try {
                _todoList.value = withContext(Dispatchers.IO) {
                    toDoDataRepository.getAllToDoData()
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error fetching ToDo list", e)
            }
        }
    }

    fun getScreenTimeData() {
        viewModelScope.launch {
            try {
                val data = withContext(Dispatchers.IO) {
                    screenTimeRepository.getScreenTimeData()
                }
                _screenTimeData.value = data
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error getting screen time data", e)
                _screenTimeData.value = emptyList()
            }
        }
    }

    fun getPlatformData() {
        viewModelScope.launch {
            try {
                val data = withContext(Dispatchers.IO) {
                    platformDataRepository.getAllPlatformData()
                }
                _platformData.value = data
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error getting platform data", e)
                _platformData.value = emptyList()
            }
        }
    }
}

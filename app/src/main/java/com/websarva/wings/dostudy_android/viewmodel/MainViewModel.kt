package com.websarva.wings.dostudy_android.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.websarva.wings.dostudy_android.OrientationSensor
import com.websarva.wings.dostudy_android.model.Room.ResultData.ResultDataTable
import com.websarva.wings.dostudy_android.model.Room.ToDoData.ToDoDataTable
import com.websarva.wings.dostudy_android.model.Room.UserData.UserDataTable
import com.websarva.wings.dostudy_android.model.repository.DataStoreRepository
import com.websarva.wings.dostudy_android.model.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
    private val repository: Repository,
    private val orientationSensor: OrientationSensor,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {
    private var _orientation: MutableStateFlow<FloatArray> = orientationSensor.orientation as MutableStateFlow<FloatArray>
    var orientation: StateFlow<FloatArray> = _orientation.asStateFlow()

    private val _seconds = MutableStateFlow(0)
    val seconds: StateFlow<Int> = _seconds.asStateFlow()

    private val _addedTimerList = MutableStateFlow<List<Int>>(listOf())
    val addedTimerList: StateFlow<List<Int>> = _addedTimerList.asStateFlow()

    private val _selectedTimer = MutableStateFlow<Int?>(null)
    val selectedTimer: StateFlow<Int?> = _selectedTimer.asStateFlow()

    private val _setTimer = MutableStateFlow<Int?>(null)
    var setTimer: StateFlow<Int?> = _setTimer.asStateFlow()

    private val _resultDataList = MutableStateFlow<List<ResultDataTable>>(listOf())
    val resultDataList: StateFlow<List<ResultDataTable>> = _resultDataList.asStateFlow()

    private val _todoList = MutableStateFlow<List<ToDoDataTable>>(listOf())
    val todoList: StateFlow<List<ToDoDataTable>> = _todoList.asStateFlow()

    private val _dailyLimit = MutableStateFlow(120)
    val dailyLimit: StateFlow<Int> = _dailyLimit.asStateFlow()

    var isTimerMode by mutableStateOf(false) // タイマーモードかどうか
    var studyTitle by mutableStateOf("") // 勉強タイトル
    var isStudyStarted by mutableStateOf(false) // 勉強が始まっているかどうか
    var isFirstStartup by mutableStateOf(false) // 起動時だけの一時的なフラグ
    var isShowTimerAddingDialog by mutableStateOf(false)
    var isShowFailedDialog by mutableStateOf(false)
    var isShowSuccessDialog by mutableStateOf(false)
    var isShowStopTimerDialog by mutableStateOf(false)
    var isShowChart by mutableStateOf(true)
    var isShowAdScreen by mutableStateOf(false)
    var isShowStudyTitleDialog by mutableStateOf(false)
    var responseMessage by mutableStateOf("")
    var selectedFont by mutableIntStateOf(0)
    var username by mutableStateOf("")
    var channelId by mutableStateOf("")
    var isShowAddToDoDialog by mutableStateOf(false)

    //初期化
    init {
        viewModelScope.launch {
            // 結果データを全件取得
            _resultDataList.value = withContext(Dispatchers.IO) {
                repository.getAllResultData()
            }

            // ユーザーデータを取得
            val userData = withContext(Dispatchers.IO) {
                repository.getCurrentUser()
            }
            if (userData == null) {
                isFirstStartup = true
            } else {
                username = userData.username
                channelId = userData.channelId
                _addedTimerList.value = userData.addedTimerList
            }

            dataStoreRepository.getDailyLimit().collect { limit ->
                _dailyLimit.value = limit
            }
        }
    }

    //1秒ずつ増やす
    fun incrementSeconds() {
        _seconds.value++
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

    //ユーザーデータを作成
    fun createUserData() {
        viewModelScope.launch {
            val newUserData = UserDataTable(
                username = username, channelId = channelId, addedTimerList = addedTimerList.value
            )
            try {
                repository.insertUserData(newUserData)
            } catch (e: Exception) {
                Log.e("MainScreenViewModel", "Error inserting data", e)
            }
        }
    }

    //ユーザーデータを更新
    fun updateUserData() {
        viewModelScope.launch {
            val updatedUserData = UserDataTable(
                username = username, channelId = channelId, addedTimerList = addedTimerList.value
            )
            try {
                repository.updateUserData(updatedUserData)
            } catch (e: Exception) {
                Log.e("MainScreenViewModel", "Error updating data", e)
            }
        }
    }

    //結果データを追加
    fun addResultData(status: Boolean) {
        val currentDate: LocalDate = LocalDate.now()
        val setTimer = _setTimer.value?.let {
            val setHours = TimeUnit.SECONDS.toHours(it.toLong()).toString().padStart(2, '0')
            val setMinutes = (TimeUnit.SECONDS.toMinutes(it.toLong()) % 60).toString().padStart(2, '0')
            val setSeconds = (it % 60).toString().padStart(2, '0')
            "${setHours}:${setMinutes}:${setSeconds}"
        }

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
                repository.insertResultData(resultData)
                _resultDataList.value += resultData
            } catch (e: Exception) {
                Log.e("MainScreenViewModel", "Error inserting data", e)
            }
        }
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

    fun getToDoList() {
        viewModelScope.launch {
            try {
                _todoList.value = withContext(Dispatchers.IO) {
                    repository.getAllToDoData()
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error fetching ToDo list", e)
            }
        }
    }

    //タスクを追加
    fun addToDo(todo: String) {
        val todoData = ToDoDataTable(title = todo)
        _todoList.value = _todoList.value + todoData
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    repository.addToDoData(todoData)
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error adding ToDo", e)
            }
        }
    }

    fun deleteToDo(todo: ToDoDataTable) {
        _todoList.value = _todoList.value - todo
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    repository.deleteToDoData(todo)
                }
                // データベースから削除成功後にローカル状態を更新
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error deleting ToDo", e)
                // エラーが発生した場合は、ローカル状態を元に戻すか
                // 最新のデータを再取得する
                getToDoList()
            }
        }
    }

    fun saveDailyLimit(limit: Int) {
        viewModelScope.launch {
            dataStoreRepository.saveDailyLimit(limit)
        }
    }

    //変数をリセット
    fun reset() {
        isTimerMode = false
        _seconds.value = 0
        isStudyStarted = false
        _selectedTimer.value = null
        _setTimer.value = null
    }

    fun startSensor() {
        orientationSensor.start()
    }

    fun stopSensor() {
        orientationSensor.stop()
    }
}
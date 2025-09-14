package com.websarva.wings.dostudy_android.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.websarva.wings.dostudy_android.model.Room.ToDoData.ToDoDataTable
import com.websarva.wings.dostudy_android.model.repository.ToDoDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.collections.plus

data class ToDoScreenUiState(
    val todoList: List<ToDoDataTable> = emptyList(),
    val selectedToDos: List<ToDoDataTable> = emptyList(),
    val isSwapMode: Boolean = false,
    val isLoading: Boolean = false,
    val isShowAddToDoDialog: Boolean = false,
)

@HiltViewModel
class ToDoScreenViewModel @Inject constructor(
    private val toDoDataRepository: ToDoDataRepository
) : ViewModel() {

    // ===== UI状態管理 =====
    private val _uiState = MutableStateFlow(ToDoScreenUiState())
    val uiState = _uiState.asStateFlow()

    // ===== 初期化 =====
    init {
        viewModelScope.launch {
            getToDoList()
        }
    }

    // ===== ToDoデータ操作 =====
    fun getToDoList() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val todoList = withContext(Dispatchers.IO) {
                    toDoDataRepository.getAllToDoData().sortedBy { it.position }
                }
                _uiState.update { it.copy(todoList = todoList, isLoading = false) }
            } catch (e: Exception) {
                Log.e("ToDoScreenViewModel", "Error getting todo list", e)
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun addToDo(title: String) {
        viewModelScope.launch {
            // Fractional Indexing
            val lastPosition = _uiState.value.todoList.lastOrNull()?.position ?: 0.0
            val newPosition = lastPosition + 1.0

            val newToDo = ToDoDataTable(title = title, position = newPosition)

            _uiState.update { it.copy(todoList = it.todoList + newToDo) }

            try {
                withContext(Dispatchers.IO) { toDoDataRepository.addToDoData(newToDo) }
                getToDoList()
            } catch (e: Exception) {
                Log.e("ToDoScreenViewModel", "Error adding todo", e)
                _uiState.update { it.copy(todoList = it.todoList - newToDo) }
            }
        }
    }

    fun deleteToDo(todo: ToDoDataTable) {
        viewModelScope.launch {
            _uiState.update { it.copy(todoList = it.todoList - todo) }
            try {
                withContext(Dispatchers.IO) {
                    toDoDataRepository.deleteToDoData(todo)
                }
            } catch (e: Exception) {
                Log.e("ToDoScreenViewModel", "Error deleting todo", e)
                _uiState.update { it.copy(todoList = it.todoList + todo) }
            }
        }
    }

    // ===== ToDo並び替え機能 =====
    fun selectToDo(toDo: ToDoDataTable) {
        val currentSelected = _uiState.value.selectedToDos
        val newSelectedList: List<ToDoDataTable>

        if (currentSelected.contains(toDo)) {
            newSelectedList = currentSelected - toDo
            _uiState.update { it.copy(selectedToDos = newSelectedList) }
        } else if (currentSelected.size < 2) {
            newSelectedList = currentSelected + toDo
            _uiState.update { it.copy(selectedToDos = newSelectedList) }
            if (newSelectedList.size == 2) {
                swapToDos(newSelectedList[0], newSelectedList[1])
            }
        }
    }

    private fun swapToDos(toDo1: ToDoDataTable, toDo2: ToDoDataTable) {
        viewModelScope.launch {
            try {
                val position1 = toDo1.position
                val position2 = toDo2.position

                val updatedToDo1 = toDo1.copy(position = position2)
                val updatedToDo2 = toDo2.copy(position = position1)

                withContext(Dispatchers.IO) {
                    toDoDataRepository.updateToDoData(updatedToDo1)
                    toDoDataRepository.updateToDoData(updatedToDo2)
                }

                getToDoList()
                _uiState.update { it.copy(selectedToDos = emptyList()) }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error swapping todos", e)
            }
        }
    }

    fun onSwapModeChange() {
        _uiState.update { it.copy(isSwapMode = !it.isSwapMode) }
    }

    // ===== UI状態制御 =====
    fun onShowAddToDoDialog() {
        // ViewModelの持つStateを、新しいStateで更新する
        _uiState.value = uiState.value.copy(isShowAddToDoDialog = true)
    }

    fun onDismissAddToDoDialog() {
        _uiState.value = uiState.value.copy(isShowAddToDoDialog = false)
    }
}

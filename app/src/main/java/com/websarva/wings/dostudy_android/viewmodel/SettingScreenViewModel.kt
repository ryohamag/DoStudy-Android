package com.websarva.wings.dostudy_android.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.websarva.wings.dostudy_android.model.Room.PlatformData.PlatformDataTable
import com.websarva.wings.dostudy_android.model.Room.UserData.UserDataTable
import com.websarva.wings.dostudy_android.model.repository.DataStoreRepository
import com.websarva.wings.dostudy_android.model.repository.PlatformDataRepository
import com.websarva.wings.dostudy_android.model.repository.UserDataRepository
import com.websarva.wings.dostudy_android.util.PlatformConstants.platforms
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class SettingScreenUiState(
    val dailyLimit: Int = 120, // デフォルト値120分
    val selectedPlatform: Int = 0,
    val selectedFont: Int = 0,
    val username: String = "",
    val channelId: String = "",
    val channelName: String = "",
    val platformKey: String = "",
    val platformData: List<PlatformDataTable> = emptyList(),
    val isFirstStartup: Boolean = false,
    val isLoading: Boolean = false,
    val isShowPlatformDialog: Boolean = false,
    val fontsExpanded: Boolean = false,
    val platformExpanded: Boolean = false
)

@HiltViewModel
class SettingScreenViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val platformDataRepository: PlatformDataRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val userData = withContext(Dispatchers.IO) {
                userDataRepository.getCurrentUser()
            }
            if (userData == null) {
                _uiState.update { it.copy(isFirstStartup = true) }
            } else {
                _uiState.update {
                    it.copy(username = userData.username, channelId = userData.channelId)
                }
            }
            val dailyLimit = withContext(Dispatchers.IO) {
                dataStoreRepository.getDailyLimit()
            }
            dailyLimit.collect { limit ->
                _uiState.update { it.copy(dailyLimit = limit) }
            }
            val selectedFont = withContext(Dispatchers.IO) {
                dataStoreRepository.getSelectedFont()
            }
            selectedFont.collect { font ->
                _uiState.update { it.copy(selectedFont = font) }
            }
            val platformData = withContext(Dispatchers.IO) {
                platformDataRepository.getAllPlatformData()
            }
            _uiState.update { it.copy(platformData = platformData) }
        }
    }

    fun getPlatformData() {
        viewModelScope.launch {
            try {
                val platformData = platformDataRepository.getAllPlatformData()
                _uiState.update { it.copy(platformData = platformData) }
            } catch (e: Exception) {
                Log.e("SettingScreenViewModel", "Error getting platform data", e)
            }
        }
    }

    fun createUserData() {
        viewModelScope.launch {
            val newUserData = UserDataTable(
                username = _uiState.value.username,
                channelId = _uiState.value.channelId,
                addedTimerList = listOf()
            )
            try {
                userDataRepository.insertUserData(newUserData)
                dataStoreRepository.saveDailyLimit(_uiState.value.dailyLimit)
            } catch (e: Exception) {
                Log.e("MainScreenViewModel", "Error inserting data", e)
            }
        }
    }

    fun updateUserData() {
        viewModelScope.launch {
            try {
                val currentUserData = userDataRepository.getCurrentUser() ?: return@launch
                val updatedUserData = currentUserData.copy(
                    username = _uiState.value.username,
                    channelId = _uiState.value.channelId
                )
                userDataRepository.updateUserData(updatedUserData)
                dataStoreRepository.saveDailyLimit(_uiState.value.dailyLimit)
            } catch (e: Exception) {
                Log.e("MainScreenViewModel", "Error updating data", e)
            }
        }
    }

    fun onFontsExpandChange() {
        _uiState.update { it.copy(fontsExpanded = !it.fontsExpanded) }
    }

    fun onPlatformExpandChange() {
        _uiState.update { it.copy(platformExpanded = !it.platformExpanded) }
    }

    fun updateDailyLimit(limit: Int) {
        _uiState.update { it.copy(dailyLimit = limit) }
    }

    fun addPlatformData() {
        val platformData = PlatformDataTable(
            platformName = platforms[_uiState.value.selectedPlatform],
            channelName = _uiState.value.channelName,
            platformKey = _uiState.value.platformKey
        )
        viewModelScope.launch {
            try {
                platformDataRepository.insertPlatformData(platformData)
                val updatedList = platformDataRepository.getAllPlatformData()
                _uiState.update { it.copy(platformData = updatedList, isShowPlatformDialog = false) }
            } catch (e: Exception) {
                Log.e("SettingScreenViewModel", "Error adding platform data", e)
            }
        }
    }

    fun deletePlatformData(platformData: PlatformDataTable) {
        viewModelScope.launch {
            try {
                platformDataRepository.deletePlatformData(platformData)
                val updatedList = platformDataRepository.getAllPlatformData()
                _uiState.update { it.copy(platformData = updatedList) }
            } catch (e: Exception) {
                Log.e("SettingScreenViewModel", "Error deleting platform data", e)
            }
        }
    }

    fun onPlatformDialogDismiss() {
        _uiState.update { it.copy(isShowPlatformDialog = false) }
    }

    fun onSelectedPlatformChange(index: Int) {
        _uiState.update { it.copy(selectedPlatform = index, platformExpanded = false) }
    }

    fun onChannelNameChange(name: String) {
        _uiState.update { it.copy(channelName = name) }
    }

    fun onKeyChange(key: String) {
        _uiState.update { it.copy(platformKey = key) }
    }

    fun onUsernameChange(name: String) {
        _uiState.update { it.copy(username = name) }
    }

    fun onSelectedFontChange(index: Int) {
        _uiState.update { it.copy(selectedFont = index, fontsExpanded = false) }
        viewModelScope.launch {
            try {
                dataStoreRepository.saveSelectedFont(index)
            } catch (e: Exception) {
                Log.e("SettingScreenViewModel", "Error saving selected font", e)
            }
        }
    }

    fun onDismissFontsMenu() {
        _uiState.update { it.copy(fontsExpanded = false) }
    }

    fun onDismissPlatformMenu() {
        _uiState.update { it.copy(platformExpanded = false) }
    }

    fun onShowPlatformDialog() {
        _uiState.update { it.copy(isShowPlatformDialog = true) }
    }
}

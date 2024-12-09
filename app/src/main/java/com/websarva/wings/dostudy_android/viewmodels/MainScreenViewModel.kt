package com.websarva.wings.dostudy_android.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainScreenViewModel : ViewModel() {
    var isSettingsDialogOpen by mutableStateOf(false)
    var username by mutableStateOf("")
    var channelId by mutableStateOf("")


}
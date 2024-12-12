package com.websarva.wings.dostudy_android.Room

import androidx.compose.ui.text.input.TextFieldValue
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserDataTable(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
    var username: String,
    var channelId: String,
    var addedTimerList: List<Int>
)

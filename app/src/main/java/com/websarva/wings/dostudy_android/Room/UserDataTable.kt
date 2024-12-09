package com.websarva.wings.dostudy_android.Room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserDataTable(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var username: String,
    var channelId: String
)

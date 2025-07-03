package com.websarva.wings.dostudy_android.model.Room.PlatformData

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PlatformDataTable(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val platformName: String,
    val channelName: String,
    val platformKey: String
)
package com.websarva.wings.dostudy_android.Room

import androidx.room.Entity
import androidx.room.PrimaryKey

//ユーザーデータテーブル
@Entity
data class UserDataTable(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
    var username: String,
    var channelId: String,
    var addedTimerList: List<Int>
)

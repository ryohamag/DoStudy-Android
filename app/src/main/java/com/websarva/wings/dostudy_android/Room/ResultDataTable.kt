package com.websarva.wings.dostudy_android.Room

import androidx.room.Entity
import androidx.room.PrimaryKey

//タイマーデータテーブル
@Entity
data class ResultDataTable(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var date: String,
    var setTimer: Int?,
    var studyTime: Int,
    var status: Boolean
)

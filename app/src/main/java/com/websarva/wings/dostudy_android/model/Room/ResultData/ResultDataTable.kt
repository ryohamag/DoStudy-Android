package com.websarva.wings.dostudy_android.model.Room.ResultData

import androidx.room.Entity
import androidx.room.PrimaryKey

//タイマーデータテーブル
@Entity
data class ResultDataTable(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var date: String,
    var setTimer: String?,
    var studyTime: String,
    var status: Boolean,
    var studyTitle: String
)

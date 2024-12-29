package com.websarva.wings.dostudy_android.functions

//String型の時間を秒数に変換する関数
fun timeToSeconds(time: String): Int {
    val parts = time.split(":")

    val hours = parts[0].toInt()
    val minutes = parts[1].toInt()
    val seconds = parts[2].toInt()

    return hours * 3600 + minutes * 60 + seconds
}

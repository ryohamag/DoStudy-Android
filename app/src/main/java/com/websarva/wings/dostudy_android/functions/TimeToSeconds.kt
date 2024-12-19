package com.websarva.wings.dostudy_android.functions

fun timeToSeconds(time: String): Int {
    val parts = time.split(":")
    if (parts.size != 3) {
        throw IllegalArgumentException("Time format must be HH:mm:ss")
    }

    val hours = parts[0].toIntOrNull() ?: throw IllegalArgumentException("Invalid hours value")
    val minutes = parts[1].toIntOrNull() ?: throw IllegalArgumentException("Invalid minutes value")
    val seconds = parts[2].toIntOrNull() ?: throw IllegalArgumentException("Invalid seconds value")

    return hours * 3600 + minutes * 60 + seconds
}

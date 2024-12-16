package com.websarva.wings.dostudy_android.functions

import android.util.Log
import androidx.compose.runtime.Composable
import com.websarva.wings.dostudy_android.viewmodels.MainScreenViewModel
import kotlin.math.abs

fun orientSensor(
    orientation: FloatArray?,
    vm: MainScreenViewModel
) {
    if (orientation != null) {
        Log.d("MainScreen", "orientation[1]: ${abs(Math.toDegrees(orientation[1].toDouble()))}")
        if (abs(Math.toDegrees(orientation[1].toDouble())) > 30.0) {
            vm.isShowFailedDialog = true
            httpRequest(channelId = vm.channelId, username = vm.username, status = false, vm.seconds, vm = vm)
            vm.reset()
            Log.d("MainScreen", vm.seconds.toString())
        }
    } else {
        Log.d("MainScreen", "null")
    }
}
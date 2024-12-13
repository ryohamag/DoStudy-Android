package com.websarva.wings.dostudy_android.functions

import android.util.Log
import androidx.compose.runtime.Composable
import com.websarva.wings.dostudy_android.viewmodels.MainScreenViewModel

fun orientSensor(
    orientation: FloatArray?,
    vm: MainScreenViewModel
) {
    if (orientation != null) {
        if (orientation[1] > Math.toRadians(45.0)) {
            vm.orientationSensor.stop()
            vm.reset()
            vm.isShowFailedDialog = true
            Log.d("MainScreen", "stop")
        }
    } else {
        Log.d("MainScreen", "null")
    }
}
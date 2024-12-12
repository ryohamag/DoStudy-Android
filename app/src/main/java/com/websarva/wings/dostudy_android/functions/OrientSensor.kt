package com.websarva.wings.dostudy_android.functions

import android.util.Log
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.websarva.wings.dostudy_android.OrientationSensor
import com.websarva.wings.dostudy_android.viewmodels.MainScreenViewModel

@Composable
fun orientSensor(
    orientation: FloatArray?,
    vm: MainScreenViewModel
) {
    if (orientation != null) {
        if (orientation[1] > Math.toRadians(45.0)) {
            vm.orientationSensor.stop()
            vm.isStudyStarted = false
            Log.d("MainScreen", "stop")
        }
    } else {
        Log.d("MainScreen", "null")
    }
}
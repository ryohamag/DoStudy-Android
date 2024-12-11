package com.websarva.wings.dostudy_android.functions

import android.util.Log
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

@Composable
fun orientSensor() {
    val context = LocalContext.current
    val orientationSensor = remember { OrientationSensor(context) }
    LaunchedEffect(orientationSensor) { // LaunchedEffect で start() を呼び出す
        orientationSensor.start()
    }

    val orientation by orientationSensor.orientation.observeAsState()

}
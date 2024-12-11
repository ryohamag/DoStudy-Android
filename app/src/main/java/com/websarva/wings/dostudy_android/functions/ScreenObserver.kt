package com.websarva.wings.dostudy_android.functions

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.websarva.wings.dostudy_android.viewmodels.MainScreenViewModel

fun screenObserver(lifecycleOwner: LifecycleOwner, vm: MainScreenViewModel) {
    // EventObserverを設定し、ライフサイクルのイベントを監視・イベントを設定
    val observer = LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_PAUSE ) {
            vm.isStudyStarted = false
            Log.d("MainScreen", "Lifecycle.Event.ON_PAUSE")
        }
        if (event == Lifecycle.Event.ON_DESTROY) {
            vm.isStudyStarted = false
            Log.d("MainScreen", "Lifecycle.Event.ON_DESTROY")
        }
    }
    // 作成した監視・イベントの設定をライフサイクルオーナーに紐付ける
    lifecycleOwner.lifecycle.addObserver(observer)
}

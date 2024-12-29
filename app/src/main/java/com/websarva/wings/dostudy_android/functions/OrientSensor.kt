package com.websarva.wings.dostudy_android.functions

import android.util.Log
import com.websarva.wings.dostudy_android.viewmodels.MainScreenViewModel
import kotlin.math.abs

//画面の向きを検知する
fun orientSensor(
    orientation: FloatArray?,
    vm: MainScreenViewModel
) {
    if (orientation != null) {
        Log.d("MainScreen", "orientation[1]: ${abs(Math.toDegrees(orientation[1].toDouble()))}")
        if (abs(Math.toDegrees(orientation[1].toDouble())) > 30.0) { //30度以上傾いたら停止
            vm.addResultData(false)
            vm.isShowFailedDialog = true
            httpRequest(channelId = vm.channelId, username = vm.username, status = false, vm.seconds, vm = vm)
            vm.reset()
        }
    }
}
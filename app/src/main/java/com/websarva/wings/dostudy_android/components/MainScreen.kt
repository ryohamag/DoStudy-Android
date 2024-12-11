package com.websarva.wings.dostudy_android.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.websarva.wings.dostudy_android.OrientationSensor
import com.websarva.wings.dostudy_android.R
import com.websarva.wings.dostudy_android.functions.httpRequest
import com.websarva.wings.dostudy_android.viewmodels.MainScreenViewModel

@Composable
fun MainScreen(
    innerPadding : PaddingValues,
    vm: MainScreenViewModel
) {
    LaunchedEffect(key1 = vm.isStudyStarted) { // isStudyStarted が true になったら実行
        vm.orientationSensor.start()
    }

    val orientation by vm.orientationSensor.orientation.observeAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    if (vm.isStudyStarted && orientation != null) {
        if (orientation?.get(1)!! > Math.toRadians(45.0)) {
            vm.orientationSensor.stop()
            Log.d("MainScreen", "stop")
        } else if (orientation?.get(1) == null) {
            Log.d("MainScreen", "null")
        }
    } else {
        Log.d("MainScreen", "null")
    }

    DisposableEffect(lifecycleOwner) {
        // EventObserverを設定し、ライフサイクルのイベントを監視・イベントを設定
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE ) {
                Log.d("MainScreen", "Lifecycle.Event.ON_PAUSE")
            }
            if (event == Lifecycle.Event.ON_DESTROY) {
                Log.d("MainScreen", "Lifecycle.Event.ON_DESTROY")
            }
        }

        // 作成した監視・イベントの設定をライフサイクルオーナーに紐付ける
        lifecycleOwner.lifecycle.addObserver(observer)

        // ライフサイクルオーナーが変化した時、または、Composeが破棄される時に、紐付けたイベントを解除する
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    if (vm.isSettingsDialogOpen || vm.isFirstStartup) {
        SettingsDialog(
            onDismissRequest = {
                vm.isSettingsDialogOpen = false
                vm.isFirstStartup = false },
            username = vm.username,
            onUsernameChange = { vm.username = it },
            channelId = vm.channelId,
            onChannelIdChange = { vm.channelId = it },
            createUserData = { vm.createUserData() },
            updateUserData = { vm.updateUserData() },
            isFirstStartup = vm.isFirstStartup
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxWidth()
    ) {
        Text(
            text = "00:00:00",
            modifier = Modifier.padding(64.dp),
            fontSize = 64.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                httpRequest(username = vm.username, channelId = vm.channelId)
                vm.isStudyStarted = true},
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "start",
                modifier = Modifier.padding(64.dp),
                fontSize = 64.sp
                )
        }

        Spacer(modifier = Modifier.height(5.dp))

        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Button(
                onClick = { vm.isSettingsDialogOpen = true },
                modifier = Modifier
                    .weight(5f)
                    .padding(16.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    painter = rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.baseline_settings_24)),
                    contentDescription = "設定ボタン"
                )
            }

            if(vm.isShowTimerSetMenu) {
                TimerSetMenu(
                    expanded = vm.isShowTimerSetMenu,
                    onDismissRequest = { vm.isShowTimerSetMenu = false }
                )
            }

            IconToggleButton(
                checked = vm.isTimerMode,
                onCheckedChange = {
                    vm.isTimerMode = it
                    vm.isShowTimerSetMenu = it},
                modifier = Modifier
                    .weight(2f)
                    .padding(16.dp)
                    .scale(2f)
            ) {
                Icon(
                    painter = rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.baseline_timer_24)),
                    contentDescription = "タイマー設定ボタン"
                )
            }
        }
    }
}
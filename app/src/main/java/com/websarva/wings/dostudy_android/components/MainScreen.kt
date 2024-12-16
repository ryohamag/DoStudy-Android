package com.websarva.wings.dostudy_android.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.websarva.wings.dostudy_android.R
import com.websarva.wings.dostudy_android.functions.httpRequest
import com.websarva.wings.dostudy_android.functions.orientSensor
import com.websarva.wings.dostudy_android.functions.screenObserver
import com.websarva.wings.dostudy_android.viewmodels.MainScreenViewModel
import kotlinx.coroutines.delay

@Composable
fun MainScreen(
    navController: NavController,
    innerPadding : PaddingValues,
    vm: MainScreenViewModel
) {
    val orientation by vm.orientationSensor.orientation.observeAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(key1 = vm.isStudyStarted) { // isStudyStarted が true になったら実行
        if (vm.isStudyStarted) {
            vm.orientationSensor.start()
            while (vm.isStudyStarted) {
                delay(1000)
                if(!vm.isTimerMode || vm.selectedTimer == null) {
                    vm.seconds++
                } else {
                    vm.selectedTimer = vm.selectedTimer!! - 1
                    if(vm.selectedTimer == 0) {
                        vm.reset()
                        vm.isShowSuccessDialog = true
                        break
                    }
                }
            }
        } else {
            vm.orientationSensor.stop()
        }
    }

    LaunchedEffect(key1 = vm.isStudyStarted) {
        while (vm.isStudyStarted) {
            delay(5000)
            orientSensor(orientation, vm)
        }
    }

    if (vm.isStudyStarted) {
        DisposableEffect(lifecycleOwner) {
            screenObserver(lifecycleOwner, vm)
            onDispose {
                vm.orientationSensor.stop()
            }
        }
    }

    if(vm.isShowFailedDialog) {
        FailedDialog(
            onDismissRequest = {
                vm.isShowFailedDialog = false
                vm.responseMessage = "" },
            responseMessage = vm.responseMessage
        )
    }

    if(vm.isShowSuccessDialog) {
        SuccessDialog(
            onDismissRequest = { vm.isShowSuccessDialog = false }
        )
    }

    if (vm.isSettingsDialogOpen || vm.isFirstStartup) {
        SettingsDialog(
            onDismissRequest = {
                vm.isSettingsDialogOpen = false
                vm.isFirstStartup = false
            },
            username = vm.username,
            onUsernameChange = { vm.username = it },
            channelId = vm.channelId,
            onChannelIdChange = { vm.channelId = it },
            createUserData = { vm.createUserData() },
            updateUserData = { vm.updateUserData() },
            isFirstStartup = vm.isFirstStartup
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White,
                        Color.Cyan,
                        Color.Blue
                    )
                )
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        ) {
            val hour = if(!vm.isTimerMode) vm.seconds / 3600 else vm.selectedTimer?.div(3600) ?: 0
            val minute = if(!vm.isTimerMode) (vm.seconds % 3600) / 60 else (vm.selectedTimer?.rem(3600) ?: 0) / 60
            val second = if(!vm.isTimerMode) vm.seconds % 60 else vm.selectedTimer?.rem(60) ?: 0

            Text(
                text = "${hour.toString().padStart(2, '0')}h" +
                        "${minute.toString().padStart(2, '0')}m" +
                        "${second.toString().padStart(2, '0')}s",
                modifier = Modifier.padding(64.dp),
                fontSize = 52.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    vm.isStudyStarted = !vm.isStudyStarted
                },
                modifier = Modifier.padding(16.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonColors(
                    contentColor = Color.Cyan,
                    containerColor = Color.White,
                    disabledContentColor = Color.Gray,
                    disabledContainerColor = Color.Gray
                )
            ) {
                Text(
                    text = if (vm.isStudyStarted) "stop" else "start",
                    modifier = Modifier.padding(64.dp),
                    fontSize = 64.sp
                )
            }

            Spacer(modifier = Modifier.height(5.dp))

            if (!vm.isStudyStarted) {
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

                    IconToggleButton(
                        checked = vm.selectedTimer != null,
                        onCheckedChange = {
                            vm.isTimerMode = true
                            if(vm.isTimerMode) navController.navigate("TimerSetting")
                        },
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
    }
}

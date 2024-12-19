package com.websarva.wings.dostudy_android.components

import android.media.MediaPlayer
import android.content.Context
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.websarva.wings.dostudy_android.R
import com.websarva.wings.dostudy_android.functions.httpRequest
import com.websarva.wings.dostudy_android.functions.orientSensor
import com.websarva.wings.dostudy_android.viewmodels.MainScreenViewModel
import kotlinx.coroutines.delay

//メイン画面
@Composable
fun MainScreen(
    navController: NavController,
    innerPadding : PaddingValues,
    context: Context,
    vm: MainScreenViewModel
) {
    //スマホの角度を監視
    val orientation by vm.orientationSensor.orientation.observeAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    // メディアプレイヤーを用意
    val mediaPlayer = remember { MediaPlayer.create(context, R.raw.effect_sound) }
    // 音声再生関数
    val playSound: () -> Unit = {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.seekTo(0) // 再生中なら最初に戻す
        }
        mediaPlayer.start() // 音を再生
    }

    Log.d("resultDataList", vm.resultDataList.toString())

    Log.d("MainScreen", vm.seconds.toString())

    //isStudyStarted が true になったら実行
    LaunchedEffect(key1 = vm.isStudyStarted) {
        if (vm.isStudyStarted) {
            vm.orientationSensor.start()
            while (vm.isStudyStarted) {
                delay(1000) //1秒ごとにカウント
                if(!vm.isTimerMode || vm.selectedTimer == null) {
                    vm.seconds++ //カウントアップモード
                } else {
                    vm.selectedTimer = vm.selectedTimer!! - 1 //カウントダウン(タイマー)モード
                    vm.seconds++
                    if(vm.selectedTimer == 0) { //カウントが0になったら成功ダイアログを表示
                        vm.isShowSuccessDialog = true
                        break
                    }
                }
            }
        } else {
            delay(1000)
            vm.seconds = 0
            vm.orientationSensor.stop()
        }
    }

    LaunchedEffect(key1 = vm.isStudyStarted) {
        while (vm.isStudyStarted) {
            delay(5000) //5秒ごとに角度を計測
            orientSensor(orientation, vm)
        }
    }

    //失敗時のダイアログを表示
    if(vm.isShowFailedDialog) {
        FailedDialog(
            onDismissRequest = {
                vm.isShowFailedDialog = false
                vm.responseMessage = "" },
            responseMessage = vm.responseMessage
        )
    }

    //成功時のダイアログを表示
    if(vm.isShowSuccessDialog) {
        SuccessDialog(
            onDismissRequest = {
                vm.isShowSuccessDialog = false
                vm.responseMessage = "" },
            responseMessage = vm.responseMessage
        )
        playSound()
    }

    //成功時のhttpリクエストを送信
    LaunchedEffect(key1 = vm.isShowSuccessDialog) {
        if (vm.isShowSuccessDialog) {
            vm.addResultData(true)
            httpRequest(channelId = vm.channelId, username = vm.username, status = true, vm.seconds, vm = vm)
            vm.reset()
        }
    }

    //ユーザ設定ダイアログを表示
    if (vm.isSettingsDialogOpen || vm.isFirstStartup) { //設定ボタンor初回起動時
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
                brush = Brush.verticalGradient( //背景のグラデーション
                    colors = listOf(
                        Color(0xffcce6ff),
                        Color(0xff66b3ff),
                        Color(0xff0080ff),
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
            //表示用のタイマー
            val hour = if(!vm.isTimerMode) vm.seconds / 3600 else vm.selectedTimer?.div(3600) ?: 0
            val minute = if(!vm.isTimerMode) (vm.seconds % 3600) / 60 else (vm.selectedTimer?.rem(3600) ?: 0) / 60
            val second = if(!vm.isTimerMode) vm.seconds % 60 else vm.selectedTimer?.rem(60) ?: 0

            //タイマー
            Text(
                text = "${hour.toString().padStart(2, '0')}h" +
                        "${minute.toString().padStart(2, '0')}m" +
                        "${second.toString().padStart(2, '0')}s",
                modifier = Modifier.padding(64.dp),
                fontSize = 52.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            //スタート/ストップボタン
            Button(
                onClick = {
                    if(vm.isStudyStarted && !vm.isTimerMode) {
                        vm.isShowSuccessDialog = true
                    }
                    vm.isStudyStarted = !vm.isStudyStarted
                          },
                modifier = Modifier.padding(16.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonColors( //ボタンの色の設定
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    disabledContentColor = Color.Gray,
                    disabledContainerColor = Color.Gray
                ),
            ) {
                Text(
                    text = if (vm.isStudyStarted) "stop" else "start",
                    modifier = Modifier.padding(64.dp),
                    fontSize = 64.sp,
                    )

            }

            Spacer(modifier = Modifier.height(5.dp))

            //ユーザ/タイマー設定ボタン
            if (!vm.isStudyStarted) { //勉強中は非表示
                Row(
                    modifier = Modifier.padding(16.dp)
                ) {
                    IconButton(
                        onClick = { vm.isSettingsDialogOpen = true },
                        modifier = Modifier
                            .padding(32.dp)
                            .scale(3f)
                    ) {
                        Icon(
                            painter = rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.baseline_settings_24)),
                            contentDescription = "設定ボタン",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    //タイマー設定ボタン
                    IconToggleButton(
                        checked = vm.selectedTimer != null,
                        onCheckedChange = {
                            vm.isTimerMode = true
                            if(vm.isTimerMode) navController.navigate("TimerSetting")
                        },
                        modifier = Modifier
                            .padding(32.dp)
                            .scale(3f)
                    ) {
                        Icon(
                            painter = rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.baseline_timer_24)),
                            contentDescription = "タイマー設定ボタン"
                        )
                    }
                }

                Button(
                    onClick = { navController.navigate("Result") },
                    colors = ButtonColors( //ボタンの色の設定
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        disabledContentColor = Color.Gray,
                        disabledContainerColor = Color.Gray
                    ),
                ) {
                    Text(
                        text = "記録",
                        modifier = Modifier.padding(start = 32.dp, end = 32.dp),
                    )
                }
            }
        }
    }
    // 画面が破棄されたときに音楽プレイヤーをリリース
    DisposableEffect(lifecycleOwner) {
        onDispose {
            mediaPlayer.release() // リソースの解放
        }
    }
}

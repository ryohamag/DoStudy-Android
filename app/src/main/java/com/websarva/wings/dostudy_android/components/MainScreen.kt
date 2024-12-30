package com.websarva.wings.dostudy_android.components

import android.media.MediaPlayer
import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.IconToggleButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
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
        vm.isShowAdScreen = true
        FailedDialog(
            onDismissRequest = {
                vm.isShowFailedDialog = false
                vm.responseMessage = "" },
            responseMessage = vm.responseMessage,
            vm = vm
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

    //勉強タイトルダイアログを表示
    if(vm.isShowStudyTitleDialog) {
        SetTitleDialog (
            onDismissRequest = { vm.isShowStudyTitleDialog = false },
            studyTitle = vm.studyTitle,
            onStudyTitleChange = { vm.studyTitle = it },
            onConfirmButtonClick = {
                if(!vm.isStudyStarted) {
                    vm.isStudyStarted = true
                    httpRequest(channelId = vm.channelId, username = vm.username, status = true, vm.seconds, vm = vm, mode = "start")
                }
                vm.isShowStudyTitleDialog = false
            }
        )
    }

    //タイマーストップダイアログを表示
    if(vm.isShowStopTimerDialog) {
        StopTimerDialog(
            onDismissRequest = { vm.isShowStopTimerDialog = false },
            onStopRequest = {
                vm.isStudyStarted = false
                vm.isShowStopTimerDialog = false
                vm.isShowSuccessDialog = true
            }
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
                modifier = Modifier.padding(start = 64.dp, end = 64.dp, top = 64.dp, bottom = 8.dp),
                fontSize = 52.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            if(vm.isStudyStarted &&!vm.isTimerMode || !vm.isStudyStarted) {
                Row(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
                ) {
                    //スタート/ストップボタン
                    Button(
                        onClick = {
                            if(!vm.isStudyStarted) {
                                vm.isShowStudyTitleDialog = true
                            }
                            if(vm.isStudyStarted && !vm.isTimerMode) {
                                vm.isShowStopTimerDialog = true
                            }
                        },
                        modifier = Modifier
                            .weight(5f)
                            .padding(16.dp),
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
                            modifier = Modifier.padding(32.dp),
                            fontSize = 48.sp,
                        )
                    }

                    Column(
                        modifier = Modifier
                            .weight(2f)
                            .padding(16.dp)
                    ) {
                        IconButton(
                            onClick = { vm.isSettingsDialogOpen = true },
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
                                .scale(2.5f)
                        ) {
                            Icon(
                                painter = rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.baseline_settings_24)),
                                contentDescription = "設定ボタン",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }

                        IconButton(
                            onClick = { navController.navigate("Result") },
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
                                .scale(2.5f)
                        ) {
                            Icon(
                                painter = rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.baseline_history_24)),
                                contentDescription = "履歴ボタン",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

            //ユーザ/タイマー設定ボタン
            if (!vm.isStudyStarted) { //勉強中は非表示
                //タイマー追加ダイアログ
                if(vm.isShowTimerAddingDialog) {
                    TimerAddingDialog(
                        onDismissRequest = { vm.isShowTimerAddingDialog = false },
                        vm = vm
                    )
                }

                //何も選択されていなければタイマーモードをオフにしておく
                if(vm.selectedTimer == null) {
                    vm.isTimerMode = false
                }

                //現在の設定できるタイマーのリストを取得
                val currentTimerList by vm.timerList.collectAsState() // collectAsState で監視
                val userTimerList = (currentTimerList + vm.addedTimerList).distinct()
                val sortedList = userTimerList.sorted()

                Box(
                    modifier = Modifier
                        .padding(start = 32.dp, end = 32.dp, top = 8.dp, bottom = 48.dp)
                        .background(
                            color = Color.White.copy(alpha = 0.5f), // 白の半透明
                            shape = RoundedCornerShape(8.dp)
                        )
                        .fillMaxWidth()
                ) {
                    //タイマーのリストに対して1つ1つ要素をカードで表示
                    LazyColumn {
                        items(sortedList) { timer ->
                            TimerCard(
                                seconds = timer,
                                vm = vm
                            )
                        }

                        item { //追加するボタン
                            Card(
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(100.dp))
                                    .clickable { vm.isShowTimerAddingDialog = true }, // Card 全体がクリック可能
                                colors = CardDefaults.cardColors(Color(0xffcce6ff))
                            ) {
                                Box( // 中央寄せのため Box を使用
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp), // 余白を Box 内部に適用
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Add",
                                        modifier = Modifier
                                            .scale(2f), // アイコンのスケール調整
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }

                        }
                    }
                }
            }
        }

        AdBanner(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        )
    }
    // 画面が破棄されたときに音楽プレイヤーをリリース
    DisposableEffect(lifecycleOwner) {
        onDispose {
            mediaPlayer.release() // リソースの解放
        }
    }
}

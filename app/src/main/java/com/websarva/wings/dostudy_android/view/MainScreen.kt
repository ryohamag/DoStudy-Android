package com.websarva.wings.dostudy_android.view

import android.media.MediaPlayer
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.websarva.wings.dostudy_android.R
import com.websarva.wings.dostudy_android.functions.httpRequest
import com.websarva.wings.dostudy_android.functions.orientSensor
import com.websarva.wings.dostudy_android.util.DefaultTimerConstants
import com.websarva.wings.dostudy_android.viewmodel.MainViewModel
import kotlinx.coroutines.delay

//メイン画面
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    innerPadding : PaddingValues,
    context: Context,
    vm: MainViewModel
) {
    //スマホの角度を監視
    val orientation by vm.orientation.collectAsState()
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

    LaunchedEffect(Unit) {
        vm.getToDoList()
        vm.getPlatformData()
    }

    //isStudyStarted が true になったら実行
    LaunchedEffect(key1 = vm.isStudyStarted) {
        if (vm.isStudyStarted) {
            vm.startSensor()
            while (vm.isStudyStarted) {
                delay(1000) //1秒ごとにカウント
                if(!vm.isTimerMode || vm.selectedTimer.value == null) {
                    vm.incrementSeconds()
                } else {
                    //カウントダウン(タイマー)モード
                    vm.decrementSeconds()
                    vm.incrementSeconds()
                    if(vm.selectedTimer.value == 0) { //カウントが0になったら成功ダイアログを表示
                        vm.isShowSuccessDialog = true
                        break
                    }
                }
            }
        } else {
            delay(500)
            vm.reset()
            vm.stopSensor()
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
            httpRequest(
                platformDataList = vm.platformData.value, username = vm.username, status = true,
                seconds = vm.seconds.value, vm = vm
            )
            vm.reset()
        }
    }

    //勉強タイトルダイアログを表示
    if(vm.isShowStudyTitleDialog) {
        val resultDataList by vm.resultDataList.collectAsState()
        val todoList by vm.todoList.collectAsState()
        SetTitleDialog (
            onDismissRequest = { vm.isShowStudyTitleDialog = false },
            studyTitle = vm.studyTitle,
            onStudyTitleChange = { vm.studyTitle = it },
            onConfirmButtonClick = {
                if(vm.studyTitle.isEmpty()) {
                    Toast.makeText(context, "タイトルを入力してください", Toast.LENGTH_SHORT).show()
                    return@SetTitleDialog
                }
                if(!vm.isStudyStarted) {
                    vm.isStudyStarted = true
                    httpRequest(
                        platformDataList = vm.platformData.value, username = vm.username,
                        status = true, seconds = vm.seconds.value, vm = vm, mode = "start"
                    )
                }
                vm.isShowStudyTitleDialog = false
            },
            titleList = resultDataList.map { it.studyTitle },
            todoList = todoList,
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

//    val gradientColors = if (isSystemInDarkTheme()) {
//        listOf(Color(0xFF1a1a1a), Color(0xFF333333), Color(0xFF4d4d4d)) // ダークモード向け
//    } else {
//        listOf(Color(0xffcce6ff), Color(0xff66b3ff), Color(0xff0080ff)) // ライトモード向け
//    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { AdBanner(modifier = Modifier.fillMaxWidth()) },
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding1 ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
//                .padding(innerPadding)
                .padding(innerPadding1)
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
//            //広告バナー
//            AdBanner(modifier = Modifier.fillMaxWidth())

            val seconds by vm.seconds.collectAsState() //経過時間を監視
            val selectedTimer by vm.selectedTimer.collectAsState() //選択されたタイマーを監視

            //表示用のタイマー
            val hour = if (!vm.isTimerMode) seconds / 3600 else (selectedTimer ?: 0) / 3600
            val minute = if (!vm.isTimerMode) (seconds % 3600) / 60 else ((selectedTimer ?: 0) % 3600) / 60
            val second = if (!vm.isTimerMode) seconds % 60 else (selectedTimer ?: 0) % 60

            if(!vm.isStudyStarted || (vm.isStudyStarted && !vm.isTimerMode)) {
                //タイマー
                Text(
                    text = "${hour.toString().padStart(2, '0')}h" +
                            "${minute.toString().padStart(2, '0')}m" +
                            "${second.toString().padStart(2, '0')}s",
                    modifier = Modifier
                        .padding(start = 32.dp, end = 32.dp, top = 64.dp, bottom = 8.dp),
                    fontSize = 52.sp,
                    fontFamily = when (vm.selectedFont) {
                        0 -> FontFamily.Default
                        1 -> FontFamily(Font(R.font.noto_sans_jp))
                        2 -> FontFamily(Font(R.font.montserrat))
                        3 -> FontFamily(Font(R.font.open_sans))
                        4 -> FontFamily(Font(R.font.playfair_display))
                        5 -> FontFamily(Font(R.font.new_amsterdam))
                        else -> FontFamily.Default
                    },
                )
            }

            //円形タイマー
            if(vm.isStudyStarted && vm.isTimerMode) {
                val setTimer by vm.setTimer.collectAsState() //設定されたタイマーを監視

                CircularTimer(
                    hour = hour,
                    minute = minute,
                    second = second,
                    setTimer = setTimer!!,
                    selectedFont = vm.selectedFont,
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            if(vm.isStudyStarted &&!vm.isTimerMode || !vm.isStudyStarted) {
                Row(
                    modifier = Modifier.padding(start = 32.dp, end = 32.dp, top = 8.dp, bottom = 8.dp)
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
                            .padding(16.dp)
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primary, //ボタンの枠線の色
                                shape = RoundedCornerShape(8.dp)
                            ),
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

//                    Column(
//                        modifier = Modifier
//                            .weight(2f)
//                            .padding(16.dp)
//                    ) {
//                        IconButton(
//                            onClick = { navController.navigate("Settings") },
//                            modifier = Modifier
//                                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
//                                .scale(2.5f)
//                        ) {
//                            Icon(
//                                painter = rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.baseline_settings_24)),
//                                contentDescription = "設定ボタン",
//                                tint = MaterialTheme.colorScheme.onPrimaryContainer
//                            )
//                        }
//
//                        IconButton(
//                            onClick = {
//                                if(!vm.isStudyStarted) {
//                                    if(vm.resultDataList.value.isNotEmpty()) {
//                                        navController.navigate("Result")
//                                    } else {
//                                        Toast.makeText(context, "まだデータがありません", Toast.LENGTH_SHORT).show()
//                                    }
//                                }
//                            },
//                            modifier = Modifier
//                                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
//                                .scale(2.5f)
//                        ) {
//                            Icon(
//                                painter = rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.baseline_history_24)),
//                                contentDescription = "履歴ボタン",
//                                tint = MaterialTheme.colorScheme.onPrimaryContainer
//                            )
//                        }
//                    }
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

                val selectedTimer by vm.selectedTimer.collectAsState() //選択されたタイマーを監視
                //何も選択されていなければタイマーモードをオフにしておく
                if(selectedTimer == null) {
                    vm.isTimerMode = false
                }

                //現在の設定できるタイマーのリストを取得
                val addedTimerList by vm.addedTimerList.collectAsState() //追加されたタイマーを監視
                val userTimerList = (DefaultTimerConstants.defaultTimers + addedTimerList).distinct()
                val sortedList = userTimerList.sorted()

                Box(
                    modifier = Modifier
                        .padding(start = 32.dp, end = 32.dp, top = 10.dp, bottom = 90.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f), // 白の半透明
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.outlineVariant, // 枠線の色
                            shape = RoundedCornerShape(8.dp)
                        )
                        .fillMaxWidth()
                ) {
                    //タイマーのリストに対して1つ1つ要素をカードで表示
                    LazyColumn {
                        items(sortedList, key = { it }) { timer ->
                            TimerCard(
                                seconds = timer,
                                vm = vm,
                                modifier = Modifier
                                    .animateItem(
                                    fadeInSpec = tween(durationMillis = 300),
                                    fadeOutSpec = tween(durationMillis = 300),
                                    placementSpec = spring(
                                        dampingRatio = Spring.DampingRatioLowBouncy,
                                        stiffness = Spring.StiffnessLow
                                    )
                                )
                            )
                        }

                        item(key = "add_button") { //追加するボタン
                            Card(
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(100.dp))
                                    .clickable { vm.isShowTimerAddingDialog = true }, // Card 全体がクリック可能
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                )
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

        // 画面が破棄されたときに音楽プレイヤーをリリース
        DisposableEffect(lifecycleOwner) {
            onDispose {
                mediaPlayer.release() // リソースの解放
            }
        }
    }


}

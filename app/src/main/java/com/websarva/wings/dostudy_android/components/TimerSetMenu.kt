package com.websarva.wings.dostudy_android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.websarva.wings.dostudy_android.viewmodels.MainScreenViewModel

//タイマーをセットするメニュー
@Composable
fun TimerSetMenu(
    vm: MainScreenViewModel
) {
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

    Scaffold(
        floatingActionButton = { //追加するボタン
            FloatingActionButton(
                onClick = { vm.isShowTimerAddingDialog = true },
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color(0xff66b3ff)
                )
        ) {
            //タイマーのリストに対して1つ1つ要素をカードで表示
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                items(sortedList) { timer ->
                    TimerCard(
                        seconds = timer,
                        vm = vm
                    )
                }
            }
        }
    }
}
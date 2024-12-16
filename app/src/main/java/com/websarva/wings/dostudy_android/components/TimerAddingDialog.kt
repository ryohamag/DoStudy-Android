package com.websarva.wings.dostudy_android.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.websarva.wings.dostudy_android.R
import com.websarva.wings.dostudy_android.viewmodels.MainScreenViewModel

@Composable
fun TimerAddingDialog(
    onDismissRequest: () -> Unit,
    vm: MainScreenViewModel
) {
    // タイマー入力状態を管理する
    var timerState by remember { mutableStateOf("000000") } // "hhmmss"形式

    // 数字ボタンを押した時の処理
    fun updateTimer(input: String) {
        // 先頭が"0"でない場合は追加しない
        if (timerState.first() == '0') {
            if(input == "00") {
                if (timerState.getOrNull(1) != '0') {
                    // 先頭から2番目が"0"の場合の処理
                    timerState = (timerState + "0").takeLast(6) // 6桁の左詰めで更新
                    return
                } else {
                    // 先頭から2番目が"0"ではない場合の処理
                    // ここに別の処理を追加できます
                    timerState = (timerState + input).takeLast(6) // 6桁の左詰めで更新
                    return
                }
            }
            timerState = (timerState + input).takeLast(6) // 6桁の左詰めで更新
        }
    }

    // バックスペースボタンの処理
    fun removeLastDigit() {
        timerState = timerState.dropLast(1).padStart(6, '0') // 最後の桁を削除し、0で埋める
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("タイマーの追加") },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                // タイマー表示
                Text(
                    text = "${timerState.substring(0, 2)}h " +
                            "${timerState.substring(2, 4)}m " +
                            "${timerState.substring(4, 6)}s",
                    modifier = Modifier.padding(8.dp),
                    fontSize = 42.sp
                )

                // 数字入力ボタン
                listOf(
                    listOf("1", "2", "3"),
                    listOf("4", "5", "6"),
                    listOf("7", "8", "9"),
                    listOf("00", "0", "icon")
                ).forEach { row ->
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        row.forEach { item ->
                            when (item) {
                                "icon" -> OutlinedIconButton(
                                    onClick = { removeLastDigit() },
                                    modifier = Modifier.size(70.dp),
                                ) {
                                    Icon(
                                        painter = rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.baseline_backspace_24)),
                                        contentDescription = "backspace"
                                    )
                                }
                                "00" -> NumpadButton(item, textPadding = 0.dp) {
                                    updateTimer(item)
                                }
                                else -> NumpadButton(item) {
                                    updateTimer(item)
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                vm.addTimer(timerState)
                onDismissRequest()
            }) {
                Text("追加")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("キャンセル")
            }
        }
    )
}

@Composable
fun NumpadButton(
    text: String,
    textPadding: Dp = 4.dp,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = {onClick()},
        shape = CircleShape,
        modifier = Modifier
            .padding(2.dp)
            .size(70.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = Color.Black)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(textPadding),
            fontSize = 18.sp
        )
    }
}
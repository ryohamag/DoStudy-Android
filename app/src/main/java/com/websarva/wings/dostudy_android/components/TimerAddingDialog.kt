package com.websarva.wings.dostudy_android.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import com.websarva.wings.dostudy_android.viewmodels.MainScreenViewModel
import kotlin.text.matches

@Composable
fun TimerAddingDialog(
    onDismissRequest: () -> Unit,
    vm: MainScreenViewModel
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("タイマーの追加") },
        text = {
            OutlinedTextField(
                value = vm.inputTimer,
                onValueChange = { newText ->
                    // 入力値が数字と "h"、"m"、"s" のみ、かつ正しい形式であることを確認
                    if (newText.text.matches(Regex("^\\d{0,2}h\\d{0,2}m\\d{0,2}s$"))) {
                        vm.inputTimer = newText
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text("Time (00h00m00s)") }
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    // 入力値が数字と "h"、"m"、"s" のみ、かつ正しい形式であることを確認
                    if (vm.inputTimer.text.matches(Regex("^\\d{0,2}h\\d{0,2}m\\d{0,2}s$"))) {
                        val (hours, minutes, seconds) = vm.inputTimer.text.split("h", "m", "s")
                            .map { it.ifEmpty { "0" }.toInt() } // 空文字列の場合は 0 に変換
                        vm.addTimer(hours * 3600 + minutes * 60 + seconds)
                        vm.inputTimer = TextFieldValue("00h00m00s")
                        vm.isShowTimerAddingDialog = false
                    }
                }
            ) {
                Text("保存")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("キャンセル")
            }
        }
    )
}
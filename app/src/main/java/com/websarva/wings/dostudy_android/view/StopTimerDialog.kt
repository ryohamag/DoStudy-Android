package com.websarva.wings.dostudy_android.view

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

//タイマー停止ダイアログ
@Composable
fun StopTimerDialog(
    onDismissRequest: () -> Unit,
    onStopRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = { Text("タイマーを止めますか？") },
        confirmButton = {
            TextButton(
                onClick = { onStopRequest() }
            ) {
                Text("止める")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismissRequest() }
            ) {
                Text("止めない")
            }
        }
    )
}
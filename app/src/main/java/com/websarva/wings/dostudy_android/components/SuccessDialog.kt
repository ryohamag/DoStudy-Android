package com.websarva.wings.dostudy_android.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun SuccessDialog(
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        icon = { Icon(Icons.Default.Done, "完了") },
        onDismissRequest = {},
        title = { Text("お疲れ様でした！") },
        text = { Text("タイマーが完了しました！") },
        confirmButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text("OK")
            }
        },
    )
}
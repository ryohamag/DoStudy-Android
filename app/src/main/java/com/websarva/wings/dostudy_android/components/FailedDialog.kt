package com.websarva.wings.dostudy_android.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun FailedDialog(
    onDismissRequest: () -> Unit,
    responseMessage: String
) {
    AlertDialog(
        icon = { Icon(Icons.Default.Warning, "警告") },
        onDismissRequest = {},
        title = { Text("何やってるんですか！") },
        text = { if(responseMessage != "") Text(responseMessage) else Text("Loading...") },
        confirmButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text("ごめんなさい")
            }
        },
    )
}
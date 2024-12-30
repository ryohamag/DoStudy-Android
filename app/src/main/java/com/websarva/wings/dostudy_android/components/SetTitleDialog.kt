package com.websarva.wings.dostudy_android.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable

@Composable
fun SetTitleDialog(
    onDismissRequest: () -> Unit,
    studyTitle: String,
    onStudyTitleChange: (String) -> Unit,
    onConfirmButtonClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("タイトルの入力") },
        text = {
            TextField(
                value = studyTitle,
                onValueChange = onStudyTitleChange,
                label = { Text("勉強タイトル") }
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirmButtonClick
            ) {
                Text("勉強開始")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text("キャンセル")
            }
        },
    )
}
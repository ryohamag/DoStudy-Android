package com.websarva.wings.dostudy_android.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SetTitleDialog(
    onDismissRequest: () -> Unit,
    studyTitle: String,
    onStudyTitleChange: (String) -> Unit,
    onConfirmButtonClick: () -> Unit,
    titleList: List<String>
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("タイトルの入力") },
        text = {
            Column {
                TextField(
                    value = studyTitle,
                    onValueChange = onStudyTitleChange,
                    label = { Text("勉強タイトル") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "履歴",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                titleList.reversed().distinct().take(5).forEach {
                    TextButton(
                        onClick = { onStudyTitleChange(it) },
                    ) {
                        Text(text = it)
                    }
                }
            }
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
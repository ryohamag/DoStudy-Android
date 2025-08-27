package com.websarva.wings.dostudy_android.view

import android.widget.Toast
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

@Composable
fun AddToDoDialog(
    onDismiss: () -> Unit,
    addToDo: (String) -> Unit,
) {
    var title by remember { mutableStateOf("") }
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("ToDoの追加") },
        text = {
            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("タイトル") } // 入力フィールドのラベル
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank()) { // タイトルが空でない場合のみ追加
                        addToDo(title)
                        onDismiss() // ダイアログを閉じる
                    } else {
                        Toast.makeText(
                            context,
                            "タイトルを入力してください",
                            Toast.LENGTH_SHORT
                        ).show() // タイトルが空の場合のエラーメッセージ
                    }
                }
            ) {
                Text("追加") // 確認ボタンのテキスト
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismiss() } // キャンセルボタンの動作
            ) {
                Text("キャンセル") // キャンセルボタンのテキスト
            }
        }
    )
}
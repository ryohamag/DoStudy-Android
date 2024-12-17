package com.websarva.wings.dostudy_android.components

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

//ユーザ設定ダイアログ
@Composable
fun SettingsDialog(
    onDismissRequest: () -> Unit,
    username: String,
    onUsernameChange: (String) -> Unit,
    channelId: String,
    onChannelIdChange: (String) -> Unit,
    createUserData: () -> Unit,
    updateUserData: () -> Unit,
    isFirstStartup: Boolean
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "設定") },
        text = {
            Column {
                TextField(
                    value = username,
                    onValueChange = onUsernameChange,
                    label = { Text("ユーザー名") }
                )

                Spacer(Modifier.height(20.dp))

                TextField(
                    value = channelId,
                    onValueChange = onChannelIdChange,
                    label = { Text("チャンネルID") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number) //入力は数字のみ
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { //入力が正しいか確認
                    if(username.isEmpty()) {
                        Toast.makeText(context, "ユーザー名を入力してください", Toast.LENGTH_SHORT).show()
                        return@TextButton
                    }
                    if (channelId.isEmpty()) {
                        Toast.makeText(context, "チャンネルIDを入力してください", Toast.LENGTH_SHORT).show()
                        return@TextButton
                    }
                    if (channelId.length != 19) { // チャンネルIDが19桁かどうかをチェック
                        Toast.makeText(context, "チャンネルIDは19桁で入力してください", Toast.LENGTH_SHORT).show()
                        return@TextButton
                    }
                    if(isFirstStartup) {
                        createUserData() //初回起動時ならcreate
                    } else {
                        updateUserData() //それ以外ならupdate
                    }
                    onDismissRequest()
                }
            ) {
                Text("保存")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismissRequest() }
            ) {
                Text("キャンセル")
            }
        })
}
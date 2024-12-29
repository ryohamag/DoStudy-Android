@file:Suppress("DEPRECATION")

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
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.text.ClickableText
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString


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

                Spacer(Modifier.height(20.dp))

                // クリック可能なリンクテキストを追加
                val annotatedText = buildAnnotatedString {
                    append("チャンネルIDの調べ方はこちら")
                    addStyle(
                        style = SpanStyle(
                            color = Color.Blue, // リンクとして青色にする
                        ),
                        start = 0,
                        end = this.length
                    )
                    addStringAnnotation(
                        tag = "URL",
                        annotation = "https://support.discord.com/hc/en-us/articles/206346498-Where-can-I-find-my-User-Server-Message-ID#h_01HRSTXPS5FMK2A5SMVSX4JW4E", // リンク先URL
                        start = 0,
                        end = this.length
                    )
                }
                ClickableText(
                    text = annotatedText,
                    onClick = { offset ->
                        annotatedText.getStringAnnotations(tag = "URL", start = offset, end = offset)
                            .firstOrNull()?.let { annotation ->
                                // リンクをクリックした場合の処理
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item))
                                context.startActivity(intent)
                            }
                    }
                )

                Spacer(Modifier.height(10.dp))

                val botInvitationText = buildAnnotatedString {
                    append("Botの招待リンクはこちら")
                    addStyle(
                        style = SpanStyle(
                            color = Color.Blue, // リンクとして青色にする
                        ),
                        start = 0,
                        end = this.length
                    )
                    addStringAnnotation(
                        tag = "URL",
                        annotation = "https://discord.com/oauth2/authorize?client_id=1311225271361212436", // リンク先URL
                        start = 0,
                        end = this.length
                    )
                }
                ClickableText(
                    text = botInvitationText,
                    onClick = { offset ->
                        botInvitationText.getStringAnnotations(tag = "URL", start = offset, end = offset)
                            .firstOrNull()?.let { annotation ->
                                // リンクをクリックした場合の処理
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item))
                                context.startActivity(intent)
                            }
                    }
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
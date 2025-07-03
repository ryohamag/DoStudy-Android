package com.websarva.wings.dostudy_android.view

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.websarva.wings.dostudy_android.util.PlatformConstants.platforms

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPlatformDialog(
    onDismiss: () -> Unit,
    addPlatform: () -> Unit,
    selectedPlatform: Int,
    selectedPlatformChange: (Int) -> Unit,
    channelName: String,
    channelNameChange: (String) -> Unit,
    key: String,
    keyChange: (String) -> Unit,
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("プラットフォームの追加") },
        text = {
            var platformExpanded by remember { mutableStateOf(false) }

            Column {
                ExposedDropdownMenuBox(
                    expanded = platformExpanded,
                    onExpandedChange = {
                        platformExpanded = !platformExpanded
                    }
                ) {
                    TextField(
                        readOnly = true,
                        value = platforms[selectedPlatform],
                        onValueChange = { },
                        modifier = Modifier.menuAnchor(),
                        label = { Text("プラットフォーム") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = platformExpanded
                            )
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors()
                    )

                    ExposedDropdownMenu(
                        expanded = platformExpanded,
                        onDismissRequest = {
                            platformExpanded = false
                        }
                    ) {
                        platforms.forEachIndexed { index, platform ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedPlatformChange(index)
                                    platformExpanded = false
                                },
                                text = { Text(platform) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                TextField(
                    value = channelName,
                    onValueChange = channelNameChange,
                    label = {
                        when(selectedPlatform) {
                            0 -> Text("チャンネル名")
                            1 -> Text("トークルーム名")
                        }
                    },
                )

                Spacer(modifier = Modifier.height(20.dp))

                TextField(
                    value = key,
                    onValueChange = keyChange,
                    label = {
                        when(selectedPlatform) {
                            0 -> Text("チャンネルID")
                            1 -> Text("Key")
                        }
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                when(selectedPlatform) {
                    0 -> {
                        val uriHandler = LocalUriHandler.current
                        val annotatedText = buildAnnotatedString {
                            append("チャンネルIDの調べ方はこちら")
                            addStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.primary,
                                    textDecoration = TextDecoration.Underline
                                ),
                                start = 0,
                                end = this.length
                            )
                        }

                        Text(
                            text = annotatedText,
                            modifier = Modifier.clickable {
                                uriHandler.openUri("https://support.discord.com/hc/en-us/articles/206346498-Where-can-I-find-my-User-Server-Message-ID#h_01HRSTXPS5FMK2A5SMVSX4JW4E")
                            }
                        )
                    }
                    1 -> {
                        Text(
                            text = "Keyは、Botとのトークルームで「Keyが欲しい」と送信すると取得できます。",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                val uriHandler = LocalUriHandler.current

                Spacer(Modifier.height(10.dp))

                when(selectedPlatform) {
                    0 -> {
                        val botInvitationText = buildAnnotatedString {
                            append("Botの招待リンクはこちら")
                            addStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.primary,
                                    textDecoration = TextDecoration.Underline
                                ),
                                start = 0,
                                end = this.length
                            )
                        }

                        Text(
                            text = botInvitationText,
                            modifier = Modifier.clickable {
                                uriHandler.openUri("https://discord.com/oauth2/authorize?client_id=1311225271361212436")
                            }
                        )
                    }

                    1 -> {
                        val botInvitationText = buildAnnotatedString {
                            append("Botの友達追加はこちら")
                            addStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.primary,
                                    textDecoration = TextDecoration.Underline
                                ),
                                start = 0,
                                end = this.length
                            )
                        }

                        Text(
                            text = botInvitationText,
                            modifier = Modifier.clickable {
                                uriHandler.openUri("https://line.me/R/ti/p/%40807spakn")
                            }
                        )
                    }
                }

            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    when {
                        channelName.isBlank() && key.isBlank() -> {
                            val channelNameLabel = if (selectedPlatform == 0) "チャンネル名" else "トークルーム名"
                            val keyLabel = if (selectedPlatform == 0) "チャンネルID" else "Key"
                            Toast.makeText(context, "${channelNameLabel}と${keyLabel}を入力してください", Toast.LENGTH_SHORT).show()
                        }
                        channelName.isBlank() -> {
                            val channelNameLabel = if (selectedPlatform == 0) "チャンネル名" else "トークルーム名"
                            Toast.makeText(context, "${channelNameLabel}を入力してください", Toast.LENGTH_SHORT).show()
                        }
                        key.isBlank() -> {
                            val keyLabel = if (selectedPlatform == 0) "チャンネルID" else "Key"
                            Toast.makeText(context, "${keyLabel}を入力してください", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            addPlatform()
                            onDismiss()
                        }
                    }
                }
            ) {
                Text("追加")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismiss() }
            ) {
                Text("キャンセル")
            }
        }
    )
}

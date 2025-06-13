package com.websarva.wings.dostudy_android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    username: String,
    onUsernameChange: (String) -> Unit,
    channelId: String,
    onChannelIdChange: (String) -> Unit,
    createUserData: () -> Unit,
    updateUserData: () -> Unit,
    isFirstStartup: Boolean,
    selectedFont: Int,
    selectedFontChange: (Int) -> Unit,
    fonts: List<String>
) {
    val context = LocalContext.current

    Scaffold(
        contentWindowInsets = WindowInsets.systemBars,
        topBar = {
            CenterAlignedTopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                title = {
                    Text("設定")
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "戻る",
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "メニュー",
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        // グラデーション背景の色を設定
        val gradientColors = if (isSystemInDarkTheme()) {
            listOf(Color(0xFF1a1a1a), Color(0xFF333333), Color(0xFF4d4d4d)) // ダークモード向け
        } else {
            listOf(Color(0xffcce6ff), Color(0xff66b3ff), Color(0xff0080ff)) // ライトモード向け
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(gradientColors)
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                Text(
                    text = "ユーザー設定",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(10.dp))

                TextField(
                    value = username,
                    onValueChange = onUsernameChange,
                    label = { Text("ユーザー名") },
                )

                Spacer(Modifier.height(20.dp))

                Text(
                    text = "Discord設定",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(10.dp))

                TextField(
                    value = channelId,
                    onValueChange = onChannelIdChange,
                    label = { Text("チャンネルID") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(Modifier.height(20.dp))

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

                Spacer(Modifier.height(10.dp))

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

                Spacer(Modifier.height(20.dp))

                Text(
                    text = "デザイン設定",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(10.dp))

                var expanded by remember { mutableStateOf(false) }

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    }
                ) {
                    TextField(
                        readOnly = true,
                        value = fonts[selectedFont],
                        onValueChange = { },
                        modifier = Modifier.menuAnchor(),
                        label = { Text("フォント") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = expanded
                            )
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        }
                    ) {
                        fonts.forEachIndexed { index, font ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedFontChange(index)
                                    expanded = false
                                },
                                text = { Text(font) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun SettingScreenPreview() {
    SettingScreen(
        username = "テストユーザー",
        onUsernameChange = {},
        channelId = "123456789",
        onChannelIdChange = {},
        createUserData = {},
        updateUserData = {},
        isFirstStartup = true,
        selectedFont = 0,
        selectedFontChange = {},
        fonts = listOf("デフォルト", "明朝体", "ゴシック体")
    )
}
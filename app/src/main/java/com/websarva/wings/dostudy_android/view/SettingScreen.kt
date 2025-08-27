package com.websarva.wings.dostudy_android.view

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.websarva.wings.dostudy_android.R
import com.websarva.wings.dostudy_android.viewmodel.MainViewModel
import com.websarva.wings.dostudy_android.util.FontConstants.fonts
import androidx.compose.foundation.lazy.items

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    navController: NavController,
    vm: MainViewModel,
) {
    LaunchedEffect(Unit) {
        vm.getPlatformData()
    }

    if (vm.isShowPlatformDialog) {
        AddPlatformDialog(
            onDismiss = { vm.isShowPlatformDialog = false },
            addPlatform = {
                vm.addPlatformData()
                vm.isShowPlatformDialog = false
            },
            selectedPlatform = vm.selectedPlatform,
            selectedPlatformChange = { vm.selectedPlatform = it },
            channelName = vm.channelName,
            channelNameChange = { vm.channelName = it },
            key = vm.platformKey,
            keyChange = { vm.platformKey = it },
        )
    }

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
                    IconButton(onClick = { navController.navigate("Home") }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "戻る",
                        )
                    }
                },
                actions = {
                    val dailyLimit = vm.dailyLimit.collectAsState().value
                    IconButton(
                        onClick = {
                            if (vm.dailyLimit.value > 0) { // 保存時のバリデーション
                                if (vm.isFirstStartup) vm.createUserData() else vm.updateUserData()
                                navController.navigate("Home")
                            }
                        },
                        enabled = dailyLimit > 0 // 無効な値の場合はボタンを無効化
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_save_alt_24),
                            contentDescription = "保存",
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            Text(
                text = "ユーザー名",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(10.dp))

            TextField(
                value = vm.username,
                onValueChange = { vm.username = it },
                label = { Text("ユーザー名") },
            )

            Spacer(Modifier.height(20.dp))

            Text(
                text = "デザイン",
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
                    value = fonts[vm.selectedFont],
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
                                vm.selectedFont = index
                                expanded = false
                            },
                            text = { Text(font) }
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = "スクリーンタイム",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(10.dp))

            val dailyLimit = vm.dailyLimit.collectAsState().value

            TextField(
                value = if (dailyLimit == 0) "" else dailyLimit.toString(),
                onValueChange = { value ->
                    if (value.isEmpty()) {
                        vm.updateDailyLimitTemporary(0) // 空文字の場合は0を設定
                    } else {
                        value.toIntOrNull()?.let { vm.updateDailyLimitTemporary(it) }
                    }
                },
                label = { Text("一日の制限時間（分）") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = dailyLimit == 0, // 0の場合はエラー表示
                supportingText = if (dailyLimit == 0) {
                    { Text("制限時間を入力してください", color = MaterialTheme.colorScheme.error) }
                } else null
            )

            Spacer(Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "プラットフォーム",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )

                TextButton(
                    onClick = {
                        vm.isShowPlatformDialog = true
                    },
                ) {
                    Text(
                        text = "追加",
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            val platformData = vm.platformData.collectAsState().value

            if (platformData.isEmpty()) {
                Text(
                    text = "プラットフォームがありません",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .padding(bottom = 90.dp)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outlineVariant,
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    items(platformData, key = { it.id }) { platform ->
                        PlatformCard(
                            platform = platform,
                            deletePlatformData = { platform -> vm.deletePlatformData(platform) },
                            modifier = Modifier.animateItem(
                                fadeInSpec = tween(durationMillis = 300),
                                fadeOutSpec = tween(durationMillis = 300),
                                placementSpec = spring(
                                    dampingRatio = Spring.DampingRatioLowBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}
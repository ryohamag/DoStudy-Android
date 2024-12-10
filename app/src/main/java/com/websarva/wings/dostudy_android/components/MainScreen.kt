package com.websarva.wings.dostudy_android.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.websarva.wings.dostudy_android.R
import com.websarva.wings.dostudy_android.functions.httpRequest
import com.websarva.wings.dostudy_android.viewmodels.MainScreenViewModel

@Composable
fun MainScreen(
    innerPadding : PaddingValues,
    vm: MainScreenViewModel
) {
    if (vm.isSettingsDialogOpen || vm.isFirstStartup) {
        SettingsDialog(
            onDismissRequest = {
                vm.isSettingsDialogOpen = false
                vm.isFirstStartup = false },
            username = vm.username,
            onUsernameChange = { vm.username = it },
            channelId = vm.channelId,
            onChannelIdChange = { vm.channelId = it },
            createUserData = { vm.createUserData() },
            updateUserData = { vm.updateUserData() },
            isFirstStartup = vm.isFirstStartup
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxWidth()
    ) {
        Text(
            text = "00:00:00",
            modifier = Modifier.padding(64.dp),
            fontSize = 64.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { httpRequest(username = vm.username, channelId = vm.channelId) },
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "start",
                modifier = Modifier.padding(64.dp),
                fontSize = 64.sp
                )
        }

        Spacer(modifier = Modifier.height(5.dp))

        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Button(
                onClick = { vm.isSettingsDialogOpen = true },
                modifier = Modifier
                    .weight(5f)
                    .padding(16.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    painter = rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.baseline_settings_24)),
                    contentDescription = "設定ボタン"
                )
            }

            if(vm.isShowTimerSetMenu) {
                TimerSetMenu(
                    expanded = vm.isShowTimerSetMenu,
                    onDismissRequest = { vm.isShowTimerSetMenu = false }
                )
            }

            IconToggleButton(
                checked = vm.isTimerMode,
                onCheckedChange = {
                    vm.isTimerMode = it
                    vm.isShowTimerSetMenu = it},
                modifier = Modifier
                    .weight(2f)
                    .padding(16.dp)
                    .scale(2f)
            ) {
                Icon(
                    painter = rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.baseline_timer_24)),
                    contentDescription = "タイマー設定ボタン"
                )
            }
        }
    }
}
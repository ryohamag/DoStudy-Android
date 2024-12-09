package com.websarva.wings.dostudy_android.components

import androidx.activity.result.launch
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.websarva.wings.dostudy_android.Room.UserRoomDataBase
import com.websarva.wings.dostudy_android.functions.httpRequest
import com.websarva.wings.dostudy_android.viewmodels.MainScreenViewModel

@Composable
fun MainScreen(
    innerPadding : PaddingValues
) {
    val context = LocalContext.current
    val vm = remember { MainScreenViewModel() }
    val db = UserRoomDataBase.getDatabase(context)
    val userDao = db.userDataDao()

    LaunchedEffect(Unit) {
        val userData = userDao.getCurrentUser()
        if (userData == null) {
            vm.username = ""
            vm.channelId = ""
        } else {
            vm.username = userData.username
            vm.channelId = userData.channelId
        }
    }

    if (vm.isSettingsDialogOpen) {
        SettingsDialog(
            onDismissRequest = { vm.isSettingsDialogOpen = false },
            username = vm.username,
            onUsernameChange = { vm.username = it },
            channelId = vm.channelId,
            onChannelIdChange = { vm.channelId = it }
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(innerPadding).fillMaxWidth()
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
                    imageVector = Icons.Default.Settings,
                    contentDescription = "設定ボタン"
                    )
            }

            var checked by remember { mutableStateOf(false) }

            Switch(
                checked = checked,
                onCheckedChange = { checked = it },
                modifier = Modifier
                    .weight(4f)
                    .padding(16.dp)
                    .scale(1.5f)
            )
        }
    }
}
package com.websarva.wings.dostudy_android.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.websarva.wings.dostudy_android.viewmodel.MainViewModel
import androidx.compose.runtime.getValue

//タイマー一覧用のカード
@Composable
fun TimerCard(
    seconds: Int,
    vm: MainViewModel,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        val selectedTimer by vm.selectedTimer.collectAsState() // selectedTimer の状態を監視
        val isChecked = selectedTimer == seconds

        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            val hour = seconds / 3600
            val minute = (seconds % 3600) / 60
            val second = seconds % 60

            Text(
                text = "${hour.toString().padStart(2, '0')}:" +
                        "${minute.toString().padStart(2, '0')}:" +
                        second.toString().padStart(2, '0'),
                modifier = Modifier
                    .padding(8.dp)
                    .weight(3f),
                fontSize = 36.sp
            )

            //オンオフのトグル
            Switch(
                checked = isChecked,
                onCheckedChange = {
                    if (it) {
                        vm.setTimer(seconds)
                        vm.isTimerMode = true
                    } else {
                        vm.resetTimer()
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )

            //デフォルトのタイマーはゴミ箱ボタンを表示しない
            if(seconds == 1800 || seconds == 3600 || seconds == 7200 || seconds == 10800) {
                Spacer(modifier = Modifier.weight(1f))
            } else {
                IconButton(
                    onClick = {
                        vm.deleteTimer(seconds)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete"
                    )
                }
            }
        }
    }
}
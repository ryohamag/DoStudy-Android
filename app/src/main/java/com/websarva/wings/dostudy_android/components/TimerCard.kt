package com.websarva.wings.dostudy_android.components

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
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.websarva.wings.dostudy_android.viewmodels.MainScreenViewModel

@Composable
fun TimerCard(
    seconds: Int,
    vm: MainScreenViewModel
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {

        val isChecked = vm.selectedTimer == seconds
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

            Switch(
                checked = isChecked,
                onCheckedChange = {
                    if (it) {
                        vm.selectedTimer = seconds // トグルがオンになったら selectedTimerId を更新
                    } else {
                        vm.selectedTimer = null // トグルがオフになったら selectedTimerId を null に設定
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )

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
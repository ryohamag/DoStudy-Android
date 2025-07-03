package com.websarva.wings.dostudy_android.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.websarva.wings.dostudy_android.model.Room.PlatformData.PlatformDataTable

@Composable
fun PlatformCard(
    platform: PlatformDataTable,
    deletePlatformData: (PlatformDataTable) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = platform.platformName,
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f),
                textAlign = TextAlign.Start
            )
            Text(
                text = platform.channelName,
                modifier = Modifier
                    .padding(16.dp)
                    .weight(2f),
                textAlign = TextAlign.Center
            )
            IconButton(
                onClick = {
                    deletePlatformData(platform)
                },
                modifier = Modifier
                    .padding(start = 20.dp)
                    .weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete"
                )
            }
        }
    }
}
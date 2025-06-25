package com.websarva.wings.dostudy_android.view

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.websarva.wings.dostudy_android.R

@Composable
fun BottomBar(navController: NavController) {
    BottomAppBar(
        actions = {
            IconButton(
                onClick = { navController.navigate("Home") },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "ホーム",
                    modifier = Modifier.size(32.dp)
                )
            }
            IconButton(
                onClick = { navController.navigate("ToDoList") },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_task_alt_24),
                    contentDescription = "ToDoリスト",
                    modifier = Modifier.size(32.dp)
                )
            }
            IconButton(
                onClick = { navController.navigate("Result") },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.outline_monitoring_24),
                    contentDescription = "モニタリング",
                    modifier = Modifier.size(32.dp)
                )
            }
            IconButton(
                onClick = { navController.navigate("Settings") },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "設定",
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    )
}
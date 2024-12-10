package com.websarva.wings.dostudy_android.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun TimerSetMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit
) {
    Box() {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest
        ) {
            DropdownMenuItem(
                text = { Text("30 minutes") },
                onClick = {
                    onDismissRequest()
                }
            )
            DropdownMenuItem(
                text = { Text("1 hour") },
                onClick = {
                    onDismissRequest()
                }
            )
            DropdownMenuItem(
                text = { Text("2 hours") },
                onClick = {
                    onDismissRequest()
                }
            )
            DropdownMenuItem(
                text = { Text("3 hours") },
                onClick = {
                    onDismissRequest()
                }
            )
        }
    }
}
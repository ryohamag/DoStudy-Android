package com.websarva.wings.dostudy_android.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(
    innerPadding : PaddingValues
) {
    Column(
        modifier = Modifier.padding(innerPadding)
    ) {
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "start")
        }
    }
}
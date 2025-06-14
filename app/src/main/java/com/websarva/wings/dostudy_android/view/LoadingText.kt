package com.websarva.wings.dostudy_android.view

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.delay

//gptAPIのレスポンス待ちの際に表示するローディングテキスト
@Composable
fun LoadingText() {
    val count = remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            count.intValue = (count.intValue + 1) % 4
            delay(500)
        }
    }
    Text("Loading" + ".".repeat(count.intValue))
}
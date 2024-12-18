package com.websarva.wings.dostudy_android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.websarva.wings.dostudy_android.viewmodels.MainScreenViewModel

@Composable
fun ResultScreen(
    innerPadding: PaddingValues,
    vm: MainScreenViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(0xff66b3ff)
            )
    ) {
        //タイマーのリストに対して1つ1つ要素をカードで表示
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            items(vm.resultDataList) { result ->
                ResultCard(result)
            }
        }
    }
}
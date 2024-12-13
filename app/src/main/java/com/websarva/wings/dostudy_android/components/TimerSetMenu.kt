package com.websarva.wings.dostudy_android.components

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.websarva.wings.dostudy_android.viewmodels.MainScreenViewModel

@Composable
fun TimerSetMenu(
    vm: MainScreenViewModel
) {
    if(vm.isShowTimerAddingDialog) {
        TimerAddingDialog(
            onDismissRequest = { vm.isShowTimerAddingDialog = false },
            vm = vm
        )
    }

    Log.d("TimerSetMenu", vm.addedTimerList.toString())

    val currentTimerList by vm.timerList.collectAsState() // collectAsState で監視
    val userTimerList = (currentTimerList + vm.addedTimerList).distinct()
    val sortedList = userTimerList.sorted()


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { vm.isShowTimerAddingDialog = true },
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            items(sortedList) { timer ->
                TimerCard(
                    seconds = timer,
                    vm = vm
                )
            }
        }
    }
}
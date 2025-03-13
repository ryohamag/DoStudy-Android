package com.websarva.wings.dostudy_android.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.websarva.wings.dostudy_android.viewmodels.MainScreenViewModel

//失敗時のダイアログ
@Composable
fun FailedDialog(
    onDismissRequest: () -> Unit,
    responseMessage: String,
    vm: MainScreenViewModel
) {
    AdScreen(vm)
    AlertDialog(
        icon = { Icon(Icons.Default.Warning, "警告") },
        onDismissRequest = {}, //必ず「ごめんなさい」を押させる
        title = { Text("何やってるんですか！") }, //勉強してください！
        text = { if(responseMessage != "") Text(responseMessage) else LoadingText() }, //
        confirmButton = {
            TextButton(
                onClick = { if(!vm.isShowAdScreen) onDismissRequest() }
            ) {
                Text("ごめんなさい")
            }
        },
    )
}
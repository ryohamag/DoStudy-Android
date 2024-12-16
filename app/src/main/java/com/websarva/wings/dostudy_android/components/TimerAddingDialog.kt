package com.websarva.wings.dostudy_android.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.websarva.wings.dostudy_android.viewmodels.MainScreenViewModel

@Composable
fun TimerAddingDialog(
    onDismissRequest: () -> Unit,
    vm: MainScreenViewModel
) {
    val hour = remember { mutableIntStateOf(0) }
    val minute = remember { mutableIntStateOf(0) }
    val second = remember { mutableIntStateOf(0) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("タイマーの追加") },
        text = {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                OutlinedTextField(
                    value = hour.intValue.toString(),
                    onValueChange = {
                        hour.intValue = it.toInt()
                    },
                    textStyle = LocalTextStyle.current.copy(color = Color.Black),
                    modifier = Modifier.weight(1.5f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Text(
                    text = "h",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                OutlinedTextField(
                    value = minute.intValue.toString(),
                    onValueChange = {
                        minute.intValue = it.toInt()
                    },
                    textStyle = LocalTextStyle.current.copy(color = Color.Black),
                    modifier = Modifier.weight(1.5f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Text(
                    text = "m",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                OutlinedTextField(
                    value = second.intValue.toString(),
                    onValueChange = {
                        second.intValue = it.toInt()
                    },
                    textStyle = LocalTextStyle.current.copy(color = Color.Black),
                    modifier = Modifier.weight(1.5f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Text(
                    text = "s",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        },
        confirmButton = {

        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("キャンセル")
            }
        }
    )


}
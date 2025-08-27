package com.websarva.wings.dostudy_android.view

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.websarva.wings.dostudy_android.model.Room.ToDoData.ToDoDataTable
import androidx.compose.foundation.lazy.items

@Composable
fun SetTitleDialog(
    onDismissRequest: () -> Unit,
    studyTitle: String,
    onStudyTitleChange: (String) -> Unit,
    onConfirmButtonClick: () -> Unit,
    titleList: List<String>,
    todoList: List<ToDoDataTable>
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("タイトルの入力") },
        text = {
            Column(
                modifier = Modifier
                    .height(400.dp)
                    .fillMaxWidth()
            ) {
                TextField(
                    value = studyTitle,
                    onValueChange = onStudyTitleChange,
                    label = { Text("作業タイトル") }
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    item {
                        Text(
                            text = "履歴",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    items(titleList.reversed().distinct().take(5)) { title ->
                        TextButton(
                            onClick = { onStudyTitleChange(title) },
                        ) {
                            Log.d("SetTitleDialog", "Title: $title")
                            Text(text = title)
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    item {
                        Text(
                            text = "ToDo",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    items(todoList) { todo ->
                        TextButton(
                            onClick = { onStudyTitleChange(todo.title) },
                        ) {
                            Log.d("SetTitleDialog", "ToDo: $todo")
                            Text(text = todo.title)
                        }
                    }
                }
            }


        },
        confirmButton = {
            TextButton(
                onClick = onConfirmButtonClick
            ) {
                Text("作業開始")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text("キャンセル")
            }
        },
    )
}
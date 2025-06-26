package com.websarva.wings.dostudy_android.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.websarva.wings.dostudy_android.R
import com.websarva.wings.dostudy_android.viewmodel.MainViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoScreen(
    navController: NavController,
    vm: MainViewModel,
    showAddToDoDialog: () -> Unit,
    deleteToDo: (String) -> Unit
) {
    if (vm.isShowAddToDoDialog) {
        AddToDoDialog(
            onDismiss = { vm.isShowAddToDoDialog = false },
            addToDo = { title -> vm.addToDo(title) }
        )
    }

    LaunchedEffect(Unit) {
        vm.getToDoList()
    }

    var todoList = vm.todoList.collectAsState()

    Scaffold(
        modifier = Modifier
            .padding(bottom = 90.dp),
        topBar = {
            CenterAlignedTopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                title = {
                    Text("ToDo")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("Home") }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "戻る",
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        /*todo*/
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_swap_vert_24),
                            contentDescription = "並び替え",
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddToDoDialog() },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "追加",
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            if (todoList.value.isEmpty()) {
                // ToDoがない場合のメッセージを表示
                item {
                    Text(
                        text = "ToDoがまだ追加されていません",
                        modifier = Modifier
                            .padding(32.dp)
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                // ToDoリストがある場合は通常通り表示
                items(todoList.value) { title ->
                    Card(
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = title,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth()
                                    .weight(4f),
                                style = MaterialTheme.typography.headlineMedium,
                                textAlign = TextAlign.Center
                            )

                            IconButton(
                                onClick = { deleteToDo(title) },
                                modifier = Modifier
                                    .weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "削除ボタン",
                                    modifier = Modifier
                                        .size(30.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
package com.websarva.wings.dostudy_android.view

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
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
import com.websarva.wings.dostudy_android.model.Room.ToDoData.ToDoDataTable

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ToDoScreen(
    navController: NavController,
    vm: MainViewModel,
    showAddToDoDialog: () -> Unit,
    deleteToDo: (ToDoDataTable) -> Unit,
    innerPadding: PaddingValues
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
    val selectedToDos = vm.selectedToDos.collectAsState()

    Scaffold(
//        modifier = Modifier
//            .padding(bottom = 90.dp),
        topBar = {
            CenterAlignedTopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                title = {
                    Text(
                        if (vm.isSwapMode) {
                            "並び替えモード"
                        } else {
                            "ToDo"
                        }
                    )
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
                        vm.isSwapMode = !vm.isSwapMode
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_swap_vert_24),
                            contentDescription = "並び替え",
                            tint = if (vm.isSwapMode) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            }
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if (!vm.isSwapMode) {
                FloatingActionButton(
                    onClick = { showAddToDoDialog() },
                    modifier = Modifier.padding(bottom = 90.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "追加",
                    )
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(top = 90.dp, bottom = 80.dp)
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
                items(todoList.value, key = { it.id }) { toDo ->
                    val isSelected = selectedToDos.value.contains(toDo)

                    Card(
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .animateItem(
                                fadeInSpec = tween(durationMillis = 300),
                                fadeOutSpec = tween(durationMillis = 300),
                                placementSpec = spring(
                                    dampingRatio = Spring.DampingRatioLowBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            ),
                        colors = CardDefaults.cardColors(
                            containerColor = when {
                                isSelected -> MaterialTheme.colorScheme.secondary
                                vm.isSwapMode -> MaterialTheme.colorScheme.surfaceVariant
                                else -> MaterialTheme.colorScheme.primaryContainer
                            },
                            contentColor = when {
                                isSelected -> MaterialTheme.colorScheme.onSecondary
                                vm.isSwapMode -> MaterialTheme.colorScheme.onSurfaceVariant
                                else -> MaterialTheme.colorScheme.onPrimaryContainer
                            }
                        ),
                        onClick = { if (vm.isSwapMode) vm.selectToDo(toDo) }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = toDo.title,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth()
                                    .weight(4f),
                                style = MaterialTheme.typography.headlineMedium,
                                textAlign = TextAlign.Center
                            )

                            if (!vm.isSwapMode) {
                                IconButton(
                                    onClick = { deleteToDo(toDo) },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "削除ボタン",
                                        modifier = Modifier.size(30.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
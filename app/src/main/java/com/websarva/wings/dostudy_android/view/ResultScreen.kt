package com.websarva.wings.dostudy_android.view

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.websarva.wings.dostudy_android.viewmodel.MainViewModel
import androidx.compose.runtime.getValue
import androidx.navigation.NavController

//記録画面
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    vm: MainViewModel,
    navController: NavController
) {
    Scaffold(
        modifier = Modifier.padding(bottom = 90.dp),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("全ての履歴") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("Monitor") }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "戻る"
                        )
                    }
                },
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
            )
        }
    ) { innerPadding ->
        val resultDataTable by vm.resultDataList.collectAsState()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(bottom = 0.dp)
        ) {
            item { Row {
                Text(
                    text = "グラフ",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 24.sp,
                )

                OutlinedButton(
                    onClick = { vm.isShowChart = !vm.isShowChart },
                    modifier = Modifier.align(Alignment.CenterVertically),
                ) {
                    Text(
                        text = if (vm.isShowChart) "非表示" else "表示",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            } }

            if (vm.isShowChart) {
                item {
                    //グラフの表示
                    LineChart(
                        resultDataTable = resultDataTable,
                        modifier = Modifier
                            .height(300.dp)
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 16.dp),
                    )
                }
                //区切り線
                item { HorizontalDivider() }
            }

            item { Text(
                text = "全ての履歴",
                modifier = Modifier.padding(16.dp),
                fontSize = 24.sp,
            ) }

            //履歴のカードを表示
            items(vm.resultDataList.value.reversed().take(30)) { result ->
                ResultCard(result)
            }
        }
    }
}
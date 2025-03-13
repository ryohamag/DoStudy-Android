package com.websarva.wings.dostudy_android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.websarva.wings.dostudy_android.viewmodels.MainScreenViewModel

//記録画面
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
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
                        resultDataTable = vm.resultDataList,
                        modifier = Modifier
                            .height(300.dp)
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 16.dp),
                    )
                }
                //区切り線
                item { HorizontalDivider(color = Color.Black) }
            }

            item { Text(
                text = "履歴",
                modifier = Modifier.padding(16.dp),
                fontSize = 24.sp,
            ) }

            //履歴のカードを表示
            items(vm.resultDataList.reversed().take(30)) { result ->
                ResultCard(result)
            }
        }
    }
}
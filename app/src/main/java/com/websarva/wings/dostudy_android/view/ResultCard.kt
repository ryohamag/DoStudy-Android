package com.websarva.wings.dostudy_android.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.websarva.wings.dostudy_android.R
import com.websarva.wings.dostudy_android.model.Room.ResultData.ResultDataTable

@Composable
fun ResultCard(
    resultDataTable: ResultDataTable
) {
    //履歴のカード
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
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 8.dp, bottom = 0.dp)
            ) {
                //日付
                Text(
                    text = resultDataTable.date,
                    modifier = Modifier.padding(8.dp),
                    fontSize = 24.sp,
                )

                Spacer(modifier = Modifier.width(16.dp))

                //タイトル
                Text(
                    text = resultDataTable.studyTitle,
                    modifier = Modifier.padding(8.dp),
                    fontSize = 18.sp,
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 16.dp, top = 0.dp, end = 8.dp, bottom = 16.dp),
            ) {
                //タイマーのアイコン
                Icon(
                    painter = rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.baseline_timer_24)),
                    contentDescription = "タイマー",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .scale(1f),
                )

                Spacer(modifier = Modifier.width(8.dp))

                //設定されたタイマー
                Text(
                    text = if(resultDataTable.setTimer == null) "未設定" else resultDataTable.setTimer!!,
                    modifier = Modifier.padding(8.dp),
                    fontSize = 18.sp,
                )

                Spacer(modifier = Modifier.width(16.dp))

                //勉強時間のアイコン
                Icon(
                    painter = rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.person_edit_24px)),
                    contentDescription = "勉強時間",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .scale(1f),
                )

                Spacer(modifier = Modifier.width(8.dp))

                //勉強時間
                Text(
                    text = resultDataTable.studyTime,
                    modifier = Modifier.padding(8.dp),
                    fontSize = 18.sp,
                )

                Spacer(modifier = Modifier.width(8.dp))

                //勉強結果
                Text(
                    text = if(resultDataTable.status) "Success" else "Failed",
                    modifier = Modifier.padding(8.dp),
                    color = if(resultDataTable.status) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.error
                    },
                    fontSize = 18.sp,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}
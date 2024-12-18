package com.websarva.wings.dostudy_android.components

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.websarva.wings.dostudy_android.R
import com.websarva.wings.dostudy_android.Room.ResultDataTable

@Composable
fun ResultCard(
    resultDataTable: ResultDataTable
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        colors = if(resultDataTable.status) CardDefaults.cardColors(Color(0xffcce6ff)) else CardDefaults.cardColors(MaterialTheme.colorScheme.errorContainer)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 8.dp, bottom = 0.dp)
            ) {
                Text(
                    text = resultDataTable.date,
                    modifier = Modifier.padding(8.dp),
                    color = Color.Black,
                    fontSize = 24.sp,
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = if(resultDataTable.status) "Success!" else "Failed...",
                    modifier = Modifier.padding(8.dp),
                    color = if(resultDataTable.status) Color.Black else Color.Red,
                    fontSize = 24.sp,
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 16.dp, top = 0.dp, end = 8.dp, bottom = 16.dp),
            ) {
                Icon(
                    painter = rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.baseline_timer_24)),
                    contentDescription = "タイマー",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .scale(1f),
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = if(resultDataTable.setTimer == null) "未設定" else resultDataTable.setTimer!!,
                    modifier = Modifier.padding(8.dp),
                    color = Color.Black,
                    fontSize = 18.sp,
                )

                Spacer(modifier = Modifier.width(16.dp))

                Icon(
                    painter = rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.person_edit_24px)),
                    contentDescription = "勉強時間",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .scale(1f),
                    tint = Color.Black
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = resultDataTable.studyTime,
                    modifier = Modifier.padding(8.dp),
                    color = if(resultDataTable.status)Color.Black else Color.Red,
                    fontSize = 18.sp,
                )
            }
        }
    }
}
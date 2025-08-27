package com.websarva.wings.dostudy_android.view

import androidx.compose.foundation.Canvas
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*
import android.graphics.Paint
import com.websarva.wings.dostudy_android.model.Room.ResultData.ResultDataTable
import com.websarva.wings.dostudy_android.functions.timeToSeconds

@Composable
fun LineChart(
    resultDataTable: List<ResultDataTable>,
    modifier: Modifier = Modifier
) {
    val dataTable = resultDataTable

    if (dataTable.isEmpty()) {
        Card(
            modifier = modifier,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "データがありません",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        return
    }

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "作業時間の推移",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp) // 横軸ラベル用に高さを少し増加
            ) {
                if (dataTable.size >= 2) {
                    drawLineChart(dataTable)
                }
            }
        }
    }
}

private fun DrawScope.drawLineChart(dataTable: List<ResultDataTable>) {
    val maxValue = dataTable.maxOfOrNull { timeToSeconds(it.studyTime) } ?: 0
    val minValue = dataTable.minOfOrNull { timeToSeconds(it.studyTime) } ?: 0

    if (maxValue == minValue) return

    val leftPadding = 80f // 縦軸ラベル用のスペース
    val rightPadding = 40f
    val topPadding = 40f
    val bottomPadding = 40f // 横軸ラベル削除により減少
    val chartWidth = size.width - leftPadding - rightPadding
    val chartHeight = size.height - topPadding - bottomPadding

    // 縦軸の目盛り値を計算（データに合わせて最適化）
    val yAxisValues = calculateOptimalYAxisValues(minValue, maxValue)

    // グリッド線を描画
    val gridColor = Color.LightGray.copy(alpha = 0.5f)

    // 水平グリッド線（縦軸の目盛りに合わせて）
    yAxisValues.forEach { value ->
        val normalizedValue = (value - minValue).toFloat() / (maxValue - minValue)
        val y = topPadding + chartHeight - normalizedValue * chartHeight
        drawLine(
            color = gridColor,
            start = Offset(leftPadding, y),
            end = Offset(size.width - rightPadding, y),
            strokeWidth = 1f
        )
    }

    // データポイントの座標を計算
    val points = dataTable.mapIndexed { index, data ->
        val x = leftPadding + (chartWidth / (dataTable.size - 1)) * index
        val studyTimeSeconds = timeToSeconds(data.studyTime)
        val normalizedValue = (studyTimeSeconds - minValue).toFloat() / (maxValue - minValue)
        val y = topPadding + chartHeight - normalizedValue * chartHeight
        Offset(x, y)
    }

    // 折れ線を描画
    val path = Path().apply {
        moveTo(points[0].x, points[0].y)
        for (i in 1 until points.size) {
            lineTo(points[i].x, points[i].y)
        }
    }

    drawPath(
        path = path,
        color = Color(0xFF2196F3),
        style = Stroke(width = 3f)
    )

    // データポイントを描画
    points.forEachIndexed { index, point ->
        val pointColor = if (dataTable[index].status) {
            Color(0xFF4CAF50)
        } else {
            Color(0xFFF44336)
        }

        drawCircle(
            color = Color.White,
            center = point,
            radius = 8f
        )

        drawCircle(
            color = pointColor,
            center = point,
            radius = 6f
        )
    }

    // 縦軸のラベル（分単位で表示）
    val yAxisTextPaint = Paint().apply {
        color = Color.Gray.toArgb()
        textSize = 24f
        textAlign = Paint.Align.RIGHT
    }

    yAxisValues.forEach { value ->
        val normalizedValue = (value - minValue).toFloat() / (maxValue - minValue)
        val y = topPadding + chartHeight - normalizedValue * chartHeight
        val minutes = value / 60
        val labelText = "${minutes}分"

        drawContext.canvas.nativeCanvas.drawText(
            labelText,
            leftPadding - 10f,
            y + 8f,
            yAxisTextPaint
        )
    }
}

// データに合わせて最適な縦軸の目盛り値を計算する関数
private fun calculateOptimalYAxisValues(minValue: Int, maxValue: Int): List<Int> {
    val range = maxValue - minValue
    val minMinutes = minValue / 60
    val maxMinutes = maxValue / 60

    // 適切な間隔を決定
    val interval = when {
        range <= 600 -> 5 * 60 // 5分間隔
        range <= 1800 -> 10 * 60 // 10分間隔
        range <= 3600 -> 15 * 60 // 15分間隔
        range <= 7200 -> 30 * 60 // 30分間隔
        range <= 14400 -> 60 * 60 // 1時間間隔
        else -> 120 * 60 // 2時間間隔
    }

    val intervalMinutes = interval / 60
    val startMinutes = (minMinutes / intervalMinutes) * intervalMinutes
    val endMinutes = ((maxMinutes / intervalMinutes) + 1) * intervalMinutes

    val values = mutableListOf<Int>()
    var currentMinutes = startMinutes
    while (currentMinutes <= endMinutes) {
        values.add(currentMinutes * 60)
        currentMinutes += intervalMinutes
    }

    return values
}



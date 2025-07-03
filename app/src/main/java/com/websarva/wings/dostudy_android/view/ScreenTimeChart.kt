package com.websarva.wings.dostudy_android.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
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
import android.graphics.Paint
import java.util.concurrent.TimeUnit

@Composable
fun ScreenTimeChart(
    screenTimeData: List<Pair<String, Long>>,
    modifier: Modifier = Modifier
) {
    if (screenTimeData.isEmpty()) {
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
                    text = "使用状況へのアクセスが許可されていません。",
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
                text = "スクリーンタイム（直近7日間）",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                drawScreenTimeChart(screenTimeData)
            }
        }
    }
}

private fun DrawScope.drawScreenTimeChart(screenTimeData: List<Pair<String, Long>>) {
    val maxTimeMillis = screenTimeData.maxOfOrNull { it.second } ?: 0L

    if (maxTimeMillis == 0L) return

    val leftPadding = 80f
    val rightPadding = 40f
    val topPadding = 40f
    val bottomPadding = 60f
    val chartWidth = size.width - leftPadding - rightPadding
    val chartHeight = size.height - topPadding - bottomPadding

    // 縦軸の目盛り値を計算
    val yAxisValues = calculateScreenTimeYAxisValues(maxTimeMillis)

    // グリッド線を描画
    val gridColor = Color.LightGray.copy(alpha = 0.5f)

    // 水平グリッド線
    yAxisValues.forEach { timeMillis ->
        val normalizedValue = timeMillis.toFloat() / maxTimeMillis
        val y = topPadding + chartHeight - normalizedValue * chartHeight
        drawLine(
            color = gridColor,
            start = Offset(leftPadding, y),
            end = Offset(size.width - rightPadding, y),
            strokeWidth = 1f
        )
    }

    // 垂直グリッド線
    screenTimeData.forEachIndexed { index, _ ->
        val x = leftPadding + (chartWidth / (screenTimeData.size - 1)) * index
        drawLine(
            color = gridColor,
            start = Offset(x, topPadding),
            end = Offset(x, topPadding + chartHeight),
            strokeWidth = 1f
        )
    }

    // データポイントの座標を計算
    val points = screenTimeData.mapIndexed { index, (_, timeMillis) ->
        val x = leftPadding + (chartWidth / (screenTimeData.size - 1)) * index
        val normalizedValue = timeMillis.toFloat() / maxTimeMillis
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
        color = Color(0xFFFF9800),
        style = Stroke(width = 3f)
    )

    // データポイントを描画
    points.forEach { point ->
        drawCircle(
            color = Color.White,
            center = point,
            radius = 6f
        )
        drawCircle(
            color = Color(0xFFFF9800),
            center = point,
            radius = 4f
        )
    }

    // 縦軸のラベル
    val yAxisTextPaint = Paint().apply {
        color = Color.Gray.toArgb()
        textSize = 24f
        textAlign = Paint.Align.RIGHT
    }

    yAxisValues.forEach { timeMillis ->
        val normalizedValue = timeMillis.toFloat() / maxTimeMillis
        val y = topPadding + chartHeight - normalizedValue * chartHeight
        val hours = TimeUnit.MILLISECONDS.toHours(timeMillis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeMillis) % 60
        val labelText = if (hours > 0) "${hours}h${minutes}m" else "${minutes}m"

        drawContext.canvas.nativeCanvas.drawText(
            labelText,
            leftPadding - 10f,
            y + 8f,
            yAxisTextPaint
        )
    }

    // 横軸のラベル
    val xAxisTextPaint = Paint().apply {
        color = Color.Gray.toArgb()
        textSize = 22f
        textAlign = Paint.Align.CENTER
    }

    screenTimeData.forEachIndexed { index, (date, _) ->
        val x = leftPadding + (chartWidth / (screenTimeData.size - 1)) * index
        drawContext.canvas.nativeCanvas.drawText(
            date,
            x,
            size.height - 10f,
            xAxisTextPaint
        )
    }
}

private fun calculateScreenTimeYAxisValues(maxTimeMillis: Long): List<Long> {
    val maxHours = TimeUnit.MILLISECONDS.toHours(maxTimeMillis)
    val interval = when {
        maxHours <= 2 -> 30 * 60 * 1000L // 30分間隔
        maxHours <= 6 -> 60 * 60 * 1000L // 1時間間隔
        maxHours <= 12 -> 2 * 60 * 60 * 1000L // 2時間間隔
        else -> 4 * 60 * 60 * 1000L // 4時間間隔
    }

    val values = mutableListOf<Long>()
    var current = 0L
    while (current <= maxTimeMillis) {
        values.add(current)
        current += interval
    }

    return values
}
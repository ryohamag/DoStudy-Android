package com.websarva.wings.dostudy_android.components

import android.text.TextPaint
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import com.websarva.wings.dostudy_android.Room.ResultDataTable
import com.websarva.wings.dostudy_android.functions.timeToSeconds

@Composable
fun LineChart(
    resultDataTable: List<ResultDataTable>,
    modifier: Modifier = Modifier,
    lineColor: Color = Color.Black
) {
    val maxValue = timeToSeconds(resultDataTable.maxOf { it.studyTime })
    val minValue = 0

    Canvas(modifier = modifier) {
        val spaceBetweenPoints = size.width / (resultDataTable.size - 1)
        val scale = size.height / (maxValue - minValue)

        // Map points to canvas coordinates
        val points = resultDataTable.mapIndexed { index, value ->
            val x = index * spaceBetweenPoints
            val y = size.height - (timeToSeconds(value.studyTime) - minValue) * scale
            Offset(x, y)
        }

        // Draw grid lines (方眼)
        val gridSpacing = 10
        val gridLineColor = Color.LightGray
        for (i in 0 until (size.height / gridSpacing).toInt()) {
            // Horizontal grid lines
            drawLine(
                color = gridLineColor,
                start = Offset(0f, i * gridSpacing.toFloat()),
                end = Offset(size.width, i * gridSpacing.toFloat()),
                strokeWidth = 1f
            )
        }
        for (i in 0 until (size.width / spaceBetweenPoints).toInt()) {
            // Vertical grid lines
            drawLine(
                color = gridLineColor,
                start = Offset(i * spaceBetweenPoints, 0f),
                end = Offset(i * spaceBetweenPoints, size.height),
                strokeWidth = 1f
            )
        }

        // Draw Y-axis markers (目盛り) with values
        val yAxisSpacing = 10 // Y軸の目盛りの間隔
        val textPaint = TextPaint().apply {
            color = Color.Black.toArgb()
            textSize = 30f
        }

        for (i in minValue until maxValue step yAxisSpacing) {
            val y = size.height - (i - minValue) * scale
            drawLine(
                color = gridLineColor,
                start = Offset(0f, y),
                end = Offset(10f, y), // Y軸方向の目盛り
                strokeWidth = 2f
            )

            // Draw the value next to the Y-axis marker
            drawContext.canvas.nativeCanvas.drawText(
                i.toString(),
                15f, // X position for the label (slightly to the right of the marker)
                y + 5f, // Y position for the label (centered vertically)
                textPaint
            )
        }

        // Draw X-axis markers (目盛り)
        for (i in resultDataTable.indices) {
            val x = i * spaceBetweenPoints
            drawLine(
                color = gridLineColor,
                start = Offset(x, size.height),
                end = Offset(x, size.height - 10f), // X軸方向の目盛り
                strokeWidth = 2f
            )
        }

        // Draw the line connecting points
        for (i in 0 until points.size - 1) {
            drawLine(
                color = lineColor,
                start = points[i],
                end = points[i + 1],
                strokeWidth = 4f
            )
        }

        // Draw circles for each point with color based on `status`
        points.forEachIndexed { index, point ->
            val pointColor = if (resultDataTable[index].status) Color.Blue else Color.Red
            drawCircle(
                color = pointColor,
                center = point,
                radius = 8f
            )
        }
    }
}




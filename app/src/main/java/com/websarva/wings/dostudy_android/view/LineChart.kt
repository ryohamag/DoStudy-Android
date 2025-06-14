package com.websarva.wings.dostudy_android.view

import android.text.TextPaint
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import com.websarva.wings.dostudy_android.model.Room.ResultDataTable
import com.websarva.wings.dostudy_android.functions.timeToSeconds

@Composable
fun LineChart(
    resultDataTable: List<ResultDataTable>,
    modifier: Modifier = Modifier,
    lineColor: Color = Color.Black
) {
    val dataTable = resultDataTable.reversed().take(20).reversed()
    val maxValue = timeToSeconds(dataTable.maxOf { it.studyTime })
    val minValue = timeToSeconds(dataTable.minOf { it.studyTime })

    Canvas(modifier = modifier) {
        val spaceBetweenPoints = size.width / (dataTable.size - 1)
        val scale = size.height / (maxValue - minValue)

        //点の座標を計算
        val points = dataTable.mapIndexed { index, value ->
            val x = index * spaceBetweenPoints
            val y = size.height - (timeToSeconds(value.studyTime) - minValue) * scale
            Offset(x, y)
        }

        //方眼を描画
        val gridSpacing = 10
        val gridLineColor = Color.LightGray
        for (i in 0 until (size.height / gridSpacing).toInt()) {
            drawLine(
                color = gridLineColor,
                start = Offset(0f, i * gridSpacing.toFloat()),
                end = Offset(size.width, i * gridSpacing.toFloat()),
                strokeWidth = 1f
            )
        }
        for (i in 0 until (size.width / spaceBetweenPoints).toInt()) {
            drawLine(
                color = gridLineColor,
                start = Offset(i * spaceBetweenPoints, 0f),
                end = Offset(i * spaceBetweenPoints, size.height),
                strokeWidth = 1f
            )
        }

        //y軸の目盛りを描画
        val yAxisSpacing = 1800 // Y軸の目盛りの間隔(30分ごと)
        val textPaint = TextPaint().apply {
            color = Color.Black.toArgb()
            textSize = 30f
        }

        for (i in minValue until maxValue step yAxisSpacing) {
            val y = size.height - (i - minValue) * scale
            drawLine(
                color = gridLineColor,
                start = Offset(0f, y),
                end = Offset(10f, y),
                strokeWidth = 2f
            )

            drawContext.canvas.nativeCanvas.drawText(
                String.format("%.1fh", i / 3600f),
                15f,
                y + 5f,
                textPaint
            )
        }

        //x軸の目盛りを描画
        for (i in dataTable.indices) {
            val x = i * spaceBetweenPoints
            drawLine(
                color = gridLineColor,
                start = Offset(x, size.height),
                end = Offset(x, size.height - 10f), // X軸方向の目盛り
                strokeWidth = 2f
            )
        }

        for (i in 0 until points.size - 1) {
            drawLine(
                color = lineColor,
                start = points[i],
                end = points[i + 1],
                strokeWidth = 4f
            )
        }

        //点を描画
        points.forEachIndexed { index, point ->
            val pointColor = if (dataTable[index].status) Color.Blue else Color.Red
            drawCircle(
                color = pointColor,
                center = point,
                radius = 8f
            )
        }
    }
}




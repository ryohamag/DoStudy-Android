package com.websarva.wings.dostudy_android.view

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.websarva.wings.dostudy_android.R
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun CircularTimer(
    hour: Int,
    minute: Int,
    second: Int,
    setTimer: Int,
    selectedFont: Int
) {
    var elapsedMillis by remember { mutableLongStateOf(0L) }
    val totalDurationMillis = setTimer * 1000L

    // MaterialThemeの色を事前に取得
    val outlineColor = MaterialTheme.colorScheme.outline
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface

    LaunchedEffect(Unit) {
        val startTime = System.currentTimeMillis()
        while (elapsedMillis < totalDurationMillis) {
            elapsedMillis = System.currentTimeMillis() - startTime
            delay(1)
        }
    }

    val progress = (elapsedMillis.toFloat() / totalDurationMillis).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 100, easing = LinearEasing),
        label = ""
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(32.dp)
            .size(280.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = center
            val radius = size.minDimension / 2 - 5.dp.toPx()

            // シンプルな背景円
            drawCircle(
                color = outlineColor.copy(alpha = 0.3f),
                style = Stroke(width = 2.dp.toPx()),
                radius = radius,
                center = center
            )

            // プログレスアーク（極細）
            drawArc(
                color = onSurfaceColor,
                startAngle = -90f,
                sweepAngle = -animatedProgress * 360f,
                useCenter = false,
                style = Stroke(width = 2.dp.toPx()),
                topLeft = Offset(
                    center.x - radius,
                    center.y - radius
                ),
                size = Size(radius * 2, radius * 2)
            )

            // プログレスの終端ドット
            if (animatedProgress > 0) {
                val angle = -90f + (-animatedProgress * 360f)
                val radians = Math.toRadians(angle.toDouble())
                val dotX = center.x + radius * cos(radians).toFloat()
                val dotY = center.y + radius * sin(radians).toFloat()

                drawCircle(
                    color = onSurfaceColor,
                    radius = 4.dp.toPx(),
                    center = Offset(dotX, dotY)
                )
            }
        }

        Text(
            text = "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}:${second.toString().padStart(2, '0')}",
            fontSize = 36.sp,
            fontFamily = when (selectedFont) {
                0 -> FontFamily.Default
                1 -> FontFamily(Font(R.font.noto_sans_jp))
                2 -> FontFamily(Font(R.font.montserrat))
                3 -> FontFamily(Font(R.font.open_sans))
                4 -> FontFamily(Font(R.font.playfair_display))
                5 -> FontFamily(Font(R.font.new_amsterdam))
                else -> FontFamily.Default
            },
            color = onSurfaceColor,
            fontWeight = FontWeight.Light
        )
    }
}

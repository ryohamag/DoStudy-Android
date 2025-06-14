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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.websarva.wings.dostudy_android.R

@Composable
fun CircularTimer(
    hour: Int,
    minute: Int,
    second: Int,
    setTimer: Int,
    selectedFont: Int
) {
    //経過時間を保持
    var elapsedMillis by remember { mutableLongStateOf(0L) }

    val totalDurationMillis = setTimer * 1000L

    //0.001秒刻みにする(インジケータをスムーズにする)ため
    LaunchedEffect(Unit) {
        val startTime = System.currentTimeMillis()
        while (elapsedMillis < totalDurationMillis) {
            elapsedMillis = System.currentTimeMillis() - startTime
            kotlinx.coroutines.delay(1) // 1msごとに更新
        }
    }

    //進捗を計算
    val progress = (elapsedMillis.toFloat() / totalDurationMillis).coerceIn(0f, 1f)

    //滑らかに
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(
            durationMillis = 0, // 更新のスムーズさを調整
            easing = LinearEasing
        ), label = ""
    )

    //タイマー描画
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(start = 16.dp, top = 64.dp, end = 16.dp, bottom = 64.dp)
            .size(300.dp)
    ) {
        // Canvasで円と円弧を描画
        Canvas(modifier = Modifier.fillMaxSize()) {
            //円の外枠
            drawCircle(
                color = Color.Gray,
                style = Stroke(width = 8.dp.toPx())
            )

            //円弧
            drawArc(
                color = Color.Blue,
                startAngle = -90f,
                sweepAngle = -animatedProgress * 360f, //反時計回り
                useCenter = false,
                style = Stroke(width = 8.dp.toPx())
            )
        }

        // 中央のテキストを描画
        Text(
            text = "${hour.toString().padStart(2, '0')}h" +
                    "${minute.toString().padStart(2, '0')}m" +
                    "${second.toString().padStart(2, '0')}s",
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
            color = Color.Black
        )
    }
}

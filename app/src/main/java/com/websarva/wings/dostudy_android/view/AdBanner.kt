package com.websarva.wings.dostudy_android.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun AdBanner(modifier: Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER) // 修正: setAdSize を使用
                adUnitId = "ca-app-pub-3940256099942544/6300978111" //テスト用
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}
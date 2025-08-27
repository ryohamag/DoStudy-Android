package com.websarva.wings.dostudy_android.view

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.websarva.wings.dostudy_android.viewmodel.MainViewModel

@Composable
fun AdScreen(vm: MainViewModel) {
    val context = LocalContext.current
    var interstitialAd: InterstitialAd? by remember { mutableStateOf(null) }
    var isAdLoaded by remember { mutableStateOf(false) } // 広告のロード状態を管理

    // インタースティシャル広告を読み込む
    LaunchedEffect(Unit) {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            "ca-app-pub-3940256099942544/1033173712", // テスト用ユニットID
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    Log.d("AdScreen", "Ad loaded")
                    interstitialAd = ad
                    isAdLoaded = true // 広告がロードされた状態を更新
                    ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            // 広告が閉じられた
                            vm.isShowAdScreen = false
                        }

                        override fun onAdFailedToShowFullScreenContent(error: AdError) {
                            // 広告の表示に失敗
                            vm.isShowAdScreen = false
                        }
                    }
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    // 広告のロードに失敗
                    vm.isShowAdScreen = false
                    Log.e("AdScreen", "Failed to load interstitial ad: ${error.message}")
                }
            }
        )
    }

    if (isAdLoaded) {
        interstitialAd?.show(context as Activity)
    } else {
        Log.e("AdScreen", "Interstitial ad not loaded yet")
    }
}


package com.websarva.wings.dostudy_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.websarva.wings.dostudy_android.components.MainScreen
import com.websarva.wings.dostudy_android.ui.theme.DoStudyAndroidTheme
import com.websarva.wings.dostudy_android.viewmodels.MainScreenViewModel
import android.content.Context
import android.os.PowerManager
import com.google.android.gms.ads.MobileAds
import com.websarva.wings.dostudy_android.components.ResultScreen
import com.websarva.wings.dostudy_android.functions.httpRequest

class MainActivity : ComponentActivity() {
    private lateinit var vm: MainScreenViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        vm = MainScreenViewModel(this)
        MobileAds.initialize(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DoStudyAndroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val context = LocalContext.current
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "Main") {
                        composable("Main") {
                            MainScreen(navController, innerPadding, context, vm)
                        }
                        composable("Result") {
                            ResultScreen(innerPadding, vm)
                        }
                    }
                }
            }
        }
    }

    //画面外検知の為にオーバーライド
    override fun onPause() {
        super.onPause()
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        if (powerManager.isInteractive) {
            // 通常の onPause（アプリが非表示になった等）
            handleReturnFromBackground(vm)
        }
    }

    private fun handleReturnFromBackground(vm: MainScreenViewModel) {
        if(vm.isStudyStarted) {
            vm.addResultData(false)
            vm.isShowFailedDialog = true
            httpRequest(channelId = vm.channelId, username = vm.username, status = false, vm.seconds, vm = vm)
            vm.reset()
        }
    }
}

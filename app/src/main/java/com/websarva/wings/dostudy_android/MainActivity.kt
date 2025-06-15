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
import com.websarva.wings.dostudy_android.view.MainScreen
import com.websarva.wings.dostudy_android.ui.theme.DoStudyAndroidTheme
import com.websarva.wings.dostudy_android.viewmodel.MainViewModel
import android.content.Context
import android.os.PowerManager
import androidx.activity.viewModels
import com.google.android.gms.ads.MobileAds
import com.websarva.wings.dostudy_android.view.ResultScreen
import com.websarva.wings.dostudy_android.view.SettingScreen
import com.websarva.wings.dostudy_android.functions.httpRequest
import com.websarva.wings.dostudy_android.util.FontConstants
import com.websarva.wings.dostudy_android.view.BottomBar
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainVM: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        MobileAds.initialize(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DoStudyAndroidTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomBar(navController)
                    }
                ) { innerPadding ->
                    val context = LocalContext.current
                    NavHost(navController = navController, startDestination = "Home") {
                        composable("Home") {
                            MainScreen(navController, innerPadding, context, mainVM)
                        }
                        composable("Result") {
                            ResultScreen(innerPadding, mainVM)
                        }
                        composable("Settings") {
                            SettingScreen(
                                username = mainVM.username,
                                onUsernameChange = { mainVM.username = it },
                                channelId = mainVM.channelId,
                                onChannelIdChange = { mainVM.channelId = it },
                                createUserData = { mainVM.createUserData() },
                                updateUserData = { mainVM.updateUserData() },
                                isFirstStartup = mainVM.isFirstStartup,
                                selectedFont = mainVM.selectedFont,
                                selectedFontChange = { mainVM.selectedFont = it },
                                fonts = FontConstants.fonts,
                                navController = navController
                            )
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
            handleReturnFromBackground(mainVM)
        }
    }

    private fun handleReturnFromBackground(vm: MainViewModel) {
        if(vm.isStudyStarted) {
            vm.addResultData(false)
            vm.isShowFailedDialog = true
            httpRequest(channelId = vm.channelId, username = vm.username, status = false, vm.seconds.value, vm = vm)
            vm.reset()
        }
    }
}

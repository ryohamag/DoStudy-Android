package com.websarva.wings.dostudy_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.websarva.wings.dostudy_android.components.MainScreen
import com.websarva.wings.dostudy_android.components.TimerSetMenu
import com.websarva.wings.dostudy_android.ui.theme.DoStudyAndroidTheme
import com.websarva.wings.dostudy_android.viewmodels.MainScreenViewModel
import android.Manifest
import android.app.KeyguardManager
import android.content.Context
import android.content.Context.POWER_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.getSystemService
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.websarva.wings.dostudy_android.functions.httpRequest

class MainActivity : ComponentActivity() {
    private lateinit var vm: MainScreenViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        vm = MainScreenViewModel(this)
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
                        composable("TimerSetting") {
                            TimerSetMenu(vm)
                        }
                    }
                }
            }
        }
    }

    private var isComingFromSleep = false
    private var lastBackgroundTime: Long = 0

    override fun onResume() {
        super.onResume()
        // KeyguardManagerを使ってスリープ復帰を判定
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        isComingFromSleep = keyguardManager.isKeyguardLocked

        if (isComingFromSleep) {
            Log.d("AppState", "onResume: スリープから復帰しました")
            handleWakeFromSleep()
        } else if (System.currentTimeMillis() - lastBackgroundTime > 0) {
            Log.d("AppState", "onResume: 他アプリやホーム画面から戻ってきました")
            handleReturnFromBackground(vm)
        }
    }

    private fun handleWakeFromSleep() {
        // スリープ復帰時の処理
        Log.d("AppState", "スリープ復帰時の処理を実行")
    }

    private fun handleReturnFromBackground(vm: MainScreenViewModel) {
        if(vm.isStudyStarted) {
            vm.isShowFailedDialog = true
            httpRequest(channelId = vm.channelId, username = vm.username, status = false, vm.seconds, vm = vm)
            vm.reset()
        }
        Log.d("AppState", "他アプリ復帰時の処理を実行")
    }
}

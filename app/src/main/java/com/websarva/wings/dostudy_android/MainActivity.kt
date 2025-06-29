package com.websarva.wings.dostudy_android

import android.Manifest
import android.app.AppOpsManager
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
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.MobileAds
import com.websarva.wings.dostudy_android.view.ResultScreen
import com.websarva.wings.dostudy_android.view.SettingScreen
import com.websarva.wings.dostudy_android.functions.httpRequest
import com.websarva.wings.dostudy_android.model.notification.service.ScreenTimeService
import com.websarva.wings.dostudy_android.util.FontConstants
import com.websarva.wings.dostudy_android.view.BottomBar
import com.websarva.wings.dostudy_android.view.MonitorScreen
import com.websarva.wings.dostudy_android.view.ToDoScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.getValue
import kotlin.text.compareTo

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainVM: MainViewModel by viewModels()

    companion object {
        private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        MobileAds.initialize(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (hasUsageStatsPermission()) {
            startScreenTimeService()
        }

        requestNotificationPermission()
        requestUsageStatsPermission()
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
                            ResultScreen(innerPadding, mainVM, navController)
                        }
                        composable("Monitor") {
                            MonitorScreen(
                                navController = navController,
                                vm = mainVM,
                            )
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
                                navController = navController,
                                dailyLimit = mainVM.dailyLimit.collectAsState().value,
                                onDailyLimitChange = { limit -> mainVM.updateDailyLimitTemporary(limit) }

                            )
                        }
                        composable("ToDoList") {
                            ToDoScreen(
                                navController = navController,
                                vm = mainVM,
                                showAddToDoDialog = { mainVM.isShowAddToDoDialog = true },
                                deleteToDo = { title ->
                                    mainVM.deleteToDo(title)
                                },
                                innerPadding = innerPadding
                            )
                        }
                    }
                }
            }
        }
    }

    private fun checkAndRequestPermissions() {
        requestNotificationPermission()
        requestUsageStatsPermission()
        // 例: バッテリー最適化の除外要求等も必要であれば実装
    }

    // 通知権限のリクエスト
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    private fun startScreenTimeService() {
        val intent = Intent(this, ScreenTimeService::class.java)
        // intentからの値渡しは不要（DataStoreから取得するため）
        startForegroundService(intent)
        Log.d("MainActivity", "ScreenTimeService開始要求")
    }

    // 使用統計権限のリクエスト（設定画面へ誘導）
    private fun requestUsageStatsPermission() {
        if (!hasUsageStatsPermission()) {
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            intent.data = Uri.parse("package:$packageName")
            startActivity(intent)
        }
    }

    // 使用統計権限が許可されているかチェックするメソッド
    private fun hasUsageStatsPermission(): Boolean {
        val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(),
            packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }

    // 権限リクエスト結果のコールバック
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            // 結果に応じた処理を実装可能
        }
    }

    override fun onResume() {
        super.onResume()
        // アプリ復帰時に権限状況をチェック
        checkAndRequestPermissions()
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

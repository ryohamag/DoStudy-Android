package com.websarva.wings.dostudy_android.model.notification.service

import android.Manifest
import android.app.AppOpsManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import com.websarva.wings.dostudy_android.MainActivity
import com.websarva.wings.dostudy_android.R
import com.websarva.wings.dostudy_android.model.notification.NotificationHelper
import com.websarva.wings.dostudy_android.model.repository.DataStoreRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ScreenTimeService : Service() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    @Inject
    lateinit var dataStoreRepository: DataStoreRepository

    private var dailyLimitMinutes: Int = 120
    private var serviceJob: Job? = null
    private var timer: Timer? = null
    private val checkInterval = 60 * 1000L // 1分ごとにチェック
    private var lastNotificationTime = 0L
    private val notificationCooldown = 60 * 60 * 1000L // 1時間のクールダウン

    companion object {
        private const val FOREGROUND_CHANNEL_ID = "screen_time_service"
        private const val FOREGROUND_NOTIFICATION_ID = 2001
    }

    override fun onCreate() {
        super.onCreate()
        createForegroundNotificationChannel()
        Log.d("ScreenTimeService", "サービス作成")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // DataStoreから制限時間を取得
        serviceJob = CoroutineScope(Dispatchers.IO).launch {
            dataStoreRepository.getDailyLimit().collect { limit ->
                dailyLimitMinutes = limit
                Log.d("ScreenTimeService", "制限時間更新: ${dailyLimitMinutes}分")
            }
        }

        startForeground(FOREGROUND_NOTIFICATION_ID, createForegroundNotification())
        startPeriodicCheck()
        return START_STICKY
    }

    private fun createForegroundNotificationChannel() {
        val channel = NotificationChannel(
            FOREGROUND_CHANNEL_ID,
            "スクリーンタイム監視",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "スクリーンタイムを監視しています"
            setShowBadge(false)
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun createForegroundNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, FOREGROUND_CHANNEL_ID)
            .setContentTitle("スクリーンタイム監視中")
            .setContentText("制限時間: ${dailyLimitMinutes}分")
            .setSmallIcon(R.drawable.baseline_timer_24)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    private fun hasUsageStatsPermission(): Boolean {
        val appOpsManager = getSystemService(APP_OPS_SERVICE) as AppOpsManager
        val mode = appOpsManager.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(),
            packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun startPeriodicCheck() {
        timer?.cancel()
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
            override fun run() {
                checkScreenTime()
            }
        }, 0, checkInterval)
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun checkScreenTime() {
        if (!hasUsageStatsPermission()) {
            Log.w("ScreenTimeService", "使用統計権限がありません")
            return
        }

        val usageStatsManager = getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val startTime = calendar.timeInMillis
        val endTime = System.currentTimeMillis()

        val usageStats = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            startTime,
            endTime
        )

        var totalTimeInForeground = 0L
        usageStats?.forEach { stats ->
            totalTimeInForeground += stats.totalTimeInForeground
        }

        val totalMinutes = (totalTimeInForeground / (1000 * 60)).toInt()
        Log.d("ScreenTimeService", "現在のスクリーンタイム: ${totalMinutes}分")

        if (totalMinutes >= dailyLimitMinutes) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastNotificationTime > notificationCooldown) {
                notificationHelper.sendScreenTimeLimitNotification(totalMinutes, dailyLimitMinutes)
                lastNotificationTime = currentTime
                Log.d("ScreenTimeService", "制限時間に到達 - 通知送信")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        serviceJob?.cancel()
        Log.d("ScreenTimeService", "サービス停止")
    }
}
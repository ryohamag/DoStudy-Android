package com.websarva.wings.dostudy_android.model.repository

import android.app.AppOpsManager
import android.app.usage.UsageStatsManager
import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScreenTimeRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getScreenTimeData(): List<Pair<String, Long>> {
        if (!hasUsageStatsPermission()) return emptyList()

        val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val calendar = Calendar.getInstance()
        val endTime = System.currentTimeMillis()
        val screenTimeList = mutableListOf<Pair<String, Long>>()

        // 直近7日間のデータを取得
        for (i in 6 downTo 0) {
            calendar.timeInMillis = endTime
            calendar.add(Calendar.DAY_OF_YEAR, -i)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val startTime = calendar.timeInMillis

            calendar.set(Calendar.HOUR_OF_DAY, 23)
            calendar.set(Calendar.MINUTE, 59)
            calendar.set(Calendar.SECOND, 59)
            calendar.set(Calendar.MILLISECOND, 999)
            val dayEndTime = calendar.timeInMillis

            val usageStats = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                startTime,
                dayEndTime
            )

            var totalTimeInForeground = 0L
            usageStats?.forEach { stats ->
                totalTimeInForeground += stats.totalTimeInForeground
            }

            val dateFormat = SimpleDateFormat("M/d", Locale.getDefault())
            val dateString = dateFormat.format(Date(startTime))
            screenTimeList.add(Pair(dateString, totalTimeInForeground))
        }

        return screenTimeList
    }

    private fun hasUsageStatsPermission(): Boolean {
        val appOpsManager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOpsManager.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(),
            context.packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }
}
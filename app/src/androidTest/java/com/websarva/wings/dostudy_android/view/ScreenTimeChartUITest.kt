package com.websarva.wings.dostudy_android.view

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScreenTimeChartUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * ScreenTimeChartがデータなしの場合に適切なメッセージを表示することをテスト
     */
    @Test
    fun screenTimeChart_displaysNoAccessMessage() {
        composeTestRule.setContent {
            ScreenTimeChart(
                screenTimeData = emptyList(),
                modifier = Modifier
            )
        }

        composeTestRule.onNodeWithText("使用状況へのアクセスが許可されていません。").assertIsDisplayed()
    }

    /**
     * ScreenTimeChartがデータありの場合にタイトルを表示することをテスト
     */
    @Test
    fun screenTimeChart_displaysTitle() {
        val screenTimeData = listOf(
            Pair("App1", 3600000L), // 1時間 (ミリ秒)
            Pair("App2", 1800000L)  // 30分 (ミリ秒)
        )

        composeTestRule.setContent {
            ScreenTimeChart(
                screenTimeData = screenTimeData,
                modifier = Modifier
            )
        }

        composeTestRule.onNodeWithText("スクリーンタイム（直近7日間）").assertIsDisplayed()
    }

    /**
     * ScreenTimeChartが複数のアプリデータで正しく表示されることをテスト
     */
    @Test
    fun screenTimeChart_displaysWithMultipleApps() {
        val screenTimeData = listOf(
            Pair("Instagram", 7200000L), // 2時間
            Pair("YouTube", 5400000L),   // 1.5時間
            Pair("Twitter", 3600000L),   // 1時間
            Pair("TikTok", 1800000L)     // 30分
        )

        composeTestRule.setContent {
            ScreenTimeChart(
                screenTimeData = screenTimeData,
                modifier = Modifier
            )
        }

        composeTestRule.onNodeWithText("スクリーンタイム（直近7日間）").assertIsDisplayed()
        // チャートが正常に描画されることを確認（クラッシュしない）
        composeTestRule.waitForIdle()
    }
}

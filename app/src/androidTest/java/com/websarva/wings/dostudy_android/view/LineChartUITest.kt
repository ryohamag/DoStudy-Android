package com.websarva.wings.dostudy_android.view

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.websarva.wings.dostudy_android.model.Room.ResultData.ResultDataTable
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LineChartUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * LineChartがデータなしの場合に適切なメッセージを表示することをテスト
     */
    @Test
    fun lineChart_displaysNoDataMessage() {
        composeTestRule.setContent {
            LineChart(
                resultDataTable = emptyList(),
                modifier = Modifier
            )
        }

        composeTestRule.onNodeWithText("データがありません").assertIsDisplayed()
    }

    /**
     * LineChartがデータありの場合にタイトルを表示することをテスト
     */
    @Test
    fun lineChart_displaysTitle() {
        val resultData = listOf(
            ResultDataTable(1, "2024-01-15", "01:30:00", "01:25:30", true, "数学"),
            ResultDataTable(2, "2024-01-16", "02:00:00", "01:45:15", true, "英語")
        )

        composeTestRule.setContent {
            LineChart(
                resultDataTable = resultData,
                modifier = Modifier
            )
        }

        composeTestRule.onNodeWithText("作業時間の推移").assertIsDisplayed()
    }

    /**
     * LineChartが単一データの場合に正しく表示されることをテスト
     */
    @Test
    fun lineChart_displaysWithSingleData() {
        val resultData = listOf(
            ResultDataTable(1, "2024-01-15", "01:30:00", "01:25:30", true, "数学")
        )

        composeTestRule.setContent {
            LineChart(
                resultDataTable = resultData,
                modifier = Modifier
            )
        }

        composeTestRule.onNodeWithText("作業時間の推移").assertIsDisplayed()
        // 単一データの場合でもクラッシュしないことを確認
        composeTestRule.waitForIdle()
    }
}

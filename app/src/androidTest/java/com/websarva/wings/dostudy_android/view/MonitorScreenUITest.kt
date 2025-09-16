package com.websarva.wings.dostudy_android.view

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.websarva.wings.dostudy_android.model.Room.ResultData.ResultDataTable
import com.websarva.wings.dostudy_android.viewmodel.MainViewModel
import io.mockk.*
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MonitorScreenUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * MonitorScreenが基本的な要素を表示することをテスト
     */
    @Test
    fun monitorScreen_displaysBasicElements() {
        val mockNavController = mockk<NavController>(relaxed = true)
        val mockViewModel = mockk<MainViewModel>(relaxed = true) {
            every { resultDataList } returns MutableStateFlow(
                listOf(
                    // ResultDataTableのインスタンス生成には名前付き引数を使うと可読性が上がります
                    ResultDataTable(1, "2024-01-15", "01:30:00", "01:25:30", true, "数学"),
                    ResultDataTable(2, "2024-01-16", "02:00:00", "01:45:15", false, "英語")
                )
            )
            every { screenTimeData } returns MutableStateFlow(
                listOf(
                    Pair("App1", 3600000L),
                    Pair("App2", 1800000L)
                )
            )
            // isShowChartは使われていないので、このテストからは削除しても良い
            // every { isShowChart } returns true
        }

        composeTestRule.setContent {
            MonitorScreen(
                navController = mockNavController,
                vm = mockViewModel
            )
        }

        // --- 検証部分を修正 ---

        // 1. トップバーのタイトルは正しく表示されるはず
        composeTestRule.onNodeWithText("モニタリング").assertIsDisplayed()

        // 2. "リスト"の代わりに、実際のヘッダーテキストを検証する
        composeTestRule.onNodeWithText("直近5回の作業履歴").assertIsDisplayed()

        // 3. モックデータがリスト項目（SmallResultCard）として表示されていることを確認する
        //    これにより、リスト部分が正しく機能していることを間接的に証明できる
        composeTestRule.onNodeWithText("数学").assertIsDisplayed()
        composeTestRule.onNodeWithText("英語").assertIsDisplayed()
    }

    /**
     * MonitorScreenでグラフ表示モードをテスト
     */
    @Test
    fun monitorScreen_displaysChartMode() {
        val mockNavController = mockk<NavController>(relaxed = true)
        val mockViewModel = mockk<MainViewModel>(relaxed = true) {
            every { resultDataList } returns MutableStateFlow(
                listOf(
                    ResultDataTable(1, "2024-01-15", "01:30:00", "01:25:30", true, "数学"),
                    ResultDataTable(2, "2024-01-16", "02:00:00", "01:45:15", true, "英語")
                )
            )
            every { screenTimeData } returns MutableStateFlow(
                listOf(Pair("App1", 3600000L))
            )
            every { isShowChart } returns true
        }

        composeTestRule.setContent {
            MonitorScreen(
                navController = mockNavController,
                vm = mockViewModel
            )
        }

        composeTestRule.onNodeWithText("作業時間の推移").assertIsDisplayed()
        composeTestRule.onNodeWithText("スクリーンタイム（直近7日間）").assertIsDisplayed()
    }
}

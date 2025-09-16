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
class ResultScreenUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * ResultScreenが基本的な要素を表示することをテスト
     */
    @Test
    fun resultScreen_displaysBasicElements() {
        val mockNavController = mockk<NavController>(relaxed = true)
        val mockViewModel = mockk<MainViewModel>(relaxed = true) {
            every { resultDataList } returns MutableStateFlow(
                listOf(
                    ResultDataTable(1, "2024-01-15", "01:30:00", "01:25:30", true, "数学")
                )
            )
        }

        composeTestRule.setContent {
            ResultScreen(navController = mockNavController, vm = mockViewModel)
        }

        // "全ての履歴"というテキストが2つ（タイトルとヘッダー）あることを確認
        composeTestRule.onAllNodesWithText("全ての履歴").assertCountEquals(2)
        verify { mockViewModel.resultDataList }
    }

    /**
     * ResultScreenで結果データが表示されることをテスト
     */
    @Test
    fun resultScreen_displaysResultData() {
        val mockNavController = mockk<NavController>(relaxed = true)
        val mockViewModel = mockk<MainViewModel>(relaxed = true) {
            every { resultDataList } returns MutableStateFlow(
                listOf(
                    ResultDataTable(1, "2024-01-15", "01:30:00", "01:25:30", true, "数学の勉強"),
                    ResultDataTable(2, "2024-01-16", "02:00:00", "01:45:15", false, "英語学習")
                )
            )
        }

        composeTestRule.setContent {
            ResultScreen(navController = mockNavController, vm = mockViewModel)
        }

        // 実際に表示されるテキストを検証する
        composeTestRule.onAllNodesWithText("全ての履歴").assertCountEquals(2)
        composeTestRule.onNodeWithText("数学の勉強").assertIsDisplayed()
        composeTestRule.onNodeWithText("英語学習").assertIsDisplayed()

        // もしResultCardが日付を表示するなら、日付で検証するのも良い方法
        composeTestRule.onNodeWithText("2024-01-15").assertIsDisplayed()
        composeTestRule.onNodeWithText("2024-01-16").assertIsDisplayed()
    }

    /**
     * ResultScreenで空のデータでも正しく表示されることをテスト
     */
    @Test
    fun resultScreen_displaysWithEmptyData() {
        val mockNavController = mockk<NavController>(relaxed = true)
        val mockViewModel = mockk<MainViewModel>(relaxed = true) {
            every { resultDataList } returns MutableStateFlow(emptyList())
        }

        composeTestRule.setContent {
            ResultScreen(
                navController = mockNavController,
                vm = mockViewModel
            )
        }

        composeTestRule.onAllNodesWithText("全ての履歴").assertCountEquals(2)
        // 空のリストでもクラッシュしないことを確認
        composeTestRule.waitForIdle()
    }
}

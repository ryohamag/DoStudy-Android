package com.websarva.wings.dostudy_android.view

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.websarva.wings.dostudy_android.model.Room.PlatformData.PlatformDataTable
import com.websarva.wings.dostudy_android.util.FontConstants.fonts
import com.websarva.wings.dostudy_android.viewmodel.SettingScreenUiState
import com.websarva.wings.dostudy_android.viewmodel.SettingScreenViewModel
import io.mockk.*
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingScreenUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * SettingScreenが基本的な要素を表示することをテスト
     */
    @Test
    fun settingScreen_displaysBasicElements() {
        val mockViewModel = mockk<SettingScreenViewModel>(relaxed = true) {
            every { uiState } returns MutableStateFlow(
                SettingScreenUiState(
                    username = "テストユーザー",
                    channelId = "test_channel",
                    dailyLimit = 120,
                    selectedFont = 0,
                    platformData = listOf(
                        PlatformDataTable(1, "Discord", "TestChannel", "test_key")
                    ),
                    isFirstStartup = false
                )
            )
        }

        val mockNavController = mockk<NavController>(relaxed = true)

        composeTestRule.setContent {
            SettingScreen(vm = mockViewModel, navController = mockNavController)
        }

        // --- 検証部分を修正 ---
        // トップバーのタイトルを検証
        composeTestRule.onNodeWithText("設定").assertIsDisplayed()

        // ヘッダーの「ユーザー名」と、入力済みの値「テストユーザー」をそれぞれ検証
        composeTestRule.onAllNodesWithText("ユーザー名").assertCountEquals(2)
        composeTestRule.onNodeWithText("テストユーザー").assertIsDisplayed()

        // ラベルではなく、入力済みの値「120」を検証
        composeTestRule.onNodeWithText("120").assertIsDisplayed()

        // 選択されているフォント名（fonts[0]）を検証
        composeTestRule.onNodeWithText(fonts[0]).assertIsDisplayed()

        // ヘッダーの「プラットフォーム」を検証
        composeTestRule.onNodeWithText("プラットフォーム").assertIsDisplayed()
    }

    /**
     * SettingScreenで初回起動時の表示をテスト
     */
    @Test
    fun settingScreen_displaysFirstStartupElements() {
        val mockViewModel = mockk<SettingScreenViewModel>(relaxed = true) {
            every { uiState } returns MutableStateFlow(
                SettingScreenUiState(
                    username = "",
                    channelId = "",
                    dailyLimit = 120,
                    selectedFont = 0,
                    platformData = listOf(),
                    isFirstStartup = true
                )
            )
        }

        val mockNavController = mockk<NavController>(relaxed = true)

        composeTestRule.setContent {
            SettingScreen(vm = mockViewModel, navController = mockNavController)
        }

        // --- 検証部分を修正 ---
        composeTestRule.onNodeWithText("設定").assertIsDisplayed()

        // "作成"というテキストはないので、"保存"というcontentDescriptionを持つアイコンを検証する
        composeTestRule.onNodeWithContentDescription("保存").assertIsDisplayed()
    }

    /**
     * SettingScreenでプラットフォームデータが表示されることをテスト
     */
    @Test
    fun settingScreen_displaysPlatformData() {
        val mockViewModel = mockk<SettingScreenViewModel>(relaxed = true) {
            every { uiState } returns MutableStateFlow(
                SettingScreenUiState(
                    username = "テストユーザー",
                    channelId = "test_channel",
                    dailyLimit = 120,
                    selectedFont = 0,
                    platformData = listOf(
                        PlatformDataTable(1, "Discord", "TestChannel", "test_key"),
                        PlatformDataTable(2, "LINE", "TestRoom", "test_line_key")
                    ),
                    isFirstStartup = false
                )
            )
        }

        val mockNavController = mockk<NavController>(relaxed = true)

        composeTestRule.setContent {
            SettingScreen(vm = mockViewModel, navController = mockNavController)
        }

        composeTestRule.onNodeWithText("Discord").assertIsDisplayed()
        composeTestRule.onNodeWithText("TestChannel").assertIsDisplayed()
        composeTestRule.onNodeWithText("LINE").assertIsDisplayed()
        composeTestRule.onNodeWithText("TestRoom").assertIsDisplayed()
    }
}

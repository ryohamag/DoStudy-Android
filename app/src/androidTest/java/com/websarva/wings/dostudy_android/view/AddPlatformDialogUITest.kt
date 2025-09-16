package com.websarva.wings.dostudy_android.view

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddPlatformDialogUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * AddPlatformDialogが正しい要素を表示することをテスト（Discord選択時）
     */
    @Test
    fun addPlatformDialog_displaysCorrectElementsForDiscord() {
        val mockOnDismiss = mockk<() -> Unit>(relaxed = true)
        val mockAddPlatform = mockk<() -> Unit>(relaxed = true)
        val mockSelectedPlatformChange = mockk<(Int) -> Unit>(relaxed = true)
        val mockOnPlatformExpandedChange = mockk<() -> Unit>(relaxed = true)
        val mockOnDismissPlatformExpanded = mockk<() -> Unit>(relaxed = true)
        val mockChannelNameChange = mockk<(String) -> Unit>(relaxed = true)
        val mockKeyChange = mockk<(String) -> Unit>(relaxed = true)

        composeTestRule.setContent {
            AddPlatformDialog(
                onDismiss = mockOnDismiss,
                addPlatform = mockAddPlatform,
                selectedPlatform = 0, // Discord
                selectedPlatformChange = mockSelectedPlatformChange,
                onPlatformExpandedChange = mockOnPlatformExpandedChange,
                onDismissPlatformExpanded = mockOnDismissPlatformExpanded,
                channelName = "test-channel",
                channelNameChange = mockChannelNameChange,
                key = "123456789",
                keyChange = mockKeyChange,
                platformExpanded = false
            )
        }

        composeTestRule.onNodeWithText("プラットフォームの追加").assertIsDisplayed()
        composeTestRule.onNodeWithText("プラットフォーム").assertIsDisplayed()
        composeTestRule.onNodeWithText("チャンネル名").assertIsDisplayed()
        composeTestRule.onNodeWithText("チャンネルID").assertIsDisplayed()
        composeTestRule.onNodeWithText("追加").assertIsDisplayed()
        composeTestRule.onNodeWithText("キャンセル").assertIsDisplayed()
    }

    /**
     * AddPlatformDialogがLINE選択時に正しい要素を表示することをテスト
     */
    @Test
    fun addPlatformDialog_displaysCorrectElementsForLine() {
        val mockOnDismiss = mockk<() -> Unit>(relaxed = true)
        val mockAddPlatform = mockk<() -> Unit>(relaxed = true)
        val mockSelectedPlatformChange = mockk<(Int) -> Unit>(relaxed = true)
        val mockOnPlatformExpandedChange = mockk<() -> Unit>(relaxed = true)
        val mockOnDismissPlatformExpanded = mockk<() -> Unit>(relaxed = true)
        val mockChannelNameChange = mockk<(String) -> Unit>(relaxed = true)
        val mockKeyChange = mockk<(String) -> Unit>(relaxed = true)

        composeTestRule.setContent {
            AddPlatformDialog(
                onDismiss = mockOnDismiss,
                addPlatform = mockAddPlatform,
                selectedPlatform = 1, // LINE
                selectedPlatformChange = mockSelectedPlatformChange,
                onPlatformExpandedChange = mockOnPlatformExpandedChange,
                onDismissPlatformExpanded = mockOnDismissPlatformExpanded,
                channelName = "test-room",
                channelNameChange = mockChannelNameChange,
                key = "test-key",
                keyChange = mockKeyChange,
                platformExpanded = false
            )
        }

        composeTestRule.onNodeWithText("プラットフォームの追加").assertIsDisplayed()
        composeTestRule.onNodeWithText("トークルーム名").assertIsDisplayed()
        composeTestRule.onNodeWithText("Key").assertIsDisplayed()
    }

    /**
     * キャンセルボタンをクリックしたときにonDismissが呼ばれることをテスト
     */
    @Test
    fun addPlatformDialog_callsOnDismissWhenCancelClicked() {
        val mockOnDismiss = mockk<() -> Unit>(relaxed = true)
        val mockAddPlatform = mockk<() -> Unit>(relaxed = true)
        val mockSelectedPlatformChange = mockk<(Int) -> Unit>(relaxed = true)
        val mockOnPlatformExpandedChange = mockk<() -> Unit>(relaxed = true)
        val mockOnDismissPlatformExpanded = mockk<() -> Unit>(relaxed = true)
        val mockChannelNameChange = mockk<(String) -> Unit>(relaxed = true)
        val mockKeyChange = mockk<(String) -> Unit>(relaxed = true)

        composeTestRule.setContent {
            AddPlatformDialog(
                onDismiss = mockOnDismiss,
                addPlatform = mockAddPlatform,
                selectedPlatform = 0,
                selectedPlatformChange = mockSelectedPlatformChange,
                onPlatformExpandedChange = mockOnPlatformExpandedChange,
                onDismissPlatformExpanded = mockOnDismissPlatformExpanded,
                channelName = "test",
                channelNameChange = mockChannelNameChange,
                key = "test",
                keyChange = mockKeyChange,
                platformExpanded = false
            )
        }

        composeTestRule.onNodeWithText("キャンセル").performClick()

        verify { mockOnDismiss() }
    }
}

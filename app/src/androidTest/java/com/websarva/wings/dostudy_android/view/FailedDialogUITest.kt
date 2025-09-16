package com.websarva.wings.dostudy_android.view

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.websarva.wings.dostudy_android.viewmodel.MainViewModel
import io.mockk.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FailedDialogUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * FailedDialogが正しいタイトルと要素を表示することをテスト
     */
    @Test
    fun failedDialog_displaysCorrectElements() {
        val mockViewModel = mockk<MainViewModel>(relaxed = true)
        every { mockViewModel.isShowAdScreen } returns false
        val mockOnDismiss = mockk<() -> Unit>(relaxed = true)

        composeTestRule.setContent {
            FailedDialog(
                onDismissRequest = mockOnDismiss,
                responseMessage = "テスト失敗メッセージ",
                vm = mockViewModel
            )
        }

        composeTestRule.onNodeWithText("何やってるんですか！").assertIsDisplayed()
        composeTestRule.onNodeWithText("テスト失敗メッセージ").assertIsDisplayed()
        composeTestRule.onNodeWithText("ごめんなさい").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("警告").assertIsDisplayed()
    }

    /**
     * responseMessageが空の場合、LoadingTextが表示されることをテスト
     */
    @Test
    fun failedDialog_showsLoadingTextWhenMessageEmpty() {
        val mockViewModel = mockk<MainViewModel>(relaxed = true)
        every { mockViewModel.isShowAdScreen } returns false
        val mockOnDismiss = mockk<() -> Unit>(relaxed = true)

        composeTestRule.setContent {
            FailedDialog(
                onDismissRequest = mockOnDismiss,
                responseMessage = "",
                vm = mockViewModel
            )
        }

        composeTestRule.onNodeWithText("何やってるんですか！").assertIsDisplayed()
        composeTestRule.onNodeWithText("Loading.").assertIsDisplayed()
        composeTestRule.onNodeWithText("ごめんなさい").assertIsDisplayed()
    }

    /**
     * 広告が表示されていない場合、ごめんなさいボタンをクリックするとonDismissRequestが呼ばれることをテスト
     */
    @Test
    fun failedDialog_callsOnDismissWhenNotShowingAd() {
        val mockViewModel = mockk<MainViewModel>(relaxed = true)
        every { mockViewModel.isShowAdScreen } returns false
        val mockOnDismiss = mockk<() -> Unit>(relaxed = true)

        composeTestRule.setContent {
            FailedDialog(
                onDismissRequest = mockOnDismiss,
                responseMessage = "テストメッセージ",
                vm = mockViewModel
            )
        }

        composeTestRule.onNodeWithText("ごめんなさい").performClick()

        verify { mockOnDismiss() }
    }
}

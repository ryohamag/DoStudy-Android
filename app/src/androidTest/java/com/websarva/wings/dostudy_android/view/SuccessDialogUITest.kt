package com.websarva.wings.dostudy_android.view

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SuccessDialogUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * SuccessDialogが正しいタイトルと要素を表示することをテスト
     */
    @Test
    fun successDialog_displaysCorrectElements() {
        val mockOnDismiss = mockk<() -> Unit>(relaxed = true)

        composeTestRule.setContent {
            SuccessDialog(
                onDismissRequest = mockOnDismiss,
                responseMessage = "テストメッセージ"
            )
        }

        composeTestRule.onNodeWithText("お疲れ様でした！").assertIsDisplayed()
        composeTestRule.onNodeWithText("テストメッセージ").assertIsDisplayed()
        composeTestRule.onNodeWithText("OK").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("完了").assertIsDisplayed()
    }

    /**
     * responseMessageが空の場合、LoadingTextが表示されることをテスト
     */
    @Test
    fun successDialog_showsLoadingTextWhenMessageEmpty() {
        val mockOnDismiss = mockk<() -> Unit>(relaxed = true)

        composeTestRule.setContent {
            SuccessDialog(
                onDismissRequest = mockOnDismiss,
                responseMessage = ""
            )
        }

        composeTestRule.onNodeWithText("お疲れ様でした！").assertIsDisplayed()
        composeTestRule.onNodeWithText("Loading.").assertIsDisplayed()
        composeTestRule.onNodeWithText("OK").assertIsDisplayed()
    }

    /**
     * OKボタンをクリックしたときにonDismissRequestが呼ばれることをテスト
     */
    @Test
    fun successDialog_callsOnDismissWhenOKClicked() {
        val mockOnDismiss = mockk<() -> Unit>(relaxed = true)

        composeTestRule.setContent {
            SuccessDialog(
                onDismissRequest = mockOnDismiss,
                responseMessage = "テストメッセージ"
            )
        }

        composeTestRule.onNodeWithText("OK").performClick()

        verify { mockOnDismiss() }
    }
}

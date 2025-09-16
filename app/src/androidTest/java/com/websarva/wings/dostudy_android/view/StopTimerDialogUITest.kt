package com.websarva.wings.dostudy_android.view

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StopTimerDialogUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * StopTimerDialogが正しいタイトルとボタンを表示することをテスト
     */
    @Test
    fun stopTimerDialog_displaysCorrectElements() {
        val mockOnDismiss = mockk<() -> Unit>(relaxed = true)
        val mockOnStop = mockk<() -> Unit>(relaxed = true)

        composeTestRule.setContent {
            StopTimerDialog(
                onDismissRequest = mockOnDismiss,
                onStopRequest = mockOnStop
            )
        }

        composeTestRule.onNodeWithText("タイマーを止めますか？").assertIsDisplayed()
        composeTestRule.onNodeWithText("止める").assertIsDisplayed()
        composeTestRule.onNodeWithText("止めない").assertIsDisplayed()
    }

    /**
     * 止めるボタンをクリックしたときにonStopRequestが呼ばれることをテスト
     */
    @Test
    fun stopTimerDialog_callsOnStopWhenStopClicked() {
        val mockOnDismiss = mockk<() -> Unit>(relaxed = true)
        val mockOnStop = mockk<() -> Unit>(relaxed = true)

        composeTestRule.setContent {
            StopTimerDialog(
                onDismissRequest = mockOnDismiss,
                onStopRequest = mockOnStop
            )
        }

        composeTestRule.onNodeWithText("止める").performClick()

        verify { mockOnStop() }
    }

    /**
     * 止めないボタンをクリックしたときにonDismissRequestが呼ばれることをテスト
     */
    @Test
    fun stopTimerDialog_callsOnDismissWhenCancelClicked() {
        val mockOnDismiss = mockk<() -> Unit>(relaxed = true)
        val mockOnStop = mockk<() -> Unit>(relaxed = true)

        composeTestRule.setContent {
            StopTimerDialog(
                onDismissRequest = mockOnDismiss,
                onStopRequest = mockOnStop
            )
        }

        composeTestRule.onNodeWithText("止めない").performClick()

        verify { mockOnDismiss() }
    }
}

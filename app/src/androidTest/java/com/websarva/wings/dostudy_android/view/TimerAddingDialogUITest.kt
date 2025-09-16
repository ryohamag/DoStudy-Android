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
class TimerAddingDialogUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * TimerAddingDialogが正しいタイトルと要素を表示することをテスト
     */
    @Test
    fun timerAddingDialog_displaysCorrectElements() {
        val mockViewModel = mockk<MainViewModel>(relaxed = true)
        val mockOnDismiss = mockk<() -> Unit>(relaxed = true)

        composeTestRule.setContent {
            TimerAddingDialog(
                onDismissRequest = mockOnDismiss,
                vm = mockViewModel
            )
        }

        composeTestRule.onNodeWithText("タイマーの追加").assertIsDisplayed()
        composeTestRule.onNodeWithText("00h 00m 00s").assertIsDisplayed()
        composeTestRule.onNodeWithText("追加").assertIsDisplayed()
        composeTestRule.onNodeWithText("キャンセル").assertIsDisplayed()
    }

    /**
     * 数字ボタンが正しく表示されることをテスト
     */
    @Test
    fun timerAddingDialog_displaysNumberButtons() {
        val mockViewModel = mockk<MainViewModel>(relaxed = true)
        val mockOnDismiss = mockk<() -> Unit>(relaxed = true)

        composeTestRule.setContent {
            TimerAddingDialog(
                onDismissRequest = mockOnDismiss,
                vm = mockViewModel
            )
        }

        // 数字ボタンの確認
        for (i in 0..9) {
            composeTestRule.onNodeWithText(i.toString()).assertIsDisplayed()
        }
        composeTestRule.onNodeWithText("00").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("backspace").assertIsDisplayed()
    }

    /**
     * キャンセルボタンをクリックしたときにonDismissRequestが呼ばれることをテスト
     */
    @Test
    fun timerAddingDialog_callsOnDismissWhenCancelClicked() {
        val mockViewModel = mockk<MainViewModel>(relaxed = true)
        val mockOnDismiss = mockk<() -> Unit>(relaxed = true)

        composeTestRule.setContent {
            TimerAddingDialog(
                onDismissRequest = mockOnDismiss,
                vm = mockViewModel
            )
        }

        composeTestRule.onNodeWithText("キャンセル").performClick()

        verify { mockOnDismiss() }
    }

    /**
     * 追加ボタンをクリックしたときにViewModelのメソッドが呼ばれることをテスト
     */
    @Test
    fun timerAddingDialog_callsViewModelMethodsWhenAddClicked() {
        val mockViewModel = mockk<MainViewModel>(relaxed = true)
        val mockOnDismiss = mockk<() -> Unit>(relaxed = true)

        composeTestRule.setContent {
            TimerAddingDialog(
                onDismissRequest = mockOnDismiss,
                vm = mockViewModel
            )
        }

        composeTestRule.onNodeWithText("追加").performClick()

        verify { mockViewModel.addTimer("000000") }
        verify { mockViewModel.updateUserData() }
        verify { mockOnDismiss() }
    }
}

@RunWith(AndroidJUnit4::class)
class NumpadButtonUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * NumpadButtonが正しくテキストを表示することをテスト
     */
    @Test
    fun numpadButton_displaysCorrectText() {
        val mockOnClick = mockk<() -> Unit>(relaxed = true)

        composeTestRule.setContent {
            NumpadButton(
                text = "5",
                onClick = mockOnClick
            )
        }

        composeTestRule.onNodeWithText("5").assertIsDisplayed()
    }

    /**
     * NumpadButtonをクリックしたときにonClickが呼ばれることをテスト
     */
    @Test
    fun numpadButton_callsOnClickWhenClicked() {
        val mockOnClick = mockk<() -> Unit>(relaxed = true)

        composeTestRule.setContent {
            NumpadButton(
                text = "5",
                onClick = mockOnClick
            )
        }

        composeTestRule.onNodeWithText("5").performClick()

        verify { mockOnClick() }
    }
}

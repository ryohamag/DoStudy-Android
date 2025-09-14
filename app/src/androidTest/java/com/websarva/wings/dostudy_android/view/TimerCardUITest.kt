package com.websarva.wings.dostudy_android.view

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.websarva.wings.dostudy_android.viewmodel.MainViewModel
import io.mockk.*
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TimerCardUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var mockViewModel: MainViewModel
    private val selectedTimerFlow = MutableStateFlow(0)

    @Before
    fun setup() {
        mockViewModel = mockk(relaxed = true)
        every { mockViewModel.selectedTimer } returns selectedTimerFlow
    }

    /**
     * 3600秒（1時間）が正しく"01:00:00"として表示されることをテスト
     */
    @Test
    fun timerCard_displaysCorrectTime_3600seconds() {
        composeTestRule.setContent {
            TimerCard(seconds = 3600, vm = mockViewModel)
        }

        composeTestRule.onNodeWithText("01:00:00").assertIsDisplayed()
    }

    /**
     * 1800秒（30分）が正しく"00:30:00"として表示されることをテスト
     */
    @Test
    fun timerCard_displaysCorrectTime_1800seconds() {
        composeTestRule.setContent {
            TimerCard(seconds = 1800, vm = mockViewModel)
        }

        composeTestRule.onNodeWithText("00:30:00").assertIsDisplayed()
    }

    /**
     * ViewModelで選択されたタイマーと一致する場合、スイッチがONになっていることをテスト
     */
    @Test
    fun timerCard_switchIsChecked_whenTimerIsSelected() {
        selectedTimerFlow.value = 3600

        composeTestRule.setContent {
            TimerCard(seconds = 3600, vm = mockViewModel)
        }

        composeTestRule.onNode(hasContentDescription("ToggleSwitch")).assertIsOn()
    }

    /**
     * ViewModelで選択されたタイマーと一致しない場合、スイッチがOFFになっていることをテスト
     */
    @Test
    fun timerCard_switchIsUnchecked_whenTimerIsNotSelected() {
        selectedTimerFlow.value = 0

        composeTestRule.setContent {
            TimerCard(seconds = 3600, vm = mockViewModel)
        }

        composeTestRule.onNode(hasContentDescription("ToggleSwitch")).assertIsOff()
    }

    /**
     * スイッチをONにした際、ViewModelのsetTimerメソッドとisTimerModeが正しく呼ばれることをテスト
     */
    @Test
    fun timerCard_callsSetTimer_whenSwitchTurnedOn() {
        composeTestRule.setContent {
            TimerCard(seconds = 1800, vm = mockViewModel)
        }

        composeTestRule.onNode(hasContentDescription("ToggleSwitch")).performClick()

        verify { mockViewModel.setTimer(1800) }
        verify { mockViewModel.isTimerMode = true }
    }

    /**
     * 選択済みのスイッチをOFFにした際、ViewModelのresetTimerメソッドが呼ばれることをテスト
     */
    @Test
    fun timerCard_callsResetTimer_whenSwitchTurnedOff() {
        selectedTimerFlow.value = 1800

        composeTestRule.setContent {
            TimerCard(seconds = 1800, vm = mockViewModel)
        }

        composeTestRule.onNode(hasContentDescription("ToggleSwitch")).performClick()

        verify { mockViewModel.resetTimer() }
    }

    /**
     * デフォルトタイマー（30分）の場合、削除ボタンが表示されないことをテスト
     */
    @Test
    fun timerCard_hideDeleteButton_forDefaultTimer_1800() {
        composeTestRule.setContent {
            TimerCard(seconds = 1800, vm = mockViewModel)
        }
        composeTestRule.onNodeWithContentDescription("Delete").assertDoesNotExist()
    }

    /**
     * デフォルトタイマー（1時間）の場合、削除ボタンが表示されないことをテスト
     */
    @Test
    fun timerCard_hideDeleteButton_forDefaultTimer_3600() {
        composeTestRule.setContent {
            TimerCard(seconds = 3600, vm = mockViewModel)
        }
        composeTestRule.onNodeWithContentDescription("Delete").assertDoesNotExist()
    }

    /**
     * デフォルトタイマー（2時間）の場合、削除ボタンが表示されないことをテスト
     */
    @Test
    fun timerCard_hideDeleteButton_forDefaultTimer_7200() {
        composeTestRule.setContent {
            TimerCard(seconds = 7200, vm = mockViewModel)
        }
        composeTestRule.onNodeWithContentDescription("Delete").assertDoesNotExist()
    }

    /**
     * デフォルトタイマー（3時間）の場合、削除ボタンが表示されないことをテスト
     */
    @Test
    fun timerCard_hideDeleteButton_forDefaultTimer_10800() {
        composeTestRule.setContent {
            TimerCard(seconds = 10800, vm = mockViewModel)
        }
        composeTestRule.onNodeWithContentDescription("Delete").assertDoesNotExist()
    }

    /**
     * カスタムタイマー（デフォルト以外）の場合、削除ボタンが表示されることをテスト
     */
    @Test
    fun timerCard_showDeleteButton_forCustomTimers() {
        composeTestRule.setContent {
            TimerCard(seconds = 900, vm = mockViewModel) // カスタムタイマー
        }

        composeTestRule.onNodeWithContentDescription("Delete").assertIsDisplayed()
    }

    /**
     * 削除ボタンをクリックした際、ViewModelのdeleteTimerメソッドが正しく呼ばれることをテスト
     */
    @Test
    fun timerCard_callsDeleteTimer_whenDeleteButtonClicked() {
        composeTestRule.setContent {
            TimerCard(seconds = 900, vm = mockViewModel)
        }

        composeTestRule.onNodeWithContentDescription("Delete").performClick()

        verify { mockViewModel.deleteTimer(900) }
    }
}

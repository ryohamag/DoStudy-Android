package com.websarva.wings.dostudy_android.view

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CircularTimerUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * CircularTimerが正しい時間フォーマットで表示されることをテスト
     */
    @Test
    fun circularTimer_displaysCorrectTimeFormat() {
        composeTestRule.setContent {
            CircularTimer(
                hour = 1,
                minute = 30,
                second = 45,
                setTimer = 3600,
                selectedFont = 0
            )
        }

        composeTestRule.onNodeWithText("01:30:45").assertIsDisplayed()
    }

    /**
     * CircularTimerがゼロパディングを正しく適用することをテスト
     */
    @Test
    fun circularTimer_appliesZeroPadding() {
        composeTestRule.setContent {
            CircularTimer(
                hour = 0,
                minute = 5,
                second = 9,
                setTimer = 1800,
                selectedFont = 0
            )
        }

        composeTestRule.onNodeWithText("00:05:09").assertIsDisplayed()
    }

    /**
     * CircularTimerがゼロ時間でも正しく表示されることをテスト
     */
    @Test
    fun circularTimer_displaysZeroTime() {
        composeTestRule.setContent {
            CircularTimer(
                hour = 0,
                minute = 0,
                second = 0,
                setTimer = 60,
                selectedFont = 0
            )
        }

        composeTestRule.onNodeWithText("00:00:00").assertIsDisplayed()
    }
}

package com.websarva.wings.dostudy_android.view

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.websarva.wings.dostudy_android.model.Room.ResultData.ResultDataTable
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ResultCardUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * ResultCardが成功時の結果を正しく表示することをテスト
     */
    @Test
    fun resultCard_displaysSuccessResult() {
        val successResult = ResultDataTable(
            id = 1,
            date = "2024-01-15",
            setTimer = "01:30:00",
            studyTime = "01:25:30",
            status = true,
            studyTitle = "数学の勉強"
        )

        composeTestRule.setContent {
            ResultCard(resultDataTable = successResult)
        }

        composeTestRule.onNodeWithText("2024-01-15").assertIsDisplayed()
        composeTestRule.onNodeWithText("数学の勉強").assertIsDisplayed()
        composeTestRule.onNodeWithText("01:30:00").assertIsDisplayed()
        composeTestRule.onNodeWithText("01:25:30").assertIsDisplayed()
        composeTestRule.onNodeWithText("Success").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("タイマー").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("勉強時間").assertIsDisplayed()
    }

    /**
     * ResultCardが失敗時の結果を正しく表示することをテスト
     */
    @Test
    fun resultCard_displaysFailedResult() {
        val failedResult = ResultDataTable(
            id = 2,
            date = "2024-01-16",
            setTimer = "02:00:00",
            studyTime = "00:45:15",
            status = false,
            studyTitle = "英語の勉強"
        )

        composeTestRule.setContent {
            ResultCard(resultDataTable = failedResult)
        }

        composeTestRule.onNodeWithText("2024-01-16").assertIsDisplayed()
        composeTestRule.onNodeWithText("英語の勉強").assertIsDisplayed()
        composeTestRule.onNodeWithText("02:00:00").assertIsDisplayed()
        composeTestRule.onNodeWithText("00:45:15").assertIsDisplayed()
        composeTestRule.onNodeWithText("Failed").assertIsDisplayed()
    }

    /**
     * ResultCardがタイマー未設定時の結果を正しく表示することをテスト
     */
    @Test
    fun resultCard_displaysNoTimerSet() {
        val noTimerResult = ResultDataTable(
            id = 3,
            date = "2024-01-17",
            setTimer = null,
            studyTime = "01:15:45",
            status = true,
            studyTitle = "プログラミング"
        )

        composeTestRule.setContent {
            ResultCard(resultDataTable = noTimerResult)
        }

        composeTestRule.onNodeWithText("2024-01-17").assertIsDisplayed()
        composeTestRule.onNodeWithText("プログラミング").assertIsDisplayed()
        composeTestRule.onNodeWithText("未設定").assertIsDisplayed()
        composeTestRule.onNodeWithText("01:15:45").assertIsDisplayed()
        composeTestRule.onNodeWithText("Success").assertIsDisplayed()
    }
}

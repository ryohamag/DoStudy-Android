package com.websarva.wings.dostudy_android.view

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.websarva.wings.dostudy_android.model.Room.ResultData.ResultDataTable
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SmallResultCardUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * SmallResultCardが正しい要素を表示することをテスト
     */
    @Test
    fun smallResultCard_displaysCorrectElements() {
        val resultData = ResultDataTable(
            id = 1,
            date = "2024-01-15",
            setTimer = "01:30:00",
            studyTime = "01:25:30",
            status = true,
            studyTitle = "数学の勉強"
        )

        composeTestRule.setContent {
            SmallResultCard(
                resultDataTable = resultData,
                modifier = Modifier
            )
        }

        composeTestRule.onNodeWithText("数学の勉強").assertIsDisplayed()
        composeTestRule.onNodeWithText("01:25:30").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("勉強時間").assertIsDisplayed()
    }

    /**
     * SmallResultCardが異なるタイトルでも正しく表示されることをテスト
     */
    @Test
    fun smallResultCard_displaysWithDifferentTitle() {
        val resultData = ResultDataTable(
            id = 2,
            date = "2024-01-16",
            setTimer = "02:00:00",
            studyTime = "00:45:15",
            status = false,
            studyTitle = "英語リスニング練習"
        )

        composeTestRule.setContent {
            SmallResultCard(
                resultDataTable = resultData,
                modifier = Modifier
            )
        }

        composeTestRule.onNodeWithText("英語リスニング練習").assertIsDisplayed()
        composeTestRule.onNodeWithText("00:45:15").assertIsDisplayed()
    }

    /**
     * SmallResultCardが長いタイトルでも正しく表示されることをテスト
     */
    @Test
    fun smallResultCard_displaysWithLongTitle() {
        val resultData = ResultDataTable(
            id = 3,
            date = "2024-01-17",
            setTimer = null,
            studyTime = "02:15:45",
            status = true,
            studyTitle = "プログラミング言語の基礎理論と実践的な応用について"
        )

        composeTestRule.setContent {
            SmallResultCard(
                resultDataTable = resultData,
                modifier = Modifier
            )
        }

        composeTestRule.onNodeWithText("プログラミング言語の基礎理論と実践的な応用について").assertIsDisplayed()
        composeTestRule.onNodeWithText("02:15:45").assertIsDisplayed()
    }
}

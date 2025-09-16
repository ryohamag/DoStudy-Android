package com.websarva.wings.dostudy_android.view

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.websarva.wings.dostudy_android.model.Room.PlatformData.PlatformDataTable
import io.mockk.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlatformCardUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * PlatformCardが正しい要素を表示することをテスト
     */
    @Test
    fun platformCard_displaysCorrectElements() {
        val platformData = PlatformDataTable(
            id = 1,
            platformName = "YouTube",
            channelName = "TestChannel",
            platformKey = "test_key"
        )
        val mockDeleteFunction = mockk<(PlatformDataTable) -> Unit>(relaxed = true)

        composeTestRule.setContent {
            PlatformCard(
                platform = platformData,
                deletePlatformData = mockDeleteFunction,
                modifier = Modifier
            )
        }

        composeTestRule.onNodeWithText("YouTube").assertIsDisplayed()
        composeTestRule.onNodeWithText("TestChannel").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Delete").assertIsDisplayed()
    }

    /**
     * 削除ボタンをクリックしたときにdeletePlatformDataが呼ばれることをテスト
     */
    @Test
    fun platformCard_callsDeleteWhenDeleteButtonClicked() {
        val platformData = PlatformDataTable(
            id = 2,
            platformName = "Twitch",
            channelName = "StreamerName",
            platformKey = "stream_key"
        )
        val mockDeleteFunction = mockk<(PlatformDataTable) -> Unit>(relaxed = true)

        composeTestRule.setContent {
            PlatformCard(
                platform = platformData,
                deletePlatformData = mockDeleteFunction,
                modifier = Modifier
            )
        }

        composeTestRule.onNodeWithContentDescription("Delete").performClick()

        verify { mockDeleteFunction(platformData) }
    }

    /**
     * PlatformCardが異なるプラットフォーム情報でも正しく表示されることをテスト
     */
    @Test
    fun platformCard_displaysWithDifferentPlatformInfo() {
        val platformData = PlatformDataTable(
            id = 3,
            platformName = "Discord",
            channelName = "MyServer",
            platformKey = "webhook_url"
        )
        val mockDeleteFunction = mockk<(PlatformDataTable) -> Unit>(relaxed = true)

        composeTestRule.setContent {
            PlatformCard(
                platform = platformData,
                deletePlatformData = mockDeleteFunction,
                modifier = Modifier
            )
        }

        composeTestRule.onNodeWithText("Discord").assertIsDisplayed()
        composeTestRule.onNodeWithText("MyServer").assertIsDisplayed()
    }
}

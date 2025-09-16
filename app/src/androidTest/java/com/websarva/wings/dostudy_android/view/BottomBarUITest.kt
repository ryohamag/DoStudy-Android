package com.websarva.wings.dostudy_android.view

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BottomBarUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * BottomBarが正しいアイコンとcontentDescriptionを表示することをテスト
     */
    @Test
    fun bottomBar_displaysCorrectElements() {
        val mockNavController = mockk<NavController>(relaxed = true)

        composeTestRule.setContent {
            BottomBar(
                navController = mockNavController,
                isStudyStarted = false
            )
        }

        composeTestRule.onNodeWithContentDescription("ホーム").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("ToDoリスト").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("モニタリング").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("設定").assertIsDisplayed()
    }

    /**
     * 勉強中でない場合、ホームボタンをクリックするとnavigateが呼ばれることをテスト
     */
    @Test
    fun bottomBar_navigatesToHomeWhenNotStudying() {
        val mockNavController = mockk<NavController>(relaxed = true)

        composeTestRule.setContent {
            BottomBar(
                navController = mockNavController,
                isStudyStarted = false
            )
        }

        composeTestRule.onNodeWithContentDescription("ホーム").performClick()

        verify { mockNavController.navigate("Home") }
    }

    /**
     * 勉強中でない場合、ToDoリストボタンをクリックするとnavigateが呼ばれることをテスト
     */
    @Test
    fun bottomBar_navigatesToToDoWhenNotStudying() {
        val mockNavController = mockk<NavController>(relaxed = true)

        composeTestRule.setContent {
            BottomBar(
                navController = mockNavController,
                isStudyStarted = false
            )
        }

        composeTestRule.onNodeWithContentDescription("ToDoリスト").performClick()

        verify { mockNavController.navigate("ToDoList") }
    }

    /**
     * 勉強中でない場合、モニタリングボタンをクリックするとnavigateが呼ばれることをテスト
     */
    @Test
    fun bottomBar_navigatesToMonitorWhenNotStudying() {
        val mockNavController = mockk<NavController>(relaxed = true)

        composeTestRule.setContent {
            BottomBar(
                navController = mockNavController,
                isStudyStarted = false
            )
        }

        composeTestRule.onNodeWithContentDescription("モニタリング").performClick()

        verify { mockNavController.navigate("Monitor") }
    }

    /**
     * 勉強中でない場合、設定ボタンをクリックするとnavigateが呼ばれることをテスト
     */
    @Test
    fun bottomBar_navigatesToSettingsWhenNotStudying() {
        val mockNavController = mockk<NavController>(relaxed = true)

        composeTestRule.setContent {
            BottomBar(
                navController = mockNavController,
                isStudyStarted = false
            )
        }

        composeTestRule.onNodeWithContentDescription("設定").performClick()

        verify { mockNavController.navigate("Settings") }
    }

    /**
     * 勉強中の場合、ボタンをクリックしてもnavigateが呼ばれないことをテスト
     */
    @Test
    fun bottomBar_doesNotNavigateWhenStudying() {
        val mockNavController = mockk<NavController>(relaxed = true)

        composeTestRule.setContent {
            BottomBar(
                navController = mockNavController,
                isStudyStarted = true
            )
        }

        composeTestRule.onNodeWithContentDescription("ホーム").performClick()
        composeTestRule.onNodeWithContentDescription("ToDoリスト").performClick()
        composeTestRule.onNodeWithContentDescription("モニタリング").performClick()
        composeTestRule.onNodeWithContentDescription("設定").performClick()

        verify(exactly = 0) { mockNavController.navigate("Home") }
    }
}

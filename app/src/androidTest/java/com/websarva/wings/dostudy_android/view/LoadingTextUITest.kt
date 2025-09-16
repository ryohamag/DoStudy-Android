package com.websarva.wings.dostudy_android.view

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoadingTextUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * LoadingTextがドット付きで表示されることをテスト
     */
    @Test
    fun loadingText_displaysWithDots() = runTest {
        composeTestRule.setContent {
            LoadingText()
        }

        // 初期状態またはドット付きの状態のいずれかが表示される
        composeTestRule.waitForIdle()

        // "Loading"で始まるテキストが存在することを確認
        val loadingExists = try {
            composeTestRule.onNodeWithText("Loading").assertExists()
            true
        } catch (e: AssertionError) {
            try {
                composeTestRule.onNodeWithText("Loading.").assertExists()
                true
            } catch (e: AssertionError) {
                try {
                    composeTestRule.onNodeWithText("Loading..").assertExists()
                    true
                } catch (e: AssertionError) {
                    composeTestRule.onNodeWithText("Loading...").assertExists()
                    true
                }
            }
        }
    }
}

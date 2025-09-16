package com.websarva.wings.dostudy_android.view

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AdBannerUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * AdBannerが正常に表示されることをテスト
     */
    @Test
    fun adBanner_displaysCorrectly() {
        composeTestRule.setContent {
            AdBanner(modifier = Modifier)
        }

        // AdBannerはAndroidViewを使用しているため、
        // 表示の確認は基本的にクラッシュしないことで確認
        composeTestRule.waitForIdle()
    }
}

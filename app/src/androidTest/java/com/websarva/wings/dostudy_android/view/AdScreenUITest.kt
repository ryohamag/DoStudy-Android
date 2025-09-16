package com.websarva.wings.dostudy_android.view

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.websarva.wings.dostudy_android.viewmodel.MainViewModel
import io.mockk.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AdScreenUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * AdScreenが正常に初期化されることをテスト
     * （AdScreenは広告ロジックを含むため、基本的な初期化のテストのみ）
     */
    @Test
    fun adScreen_initializesCorrectly() {
        val mockViewModel = mockk<MainViewModel>(relaxed = true) {
            every { isShowAdScreen } returns true
        }

        composeTestRule.setContent {
            AdScreen(vm = mockViewModel)
        }

        // AdScreenはGoogle Adsライブラリを使用するため、
        // 実際の広告表示のテストは困難
        // 基本的にクラッシュしないことを確認
        composeTestRule.waitForIdle()
    }

    /**
     * AdScreenがViewModelの状態を正しく受け取ることをテスト
     */
    @Test
    fun adScreen_receivesViewModelState() {
        val mockViewModel = mockk<MainViewModel>(relaxed = true) {
            every { isShowAdScreen } returns false
        }

        composeTestRule.setContent {
            AdScreen(vm = mockViewModel)
        }

        // ViewModelの状態が正しく参照されることを確認
        composeTestRule.waitForIdle()
        verify { mockViewModel.isShowAdScreen }
    }
}

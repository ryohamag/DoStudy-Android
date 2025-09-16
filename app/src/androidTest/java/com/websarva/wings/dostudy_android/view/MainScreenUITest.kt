package com.websarva.wings.dostudy_android.view

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.websarva.wings.dostudy_android.model.Room.ResultData.ResultDataTable
import com.websarva.wings.dostudy_android.model.Room.ToDoData.ToDoDataTable
import com.websarva.wings.dostudy_android.viewmodel.MainViewModel
import io.mockk.*
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainScreenUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    /**
     * MainScreenが基本的な要素を表示することをテスト（勉強未開始状態）
     */
    @Test
    fun mainScreen_displaysBasicElementsWhenNotStudying() {
        val mockViewModel = mockk<MainViewModel>(relaxed = true) {
            every { isStudyStarted } returns false
            every { isShowFailedDialog } returns false
            every { isShowSuccessDialog } returns false
            every { isShowStopTimerDialog } returns false
            every { isShowStudyTitleDialog } returns false
            every { isShowTimerAddingDialog } returns false
            every { isTimerMode } returns false
            every { seconds } returns MutableStateFlow(0)
            every { selectedTimer } returns MutableStateFlow(null)
            every { selectedFont } returns MutableStateFlow(0)
            every { addedTimerList } returns MutableStateFlow(emptyList()) // 空リストでシンプルに
            every { resultDataList } returns MutableStateFlow(listOf())
            every { todoList } returns MutableStateFlow(listOf())
            every { platformData } returns MutableStateFlow(listOf())
        }

        composeTestRule.setContent {
            MainScreen(
                context = LocalContext.current,
                vm = mockViewModel
            )
        }

        // UI要素の確認
        composeTestRule.onNodeWithText("00h00m00s").assertIsDisplayed()
        composeTestRule.onNodeWithText("start").assertIsDisplayed()

        // ViewModelのメソッド呼び出し確認（タイムアウトを長めに設定）
        composeTestRule.waitForIdle()

        // LaunchedEffectの実行を待つ
        composeTestRule.mainClock.advanceTimeBy(1000) // 1秒待機

        verify(timeout = 5000) { mockViewModel.getUserData() }
        verify(timeout = 5000) { mockViewModel.getToDoList() }
        verify(timeout = 5000) { mockViewModel.getPlatformData() }
    }

    /**
     * MainScreenで勉強中状態（通常モード）の表示をテスト
     */
    @Test
    fun mainScreen_displaysStudyingStateNormalMode() {
        val mockViewModel = mockk<MainViewModel>(relaxed = true) {
            every { isStudyStarted } returns true
            every { isTimerMode } returns false
            every { isShowFailedDialog } returns false
            every { isShowSuccessDialog } returns false
            every { isShowStopTimerDialog } returns false
            every { isShowStudyTitleDialog } returns false
            every { isShowTimerAddingDialog } returns false
            every { seconds } returns MutableStateFlow(3661) // 1時間1分1秒
            every { selectedTimer } returns MutableStateFlow(null)
            every { selectedFont } returns MutableStateFlow(0)
            every { addedTimerList } returns MutableStateFlow(listOf())
            every { resultDataList } returns MutableStateFlow(listOf())
            every { todoList } returns MutableStateFlow(listOf())
            every { platformData } returns MutableStateFlow(listOf())
        }

        composeTestRule.setContent {
            MainScreen(
                context = context,
                vm = mockViewModel
            )
        }

        // 経過時間表示の確認（01h01m01s）
        composeTestRule.onNodeWithText("01h01m01s").assertIsDisplayed()

        // ストップボタンの確認
        composeTestRule.onNodeWithText("stop").assertIsDisplayed()

        // タイマー追加ボタンが非表示であることを確認
        composeTestRule.onNodeWithContentDescription("Add").assertDoesNotExist()
    }

    /**
     * MainScreenで勉強中状態（タイマーモード）の表示をテスト
     */
    @Test
    fun mainScreen_displaysStudyingStateTimerMode() {
        val mockViewModel = mockk<MainViewModel>(relaxed = true) {
            every { isStudyStarted } returns true
            every { isTimerMode } returns true
            every { isShowFailedDialog } returns false
            every { isShowSuccessDialog } returns false
            every { isShowStopTimerDialog } returns false
            every { isShowStudyTitleDialog } returns false
            every { isShowTimerAddingDialog } returns false
            every { seconds } returns MutableStateFlow(300)
            every { selectedTimer } returns MutableStateFlow(1800) // 30分
            every { setTimer } returns MutableStateFlow(1800)
            every { selectedFont } returns MutableStateFlow(0)
            every { addedTimerList } returns MutableStateFlow(listOf())
            every { resultDataList } returns MutableStateFlow(listOf())
            every { todoList } returns MutableStateFlow(listOf())
            every { platformData } returns MutableStateFlow(listOf())
        }

        composeTestRule.setContent {
            MainScreen(
                context = context,
                vm = mockViewModel
            )
        }

        // CircularTimerが表示されることを確認（具体的な時間表示は CircularTimer のテストで行う）
        // スタート/ストップボタンが非表示であることを確認
        composeTestRule.onNodeWithText("start").assertDoesNotExist()
        composeTestRule.onNodeWithText("stop").assertDoesNotExist()
    }

    /**
     * MainScreenでスタートボタンをクリックしたときの動作をテスト
     */
    @Test
    fun mainScreen_showsStudyTitleDialogWhenStartClicked() {
        val mockViewModel = mockk<MainViewModel>(relaxed = true) {
            every { isStudyStarted } returns false
            every { isShowFailedDialog } returns false
            every { isShowSuccessDialog } returns false
            every { isShowStopTimerDialog } returns false
            every { isShowStudyTitleDialog } returns false
            every { isShowTimerAddingDialog } returns false
            every { isTimerMode } returns false
            every { seconds } returns MutableStateFlow(0)
            every { selectedTimer } returns MutableStateFlow(null)
            every { selectedFont } returns MutableStateFlow(0)
            every { addedTimerList } returns MutableStateFlow(listOf())
            every { resultDataList } returns MutableStateFlow(listOf())
            every { todoList } returns MutableStateFlow(listOf())
            every { platformData } returns MutableStateFlow(listOf())
        }

        composeTestRule.setContent {
            MainScreen(
                context = context,
                vm = mockViewModel
            )
        }

        composeTestRule.onNodeWithText("start").performClick()

        verify { mockViewModel.isShowStudyTitleDialog = true }
    }

    /**
     * MainScreenでストップボタンをクリックしたときの動作をテスト
     */
    @Test
    fun mainScreen_showsStopTimerDialogWhenStopClicked() {
        val mockViewModel = mockk<MainViewModel>(relaxed = true) {
            every { isStudyStarted } returns true
            every { isTimerMode } returns false
            every { isShowFailedDialog } returns false
            every { isShowSuccessDialog } returns false
            every { isShowStopTimerDialog } returns false
            every { isShowStudyTitleDialog } returns false
            every { isShowTimerAddingDialog } returns false
            every { seconds } returns MutableStateFlow(1800)
            every { selectedTimer } returns MutableStateFlow(null)
            every { selectedFont } returns MutableStateFlow(0)
            every { addedTimerList } returns MutableStateFlow(listOf())
            every { resultDataList } returns MutableStateFlow(listOf())
            every { todoList } returns MutableStateFlow(listOf())
            every { platformData } returns MutableStateFlow(listOf())
        }

        composeTestRule.setContent {
            MainScreen(
                context = context,
                vm = mockViewModel
            )
        }

        composeTestRule.onNodeWithText("stop").performClick()

        verify { mockViewModel.isShowStopTimerDialog = true }
    }

    /**
     * MainScreenでタイマー追加ボタンをクリックしたときの動作をテスト
     */
    @Test
    fun mainScreen_showsTimerAddingDialogWhenAddClicked() {
        val mockViewModel = mockk<MainViewModel>(relaxed = true) {
            every { isStudyStarted } returns false
            every { isShowFailedDialog } returns false
            every { isShowSuccessDialog } returns false
            every { isShowStopTimerDialog } returns false
            every { isShowStudyTitleDialog } returns false
            every { isShowTimerAddingDialog } returns false
            every { isTimerMode } returns false
            every { seconds } returns MutableStateFlow(0)
            every { selectedTimer } returns MutableStateFlow(null)
            every { selectedFont } returns MutableStateFlow(0)
            every { addedTimerList } returns MutableStateFlow(emptyList()) // 空リストで追加ボタンを上部に表示
            every { resultDataList } returns MutableStateFlow(listOf())
            every { todoList } returns MutableStateFlow(listOf())
            every { platformData } returns MutableStateFlow(listOf())
        }

        composeTestRule.setContent {
            MainScreen(
                context = LocalContext.current,
                vm = mockViewModel
            )
        }

        // LazyColumnを下にスクロールして追加ボタンを見つける
        composeTestRule.onRoot().performTouchInput {
            swipeUp(startY = height * 0.8f, endY = height * 0.2f)
        }

        composeTestRule
            .onNodeWithContentDescription("Add")
            .assertExists()
            .performClick()

        verify { mockViewModel.isShowTimerAddingDialog = true }
    }



    /**
     * MainScreenで失敗ダイアログが表示される状態のテスト
     */
    @Test
    fun mainScreen_showsFailedDialogWhenFailed() {
        val mockViewModel = mockk<MainViewModel>(relaxed = true) {
            every { isStudyStarted } returns false
            every { isShowFailedDialog } returns true
            every { isShowSuccessDialog } returns false
            every { isShowStopTimerDialog } returns false
            every { isShowStudyTitleDialog } returns false
            every { isShowTimerAddingDialog } returns false
            every { isShowAdScreen } returns false
            every { responseMessage } returns "テスト失敗メッセージ"
            every { seconds } returns MutableStateFlow(0)
            every { selectedTimer } returns MutableStateFlow(null)
            every { selectedFont } returns MutableStateFlow(0)
            every { addedTimerList } returns MutableStateFlow(listOf())
            every { resultDataList } returns MutableStateFlow(listOf())
            every { todoList } returns MutableStateFlow(listOf())
            every { platformData } returns MutableStateFlow(listOf())
        }

        composeTestRule.setContent {
            MainScreen(
                context = context,
                vm = mockViewModel
            )
        }

        composeTestRule.onNodeWithText("何やってるんですか！").assertIsDisplayed()
        composeTestRule.onNodeWithText("テスト失敗メッセージ").assertIsDisplayed()

        verify { mockViewModel.isShowAdScreen = true }
    }

    /**
     * MainScreenで成功ダイアログが表示される状態のテスト
     */
    @Test
    fun mainScreen_showsSuccessDialogWhenSuccessful() {
        val mockViewModel = mockk<MainViewModel>(relaxed = true) {
            every { isStudyStarted } returns false
            every { isShowFailedDialog } returns false
            every { isShowSuccessDialog } returns true
            every { isShowStopTimerDialog } returns false
            every { isShowStudyTitleDialog } returns false
            every { isShowTimerAddingDialog } returns false
            every { responseMessage } returns "テスト成功メッセージ"
            every { seconds } returns MutableStateFlow(1800)
            every { selectedTimer } returns MutableStateFlow(null)
            every { selectedFont } returns MutableStateFlow(0)
            every { addedTimerList } returns MutableStateFlow(listOf())
            every { resultDataList } returns MutableStateFlow(listOf())
            every { todoList } returns MutableStateFlow(listOf())
            every { platformData } returns MutableStateFlow(listOf())
            every { username } returns "テストユーザー"
        }

        composeTestRule.setContent {
            MainScreen(
                context = context,
                vm = mockViewModel
            )
        }

        composeTestRule.onNodeWithText("お疲れ様でした！").assertIsDisplayed()
        composeTestRule.onNodeWithText("テスト成功メッセージ").assertIsDisplayed()

        // 成功時の処理が呼ばれることを確認
        verify { mockViewModel.addResultData(true) }
        verify { mockViewModel.reset() }
    }

    /**
     * MainScreenで勉強タイトルダイアログが表示される状態のテスト
     */
    @Test
    fun mainScreen_showsStudyTitleDialogWhenRequested() {
        val mockViewModel = mockk<MainViewModel>(relaxed = true) {
            every { isStudyStarted } returns false
            every { isShowFailedDialog } returns false
            every { isShowSuccessDialog } returns false
            every { isShowStopTimerDialog } returns false
            every { isShowStudyTitleDialog } returns true
            every { isShowTimerAddingDialog } returns false
            every { studyTitle } returns "テスト勉強"
            every { seconds } returns MutableStateFlow(0)
            every { selectedTimer } returns MutableStateFlow(null)
            every { selectedFont } returns MutableStateFlow(0)
            every { addedTimerList } returns MutableStateFlow(listOf())
            every { resultDataList } returns MutableStateFlow(
                listOf(ResultDataTable(1, "2024-01-15", "01:30:00", "01:25:30", true, "数学"))
            )
            every { todoList } returns MutableStateFlow(
                listOf(ToDoDataTable(1, "英語の勉強", 1.0))
            )
            every { platformData } returns MutableStateFlow(listOf())
        }

        composeTestRule.setContent {
            MainScreen(
                context = context,
                vm = mockViewModel
            )
        }

        composeTestRule.onNodeWithText("タイトルの入力").assertIsDisplayed()
        composeTestRule.onNodeWithText("作業開始").assertIsDisplayed()
        composeTestRule.onNodeWithText("キャンセル").assertIsDisplayed()
        composeTestRule.onNodeWithText("数学").assertIsDisplayed()
        composeTestRule.onNodeWithText("英語の勉強").assertIsDisplayed()
    }

    /**
     * MainScreenでストップタイマーダイアログが表示される状態のテスト
     */
    @Test
    fun mainScreen_showsStopTimerDialogWhenRequested() {
        val mockViewModel = mockk<MainViewModel>(relaxed = true) {
            every { isStudyStarted } returns true
            every { isShowFailedDialog } returns false
            every { isShowSuccessDialog } returns false
            every { isShowStopTimerDialog } returns true
            every { isShowStudyTitleDialog } returns false
            every { isShowTimerAddingDialog } returns false
            every { seconds } returns MutableStateFlow(1800)
            every { selectedTimer } returns MutableStateFlow(null)
            every { selectedFont } returns MutableStateFlow(0)
            every { addedTimerList } returns MutableStateFlow(listOf())
            every { resultDataList } returns MutableStateFlow(listOf())
            every { todoList } returns MutableStateFlow(listOf())
            every { platformData } returns MutableStateFlow(listOf())
        }

        composeTestRule.setContent {
            MainScreen(
                context = context,
                vm = mockViewModel
            )
        }

        composeTestRule.onNodeWithText("タイマーを止めますか？").assertIsDisplayed()
        composeTestRule.onNodeWithText("止める").assertIsDisplayed()
        composeTestRule.onNodeWithText("止めない").assertIsDisplayed()
    }

    /**
     * MainScreenでタイマー追加ダイアログが表示される状態のテスト
     */
    @Test
    fun mainScreen_showsTimerAddingDialogWhenRequested() {
        val mockViewModel = mockk<MainViewModel>(relaxed = true) {
            every { isStudyStarted } returns false
            every { isShowFailedDialog } returns false
            every { isShowSuccessDialog } returns false
            every { isShowStopTimerDialog } returns false
            every { isShowStudyTitleDialog } returns false
            every { isShowTimerAddingDialog } returns true
            every { seconds } returns MutableStateFlow(0)
            every { selectedTimer } returns MutableStateFlow(null)
            every { selectedFont } returns MutableStateFlow(0)
            every { addedTimerList } returns MutableStateFlow(listOf())
            every { resultDataList } returns MutableStateFlow(listOf())
            every { todoList } returns MutableStateFlow(listOf())
            every { platformData } returns MutableStateFlow(listOf())
        }

        composeTestRule.setContent {
            MainScreen(
                context = context,
                vm = mockViewModel
            )
        }

        composeTestRule.onNodeWithText("タイマーの追加").assertIsDisplayed()
        composeTestRule.onNodeWithText("00h 00m 00s").assertIsDisplayed()
    }

    /**
     * MainScreenで異なるフォント設定での表示をテスト
     */
    @Test
    fun mainScreen_displaysWithDifferentFont() {
        val mockViewModel = mockk<MainViewModel>(relaxed = true) {
            every { isStudyStarted } returns false
            every { isTimerMode } returns false
            every { isShowFailedDialog } returns false
            every { isShowSuccessDialog } returns false
            every { isShowStopTimerDialog } returns false
            every { isShowStudyTitleDialog } returns false
            every { isShowTimerAddingDialog } returns false
            every { seconds } returns MutableStateFlow(3661) // 01h01m01s
            every { selectedTimer } returns MutableStateFlow(null)
            every { selectedFont } returns MutableStateFlow(2) // Montserrat
            every { addedTimerList } returns MutableStateFlow(listOf())
            every { resultDataList } returns MutableStateFlow(listOf())
            every { todoList } returns MutableStateFlow(listOf())
            every { platformData } returns MutableStateFlow(listOf())
        }

        composeTestRule.setContent {
            MainScreen(
                context = context,
                vm = mockViewModel
            )
        }

        // フォントが変わっても時間表示が正しいことを確認
        composeTestRule.onNodeWithText("01h01m01s").assertIsDisplayed()
    }

    /**
     * MainScreenでタイマーリストが正しく表示されることをテスト
     */
    @Test
    fun mainScreen_displaysTimerList() {
        val mockViewModel = mockk<MainViewModel>(relaxed = true) {
            every { isStudyStarted } returns false
            every { isShowFailedDialog } returns false
            every { isShowSuccessDialog } returns false
            every { isShowStopTimerDialog } returns false
            every { isShowStudyTitleDialog } returns false
            every { isShowTimerAddingDialog } returns false
            every { isTimerMode } returns false
            every { seconds } returns MutableStateFlow(0)
            every { selectedTimer } returns MutableStateFlow(null)
            every { selectedFont } returns MutableStateFlow(0)
            every { addedTimerList } returns MutableStateFlow(listOf(900)) // 1つのタイマーのみ
            every { resultDataList } returns MutableStateFlow(listOf())
            every { todoList } returns MutableStateFlow(listOf())
            every { platformData } returns MutableStateFlow(listOf())
        }

        composeTestRule.setContent {
            MainScreen(
                context = LocalContext.current,
                vm = mockViewModel
            )
        }

        // デフォルトタイマーが表示されることを確認
        composeTestRule.onNodeWithText("00:30:00").assertExists() // デフォルト30分
        composeTestRule.onNodeWithText("01:00:00").assertExists() // デフォルト1時間

        // 追加されたタイマーも確認（スクロールが必要な場合）
        composeTestRule.onNodeWithText("00:15:00").assertExists() // 追加した15分
    }
}

package com.websarva.wings.dostudy_android.view

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.websarva.wings.dostudy_android.model.Room.ToDoData.ToDoDataTable
import io.mockk.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SetTitleDialogUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * SetTitleDialogが正しいタイトルと要素を表示することをテスト
     */
    @Test
    fun setTitleDialog_displaysCorrectElements() {
        val mockOnDismiss = mockk<() -> Unit>(relaxed = true)
        val mockOnConfirm = mockk<() -> Unit>(relaxed = true)
        val mockOnTitleChange = mockk<(String) -> Unit>(relaxed = true)

        composeTestRule.setContent {
            SetTitleDialog(
                onDismissRequest = mockOnDismiss,
                studyTitle = "テストタイトル",
                onStudyTitleChange = mockOnTitleChange,
                onConfirmButtonClick = mockOnConfirm,
                titleList = listOf("履歴1", "履歴2"),
                todoList = listOf(
                    ToDoDataTable(id = 1, title = "ToDo1", position = 1.0),
                    ToDoDataTable(id = 2, title = "ToDo2", position = 2.0)
                )
            )
        }

        composeTestRule.onNodeWithText("タイトルの入力").assertIsDisplayed()
        composeTestRule.onNodeWithText("作業タイトル").assertIsDisplayed()
        composeTestRule.onNodeWithText("履歴").assertIsDisplayed()
        composeTestRule.onNodeWithText("ToDo").assertIsDisplayed()
        composeTestRule.onNodeWithText("作業開始").assertIsDisplayed()
        composeTestRule.onNodeWithText("キャンセル").assertIsDisplayed()
    }

    /**
     * TextFieldに入力した値が正しく表示されることをテスト
     */
    @Test
    fun setTitleDialog_displaysStudyTitleInTextField() {
        val mockOnDismiss = mockk<() -> Unit>(relaxed = true)
        val mockOnConfirm = mockk<() -> Unit>(relaxed = true)
        val mockOnTitleChange = mockk<(String) -> Unit>(relaxed = true)

        composeTestRule.setContent {
            SetTitleDialog(
                onDismissRequest = mockOnDismiss,
                studyTitle = "テストタイトル",
                onStudyTitleChange = mockOnTitleChange,
                onConfirmButtonClick = mockOnConfirm,
                titleList = listOf(),
                todoList = listOf()
            )
        }

        composeTestRule.onNodeWithText("テストタイトル").assertIsDisplayed()
    }

    /**
     * 作業開始ボタンをクリックしたときにonConfirmButtonClickが呼ばれることをテスト
     */
    @Test
    fun setTitleDialog_callsOnConfirmWhenStartClicked() {
        val mockOnDismiss = mockk<() -> Unit>(relaxed = true)
        val mockOnConfirm = mockk<() -> Unit>(relaxed = true)
        val mockOnTitleChange = mockk<(String) -> Unit>(relaxed = true)

        composeTestRule.setContent {
            SetTitleDialog(
                onDismissRequest = mockOnDismiss,
                studyTitle = "テスト",
                onStudyTitleChange = mockOnTitleChange,
                onConfirmButtonClick = mockOnConfirm,
                titleList = listOf(),
                todoList = listOf()
            )
        }

        composeTestRule.onNodeWithText("作業開始").performClick()

        verify { mockOnConfirm() }
    }

    /**
     * キャンセルボタンをクリックしたときにonDismissRequestが呼ばれることをテスト
     */
    @Test
    fun setTitleDialog_callsOnDismissWhenCancelClicked() {
        val mockOnDismiss = mockk<() -> Unit>(relaxed = true)
        val mockOnConfirm = mockk<() -> Unit>(relaxed = true)
        val mockOnTitleChange = mockk<(String) -> Unit>(relaxed = true)

        composeTestRule.setContent {
            SetTitleDialog(
                onDismissRequest = mockOnDismiss,
                studyTitle = "テスト",
                onStudyTitleChange = mockOnTitleChange,
                onConfirmButtonClick = mockOnConfirm,
                titleList = listOf(),
                todoList = listOf()
            )
        }

        composeTestRule.onNodeWithText("キャンセル").performClick()

        verify { mockOnDismiss() }
    }
}

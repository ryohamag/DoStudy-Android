package com.websarva.wings.dostudy_android.view

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddToDoDialogUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * AddToDoDialogが正しいタイトルと要素を表示することをテスト
     */
    @Test
    fun addToDoDialog_displaysCorrectElements() {
        val mockOnDismiss = mockk<() -> Unit>(relaxed = true)
        val mockAddToDo = mockk<(String) -> Unit>(relaxed = true)

        composeTestRule.setContent {
            AddToDoDialog(
                onDismiss = mockOnDismiss,
                addToDo = mockAddToDo
            )
        }

        composeTestRule.onNodeWithText("ToDoの追加").assertIsDisplayed()
        composeTestRule.onNodeWithText("タイトル").assertIsDisplayed()
        composeTestRule.onNodeWithText("追加").assertIsDisplayed()
        composeTestRule.onNodeWithText("キャンセル").assertIsDisplayed()
    }

    /**
     * TextFieldに入力できることをテスト
     */
    @Test
    fun addToDoDialog_allowsTextInput() {
        val mockOnDismiss = mockk<() -> Unit>(relaxed = true)
        val mockAddToDo = mockk<(String) -> Unit>(relaxed = true)

        composeTestRule.setContent {
            AddToDoDialog(
                onDismiss = mockOnDismiss,
                addToDo = mockAddToDo
            )
        }

        composeTestRule.onNodeWithText("タイトル").performTextInput("新しいToDo")
        composeTestRule.onNodeWithText("新しいToDo").assertIsDisplayed()
    }

    /**
     * キャンセルボタンをクリックしたときにonDismissが呼ばれることをテスト
     */
    @Test
    fun addToDoDialog_callsOnDismissWhenCancelClicked() {
        val mockOnDismiss = mockk<() -> Unit>(relaxed = true)
        val mockAddToDo = mockk<(String) -> Unit>(relaxed = true)

        composeTestRule.setContent {
            AddToDoDialog(
                onDismiss = mockOnDismiss,
                addToDo = mockAddToDo
            )
        }

        composeTestRule.onNodeWithText("キャンセル").performClick()

        verify { mockOnDismiss() }
    }

    /**
     * 有効なタイトルを入力して追加ボタンをクリックしたときにaddToDoが呼ばれることをテスト
     */
    @Test
    fun addToDoDialog_callsAddToDoWhenValidTitleEntered() {
        val mockOnDismiss = mockk<() -> Unit>(relaxed = true)
        val mockAddToDo = mockk<(String) -> Unit>(relaxed = true)

        composeTestRule.setContent {
            AddToDoDialog(
                onDismiss = mockOnDismiss,
                addToDo = mockAddToDo
            )
        }

        composeTestRule.onNodeWithText("タイトル").performTextInput("新しいToDo")
        composeTestRule.onNodeWithText("追加").performClick()

        verify { mockAddToDo("新しいToDo") }
        verify { mockOnDismiss() }
    }
}

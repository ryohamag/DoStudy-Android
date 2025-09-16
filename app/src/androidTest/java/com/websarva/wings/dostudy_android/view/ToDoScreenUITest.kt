package com.websarva.wings.dostudy_android.view

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.websarva.wings.dostudy_android.model.Room.ToDoData.ToDoDataTable
import com.websarva.wings.dostudy_android.viewmodel.ToDoScreenViewModel
import com.websarva.wings.dostudy_android.viewmodel.ToDoScreenUiState
import io.mockk.*
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ToDoScreenUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * ToDoScreenが基本的なUI要素を正しく表示することをテスト
     */
    @Test
    fun toDoScreen_displaysBasicElements() {
        val mockViewModel = createMockViewModel(
            uiState = ToDoScreenUiState(
                todoList = listOf(
                    ToDoDataTable(id = 1, title = "テストToDo1", position = 1.0),
                    ToDoDataTable(id = 2, title = "テストToDo2", position = 2.0)
                )
            )
        )

        composeTestRule.setContent {
            ToDoScreen(
                navController = rememberNavController(),
                vm = mockViewModel,
                onShowAddToDoDialog = {},
                onDismissAddToDoDialog = {},
                onSwapModeChange = {},
                deleteToDo = {},
                innerPadding = PaddingValues(0.dp)
            )
        }

        // トップバーのタイトル確認
        composeTestRule.onNodeWithText("ToDo").assertIsDisplayed()

        // 戻るボタン確認
        composeTestRule.onNodeWithContentDescription("戻る").assertIsDisplayed()

        // 並び替えボタン確認
        composeTestRule.onNodeWithContentDescription("並び替え").assertIsDisplayed()

        // FloatingActionButton確認
        composeTestRule.onNodeWithContentDescription("追加").assertIsDisplayed()
    }

    /**
     * ToDoリストが正しく表示されることをテスト
     */
    @Test
    fun toDoScreen_displaysToDoList() {
        val testToDos = listOf(
            ToDoDataTable(id = 1, title = "テストToDo1", position = 1.0),
            ToDoDataTable(id = 2, title = "テストToDo2", position = 2.0),
            ToDoDataTable(id = 3, title = "テストToDo3", position = 3.0)
        )

        val mockViewModel = createMockViewModel(
            uiState = ToDoScreenUiState(todoList = testToDos)
        )

        composeTestRule.setContent {
            ToDoScreen(
                navController = rememberNavController(),
                vm = mockViewModel,
                onShowAddToDoDialog = {},
                onDismissAddToDoDialog = {},
                onSwapModeChange = {},
                deleteToDo = {},
                innerPadding = PaddingValues(0.dp)
            )
        }

        // 各ToDoが表示されていることを確認
        testToDos.forEach { toDo ->
            composeTestRule.onNodeWithText(toDo.title).assertIsDisplayed()
        }

        // 削除ボタンが表示されていることを確認（通常モードの場合）
        composeTestRule.onAllNodesWithContentDescription("削除ボタン")
            .assertCountEquals(testToDos.size)
    }

    /**
     * 空のToDoリストの場合にメッセージが表示されることをテスト
     */
    @Test
    fun toDoScreen_displaysEmptyMessage_whenNoToDos() {
        val mockViewModel = createMockViewModel(
            uiState = ToDoScreenUiState(todoList = emptyList())
        )

        composeTestRule.setContent {
            ToDoScreen(
                navController = rememberNavController(),
                vm = mockViewModel,
                onShowAddToDoDialog = {},
                onDismissAddToDoDialog = {},
                onSwapModeChange = {},
                deleteToDo = {},
                innerPadding = PaddingValues(0.dp)
            )
        }

        // 空のメッセージが表示されることを確認
        composeTestRule.onNodeWithText("ToDoがまだ追加されていません").assertIsDisplayed()
    }

    /**
     * 並び替えモードでUIが正しく変更されることをテスト
     */
    @Test
    fun toDoScreen_displaysSwapModeElements() {
        val testToDos = listOf(
            ToDoDataTable(id = 1, title = "テストToDo1", position = 1.0),
            ToDoDataTable(id = 2, title = "テストToDo2", position = 2.0)
        )

        val mockViewModel = createMockViewModel(
            uiState = ToDoScreenUiState(
                todoList = testToDos,
                isSwapMode = true
            )
        )

        composeTestRule.setContent {
            ToDoScreen(
                navController = rememberNavController(),
                vm = mockViewModel,
                onShowAddToDoDialog = {},
                onDismissAddToDoDialog = {},
                onSwapModeChange = {},
                deleteToDo = {},
                innerPadding = PaddingValues(0.dp)
            )
        }

        // 並び替えモードのタイトル確認
        composeTestRule.onNodeWithText("並び替えモード").assertIsDisplayed()

        // FloatingActionButtonが非表示になることを確認
        composeTestRule.onNodeWithContentDescription("追加").assertDoesNotExist()

        // 削除ボタンが非表示になることを確認
        composeTestRule.onAllNodesWithContentDescription("削除ボタン")
            .assertCountEquals(0)
    }

    /**
     * ToDoを選択した時の色変更をテスト
     */
    @Test
    fun toDoScreen_highlightsSelectedToDos_inSwapMode() {
        val testToDos = listOf(
            ToDoDataTable(id = 1, title = "テストToDo1", position = 1.0),
            ToDoDataTable(id = 2, title = "テストToDo2", position = 2.0)
        )

        val mockViewModel = createMockViewModel(
            uiState = ToDoScreenUiState(
                todoList = testToDos,
                selectedToDos = listOf(testToDos[0]),
                isSwapMode = true
            )
        )

        composeTestRule.setContent {
            ToDoScreen(
                navController = rememberNavController(),
                vm = mockViewModel,
                onShowAddToDoDialog = {},
                onDismissAddToDoDialog = {},
                onSwapModeChange = {},
                deleteToDo = {},
                innerPadding = PaddingValues(0.dp)
            )
        }

        // 選択されたToDoが表示されていることを確認
        composeTestRule.onNodeWithText("テストToDo1").assertIsDisplayed()
        composeTestRule.onNodeWithText("テストToDo2").assertIsDisplayed()
    }

    /**
     * AddToDoDialogが表示されることをテスト
     */
    @Test
    fun toDoScreen_displaysAddToDoDialog_whenDialogStateIsTrue() {
        val mockViewModel = createMockViewModel(
            uiState = ToDoScreenUiState(isShowAddToDoDialog = true)
        )

        composeTestRule.setContent {
            ToDoScreen(
                navController = rememberNavController(),
                vm = mockViewModel,
                onShowAddToDoDialog = {},
                onDismissAddToDoDialog = {},
                onSwapModeChange = {},
                deleteToDo = {},
                innerPadding = PaddingValues(0.dp)
            )
        }

        // AddToDoDialogが表示されることを確認
        composeTestRule.onNodeWithText("ToDoの追加").assertIsDisplayed()
    }

    /**
     * FloatingActionButtonをクリックした時のコールバックをテスト
     */
    @Test
    fun toDoScreen_callsOnShowAddToDoDialog_whenFabClicked() {
        val mockOnShowAddToDoDialog = mockk<() -> Unit>(relaxed = true)
        val mockViewModel = createMockViewModel(
            uiState = ToDoScreenUiState(todoList = emptyList())
        )

        composeTestRule.setContent {
            ToDoScreen(
                navController = rememberNavController(),
                vm = mockViewModel,
                onShowAddToDoDialog = mockOnShowAddToDoDialog,
                onDismissAddToDoDialog = {},
                onSwapModeChange = {},
                deleteToDo = {},
                innerPadding = PaddingValues(0.dp)
            )
        }

        composeTestRule.onNodeWithContentDescription("追加").performClick()

        verify { mockOnShowAddToDoDialog() }
    }

    /**
     * 削除ボタンをクリックした時のコールバックをテスト
     */
    @Test
    fun toDoScreen_callsDeleteToDo_whenDeleteButtonClicked() {
        val testToDo = ToDoDataTable(id = 1, title = "テストToDo", position = 1.0)
        val mockDeleteToDo = mockk<(ToDoDataTable) -> Unit>(relaxed = true)
        val mockViewModel = createMockViewModel(
            uiState = ToDoScreenUiState(todoList = listOf(testToDo))
        )

        composeTestRule.setContent {
            ToDoScreen(
                navController = rememberNavController(),
                vm = mockViewModel,
                onShowAddToDoDialog = {},
                onDismissAddToDoDialog = {},
                onSwapModeChange = {},
                deleteToDo = mockDeleteToDo,
                innerPadding = PaddingValues(0.dp)
            )
        }

        composeTestRule.onNodeWithContentDescription("削除ボタン").performClick()

        verify { mockDeleteToDo(testToDo) }
    }

    /**
     * 並び替えボタンをクリックした時のコールバックをテスト
     */
    @Test
    fun toDoScreen_callsOnSwapModeChange_whenSwapButtonClicked() {
        val mockOnSwapModeChange = mockk<() -> Unit>(relaxed = true)
        val mockViewModel = createMockViewModel(
            uiState = ToDoScreenUiState(todoList = emptyList())
        )

        composeTestRule.setContent {
            ToDoScreen(
                navController = rememberNavController(),
                vm = mockViewModel,
                onShowAddToDoDialog = {},
                onDismissAddToDoDialog = {},
                onSwapModeChange = mockOnSwapModeChange,
                deleteToDo = {},
                innerPadding = PaddingValues(0.dp)
            )
        }

        composeTestRule.onNodeWithContentDescription("並び替え").performClick()

        verify { mockOnSwapModeChange() }
    }

    /**
     * ToDoカードをクリックした時（並び替えモード）にselectToDoが呼ばれることをテスト
     */
    @Test
    fun toDoScreen_callsSelectToDo_whenToDoCardClickedInSwapMode() {
        val testToDo = ToDoDataTable(id = 1, title = "テストToDo", position = 1.0)
        val mockViewModel = createMockViewModel(
            uiState = ToDoScreenUiState(
                todoList = listOf(testToDo),
                isSwapMode = true
            )
        )

        composeTestRule.setContent {
            ToDoScreen(
                navController = rememberNavController(),
                vm = mockViewModel,
                onShowAddToDoDialog = {},
                onDismissAddToDoDialog = {},
                onSwapModeChange = {},
                deleteToDo = {},
                innerPadding = PaddingValues(0.dp)
            )
        }

        composeTestRule.onNodeWithText("テストToDo").performClick()

        verify { mockViewModel.selectToDo(testToDo) }
    }

    // ヘルパーメソッド：モックViewModelを作成
    private fun createMockViewModel(uiState: ToDoScreenUiState): ToDoScreenViewModel {
        return mockk<ToDoScreenViewModel>(relaxed = true) {
            every { this@mockk.uiState } returns MutableStateFlow(uiState)
        }
    }
}

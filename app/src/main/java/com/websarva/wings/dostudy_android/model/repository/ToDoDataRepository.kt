package com.websarva.wings.dostudy_android.model.repository

import com.websarva.wings.dostudy_android.model.Room.ToDoData.ToDoDataDao
import com.websarva.wings.dostudy_android.model.Room.ToDoData.ToDoDataTable
import javax.inject.Inject

class ToDoDataRepository @Inject constructor(
    private val toDoDataDao: ToDoDataDao
) {
    suspend fun addToDoData(toDoData: ToDoDataTable) {
        toDoDataDao.insert(toDoData)
    }

    suspend fun deleteToDoData(toDoData: ToDoDataTable) {
        toDoDataDao.delete(toDoData)
    }

    suspend fun updateToDoData(toDoData: ToDoDataTable) {
        toDoDataDao.update(toDoData)
    }

    suspend fun getAllToDoData(): List<ToDoDataTable> {
        return toDoDataDao.getAllToDoData()
    }
}

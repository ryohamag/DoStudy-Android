package com.websarva.wings.dostudy_android.model.repository

import com.websarva.wings.dostudy_android.model.Room.PlatformData.PlatformDataDao
import com.websarva.wings.dostudy_android.model.Room.PlatformData.PlatformDataTable
import com.websarva.wings.dostudy_android.model.Room.ResultData.ResultDataDao
import com.websarva.wings.dostudy_android.model.Room.ResultData.ResultDataTable
import com.websarva.wings.dostudy_android.model.Room.ToDoData.ToDoDataDao
import com.websarva.wings.dostudy_android.model.Room.ToDoData.ToDoDataTable
import com.websarva.wings.dostudy_android.model.Room.UserData.UserDataDao
import com.websarva.wings.dostudy_android.model.Room.UserData.UserDataTable
import javax.inject.Inject

class Repository @Inject constructor(
    private val resultDataDao: ResultDataDao,
    private val userDataDao: UserDataDao,
    private val toDoDataDao: ToDoDataDao,
    private val platformDataDao: PlatformDataDao
) {
    // ResultDataDaoを通じてデータベースにアクセスするメソッド
    suspend fun insertResultData(resultData: ResultDataTable) {
        resultDataDao.insert(resultData)
    }

    suspend fun getAllResultData(): List<ResultDataTable> {
        return resultDataDao.getAllResultData()
    }

    suspend fun deleteResultData(resultData: ResultDataTable) {
        resultDataDao.delete(resultData)
    }

    // UserDataDaoを通じてデータベースにアクセスするメソッド
    suspend fun insertUserData(userData: UserDataTable) {
        userDataDao.insert(userData)
    }

    suspend fun getCurrentUser(): UserDataTable? {
        return userDataDao.getCurrentUser()
    }

    suspend fun updateUserData(userData: UserDataTable) {
        userDataDao.update(userData)
    }

    suspend fun deleteUserData(userData: UserDataTable) {
        userDataDao.delete(userData)
    }

    // ToDoDataDaoを通じてデータベースにアクセスするメソッド
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

    // PlatformDataDaoを通じてデータベースにアクセスするメソッド
    suspend fun insertPlatformData(platformData: PlatformDataTable) {
        platformDataDao.insert(platformData)
    }

    suspend fun updatePlatformData(platformData: PlatformDataTable) {
        platformDataDao.update(platformData)
    }

    suspend fun deletePlatformData(platformData: PlatformDataTable) {
        platformDataDao.delete(platformData)
    }

    suspend fun getAllPlatformData(): List<PlatformDataTable> {
        return platformDataDao.getAllPlatformData()
    }
}
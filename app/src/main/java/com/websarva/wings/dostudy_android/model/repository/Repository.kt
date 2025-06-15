package com.websarva.wings.dostudy_android.model.repository

import com.websarva.wings.dostudy_android.model.Room.ResultData.ResultDataDao
import com.websarva.wings.dostudy_android.model.Room.ResultData.ResultDataTable
import com.websarva.wings.dostudy_android.model.Room.UserData.UserDataDao
import com.websarva.wings.dostudy_android.model.Room.UserData.UserDataTable
import javax.inject.Inject

class Repository @Inject constructor(
    private val resultDataDao: ResultDataDao,
    private val userDataDao: UserDataDao,
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
}
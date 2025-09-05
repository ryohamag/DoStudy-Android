package com.websarva.wings.dostudy_android.model.repository

import com.websarva.wings.dostudy_android.model.Room.UserData.UserDataDao
import com.websarva.wings.dostudy_android.model.Room.UserData.UserDataTable
import javax.inject.Inject

class UserDataRepository @Inject constructor(
    private val userDataDao: UserDataDao
) {
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

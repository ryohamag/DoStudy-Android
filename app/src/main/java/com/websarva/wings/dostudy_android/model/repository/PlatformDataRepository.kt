package com.websarva.wings.dostudy_android.model.repository

import com.websarva.wings.dostudy_android.model.Room.PlatformData.PlatformDataDao
import com.websarva.wings.dostudy_android.model.Room.PlatformData.PlatformDataTable
import javax.inject.Inject

class PlatformDataRepository @Inject constructor(
    private val platformDataDao: PlatformDataDao
) {
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

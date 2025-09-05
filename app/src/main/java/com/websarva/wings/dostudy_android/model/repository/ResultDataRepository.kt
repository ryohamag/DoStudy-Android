package com.websarva.wings.dostudy_android.model.repository

import com.websarva.wings.dostudy_android.model.Room.ResultData.ResultDataDao
import com.websarva.wings.dostudy_android.model.Room.ResultData.ResultDataTable
import javax.inject.Inject

class ResultDataRepository @Inject constructor(
    private val resultDataDao: ResultDataDao
) {
    suspend fun insertResultData(resultData: ResultDataTable) {
        resultDataDao.insert(resultData)
    }

    suspend fun getAllResultData(): List<ResultDataTable> {
        return resultDataDao.getAllResultData()
    }

    suspend fun deleteResultData(resultData: ResultDataTable) {
        resultDataDao.delete(resultData)
    }
}

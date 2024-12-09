package com.websarva.wings.dostudy_android.functions

import android.content.Context
import com.websarva.wings.dostudy_android.Room.UserDataTable
import com.websarva.wings.dostudy_android.Room.UserRoomDataBase

suspend fun createUserData(
    context: Context,
    userName: String,
    channelId: String
) {
    val db = UserRoomDataBase.getDatabase(context)
    val userDao = db.userDataDao()

    val newData = UserDataTable(username = userName, channelId = channelId)
    userDao.insert(newData)
}
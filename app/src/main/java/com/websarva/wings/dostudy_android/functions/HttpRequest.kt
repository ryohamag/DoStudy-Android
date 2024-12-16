package com.websarva.wings.dostudy_android.functions

import android.util.Log
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.websarva.wings.dostudy_android.viewmodels.MainScreenViewModel
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

data class UserData(
    val channelid: String,
    val name: String,
    val close: Boolean,
    val realtimer_seconds: Int
)

data class ApiResponse(
    @SerializedName("statusCode") val statusCode: Int,
    @SerializedName("message") val message: String
)

fun httpRequest(
    channelId: String,
    username: String,
    status: Boolean,
    seconds: Int,
    vm: MainScreenViewModel
) {
    val userData = UserData(channelId, username, status, seconds)
    val jsonString = Gson().toJson(userData) // Gson ライブラリを使用して JSON に変換

    val requestBody = jsonString.toRequestBody("application/json".toMediaTypeOrNull())

    val request = Request.Builder()
        .url("https://6fqsnu3hec.execute-api.ap-northeast-1.amazonaws.com/kouno")
        .post(requestBody)
        .build()

    val client = OkHttpClient()
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {
            val responseBody = response.body?.string()
            val message = extractMessageFromJson(responseBody ?: "")
            vm.responseMessage = message ?: "勉強してください！"
        }
    })
}

fun extractMessageFromJson(jsonString: String): String? {
    return try {
        val apiResponse = Gson().fromJson(jsonString, ApiResponse::class.java)
        apiResponse.message
    } catch (e: Exception) {
        Log.e("JSON Parsing Error", "Error parsing JSON: ${e.message}", e)
        null // エラーが発生した場合は null を返す
    }
}
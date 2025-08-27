package com.websarva.wings.dostudy_android.functions

import android.util.Log
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.websarva.wings.dostudy_android.model.Room.PlatformData.PlatformDataTable
import com.websarva.wings.dostudy_android.viewmodel.MainViewModel
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

data class CloseData(
    val realtimer_seconds: Int,
    val title: String,
    val close: Boolean = false,
    val channelid: List<Map<String, String>>,
    val name: String,
)

data class StartData(
    val channelid: List<Map<String, String>>,
    val name: String,
    val title: String
)

data class ApiResponse(
    @SerializedName("statusCode") val statusCode: Int,
    @SerializedName("message") val message: String
)

fun httpRequest(
    platformDataList: List<PlatformDataTable>,
    username: String,
    status: Boolean,
    seconds: Int,
    vm: MainViewModel,
    mode: String = "close"
) {
    val channelDataList = platformDataList.map { platformData ->
        mapOf(platformData.platformName.lowercase() to platformData.platformKey)
    }

    val userData: Any = when (mode) {
        "close" -> CloseData(seconds, vm.studyTitle, status, channelDataList, username)
        "start" -> StartData(channelDataList, username, vm.studyTitle)
        else -> {
            Log.e("HttpRequest", "Invalid mode: $mode")
            return
        }
    }

    val jsonString = Gson().toJson(userData) //JSONに変換

    Log.d("HttpRequest", "JSON String: $jsonString")

    val requestBody = jsonString.toRequestBody("application/json".toMediaTypeOrNull())

    val request = Request.Builder()
        .url("https://6fqsnu3hec.execute-api.ap-northeast-1.amazonaws.com/kouno/${mode}")
        .post(requestBody)
        .build()

    val client = OkHttpClient()
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {
            val responseBody = response.body?.string()
            Log.d("HttpRequest", "Response: $responseBody")
            val message = extractMessageFromJson(responseBody ?: "")
            vm.responseMessage = message ?: "勉強してください！"
        }
    })
}

//JSONからメッセージを取り出す
fun extractMessageFromJson(jsonString: String): String? {
    return try {
        Log.d("JSON Parsing", "Parsing JSON: $jsonString")
        val apiResponse = Gson().fromJson(jsonString, ApiResponse::class.java)
        apiResponse.message
    } catch (e: Exception) {
        Log.e("JSON Parsing Error", "Error parsing JSON: ${e.message}", e)
        null
    }
}
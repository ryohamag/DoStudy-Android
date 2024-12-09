package com.websarva.wings.dostudy_android.functions

import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException


fun httpRequest(
    channelId: String,
    username: String
) {
    val jsonString = """
    {
        "channelid": "$channelId",
        "name": "$username"
    }
""" // 送信するJSON文字列

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
            Log.d("HTTP Response", "Response: ${response.body?.string()}")
        }
    })
}
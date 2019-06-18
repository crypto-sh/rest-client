package com.github.library.requestMethod

import android.os.Handler
import android.os.Looper

import com.github.library.helper.ResultHandler
import com.github.library.enums.AuthType
import com.github.library.enums.ErrorCode
import com.github.library.enums.RequestBodyType
import com.github.library.helper.General
import com.github.library.model.AuthModel
import com.github.library.oauth2.OAuth2Client
import com.github.library.response.ResponseFileHandler
import com.github.library.response.ResponseTextHandler
import com.github.library.utils.RequestBodyProgressive
import com.github.library.utils.RequestParams

import java.io.IOException
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response

class PUT : BaseMethod() {

    companion object {

        fun noAuth(
                client: OkHttpClient,
                url: String,
                tag: String?,
                authModel: AuthModel,
                params: RequestParams,
                responder: ResultHandler) {
            try {
                val requestBody: RequestBody
                if (params.type === RequestBodyType.MultiPart) {
                    requestBody = RequestBodyProgressive(params.requestForm) { bytesWritten, contentLength ->
                        Handler(Looper.getMainLooper()).post {
                            var percent = bytesWritten.toDouble() / contentLength.toDouble()
                            percent = Math.floor(percent * 100) / 1
                            responder.onProgress(percent, bytesWritten, contentLength)
                        }
                    }
                } else {
                    requestBody = params.requestForm
                }
                val request = Request.Builder()
                        .url(url)
                        .tag(tag)
                        .addHeader("cache-control", "no-cache")
                        .addHeader("os", "android")
                        .put(requestBody)

                when (responder) {
                    is ResponseTextHandler -> {
                        request.addHeader("Content-Type", "application/text")
                        request.addHeader("Accept", "application/text")
                    }
                    is ResponseFileHandler -> {
                        request.addHeader("Content-Type", "application/x-www-form-urlencoded")
                        request.addHeader("Accept", "application/octet-stream")
                    }
                    else -> {
                        request.addHeader("Content-Type", "application/json")
                        request.addHeader("Accept", "application/json")
                    }
                }

                if (authModel.authType === AuthType.BASIC_AUTH) {
                    request.addHeader("Authorization", String.format("Basic %", authModel.basicAuthorization))
                }

                val headers = authModel.headers
                for (index in 0 until headers!!.size) {
                    try {
                        val key = headers.keyAt(index)
                        val value = headers[key]
                        if (value != null) {
                            if (!General.stringIsEmptyOrNull(key) && !General.stringIsEmptyOrNull(value)) {
                                request.addHeader(key, value)
                            }
                        }
                    } catch (e: Exception) {
                        Handler(Looper.getMainLooper()).post { responder.onFailure(url, ErrorCode.RuntimeException) }
                    }

                }
                client.newCall(request.build()).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Handler(Looper.getMainLooper()).post { responder.onFailure(url, ErrorCode.ServerConnectionError) }
                    }

                    override fun onResponse(call: Call, response: Response) {
                        responder.onResultHandler(response)
                    }
                })
            } catch (ex: Exception) {
                Handler(Looper.getMainLooper()).post { responder.onFailure(url, ErrorCode.RuntimeException) }
            }

        }

        fun basicAuth(
                client: OkHttpClient,
                url: String,
                tag: String?,
                authModel: AuthModel,
                params: RequestParams,
                responder: ResultHandler) {
            try {
                val headers = authModel.headers
                val auth = OAuth2Client(client, headers!!, authModel)
                auth.requestAccessToken { response ->
                    if (response.isSuccessful) {
                        if (authModel.authType == AuthType.OAUTH2_AUTH) {
                            headers["Authorization"] = String.format("Bearer %s", response.accessToken)
                        }
                        authModel.headers = headers
                        noAuth(client, url, tag, authModel, params, responder)
                    } else {
                        Handler(Looper.getMainLooper()).post { responder.onFailure(url, ErrorCode.AuthorizationException) }
                    }
                }
            } catch (ex: Exception) {
                Handler(Looper.getMainLooper()).post { responder.onFailure(url, ErrorCode.RuntimeException) }
            }

        }
    }
}

package com.github.library

import android.content.Context
import android.net.ConnectivityManager
import androidx.collection.ArrayMap
import com.github.library.enums.AuthType
import com.github.library.helper.LogHelper
import com.github.library.model.AuthModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException
import java.lang.Exception
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit


abstract class BaseClient {

    val log = LogHelper(this::class.java)

    var appContext: WeakReference<Context>? = null

    var clientId: String = ""
    var clientSecret: String = ""
    var site: String = ""

    var token: String = ""

    var grantType: String = ""

    var username: String = ""
    var password: String = ""

    var baseUrl: String = ""

    var authType: AuthType = AuthType.NO_AUTH

    var headers: ArrayMap<String, String>? = null

    var timeMilliSecond = 60

    var debugEnable = true

    val authModel: AuthModel get() {
            val auth = AuthModel()
            auth.clientId = this.clientId
            auth.clientSecret = this.clientSecret
            auth.site = this.site
            auth.token = this.token
            auth.grantType = this.grantType
            auth.username = this.username
            auth.password = this.password
            auth.authType = this.authType
            auth.headers = this.headers
            return auth
        }

    private class LoggingInterceptor : Interceptor {

        internal var logHelper = LogHelper(RestClient::class.java)

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val t1 = System.nanoTime()
            logHelper.d(String.format("Sending request ${request.url()} on ${chain.connection()}%n${request.headers()}"))
            val response = chain.proceed(request)
            val t2 = System.nanoTime()
            logHelper.d(String.format("Received response for ${response.request().url()} in ${(t2 - t1) / 1e6} %n ${response.headers()}"))
            return response
        }
    }

    fun checkNetworkConnection(context: Context?): Boolean {
        context ?: return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nwInfo = connectivityManager.activeNetworkInfo ?: return false
        return nwInfo.isConnected
    }

    companion object {

        private var instance: OkHttpClient? = null

        fun getClient(timeOut: Int, enableDebug: Boolean): OkHttpClient? {
            if (instance == null) {
                instance = OkHttpClient()
                        .newBuilder()
                        .connectTimeout(timeOut.toLong(), TimeUnit.SECONDS)

                        .build()

                if (enableDebug) {
                    instance!!.newBuilder()
                            .addInterceptor(LoggingInterceptor())
                            .build()
                }
            }
            return instance
        }
    }

    fun cancelAllRequest() {
        for (call in getClient(timeMilliSecond, debugEnable)?.dispatcher()?.runningCalls()!!) {
            call.cancel()
        }
    }

    fun cancelCallWithTag(tag: String) {
        for (call in getClient(timeMilliSecond, debugEnable)?.dispatcher()?.runningCalls()!!) {
            try {
                if (call.request().tag()?.equals(tag)!!) {
                    call.cancel()
                }
            } catch (e: Exception) {
                log.e(e = e)
            }
        }
    }

}

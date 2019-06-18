package com.github.library.response

import android.os.Handler
import android.os.Looper

import java.io.UnsupportedEncodingException

import com.github.library.helper.ResultHandler
import com.github.library.enums.ErrorCode
import okio.Buffer
import java.nio.charset.Charset


/**
 * Created by alishatergholi on 12/16/17.
 */
abstract class ResponseTextHandler : ResultHandler() {

    override fun onSuccess(url: String, result: Buffer) {
        try {
            val value = result.readString(Charset.forName("UTF8")).toString()
            Handler(Looper.getMainLooper()).post { onSuccess(value) }
        } catch (e: UnsupportedEncodingException) {
            this.onFailure(url, ErrorCode.UnsupportedEncodingException)
        }

    }

    override fun onProgress(percent: Double, bytesWritten: Long, totalSize: Long) {
        super.onProgress(percent, bytesWritten, totalSize)
        Handler(Looper.getMainLooper()).post { onProgress(percent, bytesWritten, totalSize) }
    }

    override fun onFailure(url: String, errorCode: ErrorCode) {
        Handler(Looper.getMainLooper()).post { onFailure(errorCode.code, errorCode.description) }
    }

    protected abstract fun onSuccess(result: String)

    abstract fun onFailure(errorCode: Int, errorMsg: String)


}

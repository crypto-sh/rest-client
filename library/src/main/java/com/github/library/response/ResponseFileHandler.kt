package com.github.library.response

import android.os.Handler
import android.os.Looper

import com.github.library.helper.ResultHandler
import com.github.library.enums.ErrorCode
import okio.Buffer


/**
 * Created by alishatergholi on 12/16/17.
 */
abstract class ResponseFileHandler : ResultHandler() {

    override fun onSuccess(url: String, result: Buffer) {
        Handler(Looper.getMainLooper()).post { onSuccess(result.readByteArray()) }
    }

    override fun onProgress(percent: Double, bytesWritten: Long, totalSize: Long) {
        super.onProgress(percent, bytesWritten, totalSize)
        Handler(Looper.getMainLooper()).post { onProgress(percent, bytesWritten, totalSize) }
    }

    override fun onFailure(url: String, errorCode: ErrorCode) {
        Handler(Looper.getMainLooper()).post { onFailure(errorCode.code, errorCode.description) }
    }

    protected abstract fun onSuccess(result: ByteArray)

    protected abstract fun onFailure(errorCode: Int, errorMsg: String)


}
package com.github.library.helper


import com.github.library.enums.ErrorCode
import okhttp3.Response
import okio.Buffer
import java.io.IOException
import java.util.concurrent.TimeUnit


/**
 * Created by alishatergholi on 12/16/17.
 */
abstract class ResultHandler {

    fun onResultHandler(response: Response) {
        val url = response.request().url().url().toString()
        if (response.body() != null && response.isSuccessful) {
            try {
                val body = response.body()
                val source = body!!.source()
                source.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
                val buffer = source.buffer()
                this.onSuccess(url, buffer.clone())
            } catch (e: IOException) {
                this.onFailure(url, ErrorCode.IOException)
            }
        } else {
            this.onFailure(url, ErrorCode.parse(response.code()))
        }
    }

    protected abstract fun onSuccess(url: String, result: Buffer)

    open fun onProgress(percent: Double, bytesWritten: Long, totalSize: Long) {

    }

    abstract fun onFailure(url: String, errorCode: ErrorCode)

    companion object {

        protected fun calcTime(startTime: Long?): String {
            val duration = timeMillisecond - startTime!!
            return TimeUnit.MILLISECONDS.toSeconds(duration).toString() + "." + (TimeUnit.MILLISECONDS.toMillis(duration) % 1000).toString().substring(0, 1)
        }

        private val timeMillisecond: Long
            get() = System.currentTimeMillis()

        protected fun calcFileSize(result: ByteArray): String {
            val fileSize: Double = result.size.toDouble()//in Bytes
            val modifiedFileSize: String = if (fileSize < 1024) {
                "$fileSize B"
            } else if (fileSize > 1024 && fileSize < 1024 * 1024) {
                (Math.round(fileSize / 1024 * 100.0) / 100.0).toString() + " KB"
            } else {
                (Math.round(fileSize / (1024 * 1204) * 100.0) / 100.0).toString() + " MB"
            }
            return modifiedFileSize
        }
    }
}

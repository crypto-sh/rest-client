package com.github.library.response


import android.os.Handler
import android.os.Looper
import com.github.library.enums.ErrorCode
import com.github.library.helper.ResultHandler
import com.google.gson.Gson
import okio.Buffer
import org.json.JSONException
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset


/**
 * Created by alishatergholi on 12/16/17.
 */
abstract class ResponseJsonHandler<T>(private val cls: Class<T>) : ResultHandler() {

    private var data = ""

    override fun onSuccess(url: String, result: Buffer) {
        try {
            try {
                data = result.readString(Charset.forName("UTF8")).toString()
                val response = Gson().fromJson(data,cls)
                Handler(Looper.getMainLooper()).post { onSuccess(response) }
            } catch (e: JSONException) {
                Handler(Looper.getMainLooper()).post { onFailure(url, ErrorCode.ParseDataException) }
            }

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

    protected abstract fun onSuccess(result: T)

    protected abstract fun onFailure(errorCode: Int, errorMsg: String)


}

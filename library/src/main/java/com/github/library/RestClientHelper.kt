package com.github.library

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap

import com.github.library.helper.LogHelper
import com.github.library.requestMethod.BaseMethod


import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.lang.ref.WeakReference

object RestClientHelper {

    private val logHelper = LogHelper(BaseMethod::class.java)

    fun getMimeType(context: WeakReference<Context>, url: Uri): String? {
        var type: String? = null
        try {
            type = if (url.scheme == ContentResolver.SCHEME_CONTENT) {
                context.get()?.contentResolver?.getType(url)
            } else {
                val fileExtension = MimeTypeMap.getFileExtensionFromUrl(url.toString())
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase())
            }
        } catch (e: Exception) {
            logHelper.e("getMimeType", e)
        }

        return type
    }

    fun getFileName(filepath: String): String {

        try {
            return filepath.substring(filepath.lastIndexOf("/") + 1)
        } catch (e: Exception) {
            logHelper.e("getFileName", e)
            return ""
        }

    }

    @Throws(IOException::class)
    fun imageToBytes(file: File): ByteArray {
        val bytesArray = ByteArray(file.length().toInt())
        val fis = FileInputStream(file)
        fis.read(bytesArray)
        fis.close()
        return bytesArray
    }
}

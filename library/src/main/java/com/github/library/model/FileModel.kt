package com.github.library.model

import android.content.Context
import android.net.Uri

import com.github.library.RestClientHelper

import java.io.File
import java.io.IOException
import java.lang.ref.WeakReference

import okhttp3.MediaType

class FileModel {

    var file        : ByteArray? = null
    var fileName    : String
    var mimeType    : MediaType? = null

    @Throws(IOException::class)
    constructor(context: Context, file: File) {
        val weakContext = WeakReference(context)
        this.file       = RestClientHelper.imageToBytes(file)
        this.fileName   = RestClientHelper.getFileName(file.absolutePath)
        RestClientHelper.getMimeType(weakContext, Uri.fromFile(file)).also {
            this.mimeType   = MediaType.parse(it!!)
        }

    }

    constructor(file: ByteArray, mimeType: MediaType, fileName: String) {
        this.file = file
        this.mimeType = mimeType
        this.fileName = fileName
    }
}

package com.github.library.utils


import com.github.library.enums.RequestBodyType
import com.github.library.helper.LogHelper
import com.github.library.helper.General
import com.github.library.model.FileModel

import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

import java.util.Objects

import okhttp3.FormBody
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody


/**
 * Created by alishatergholi on 2/21/18.
 */
class RequestParams {

    private var plainText: String? = null

    private var contentType: String? = null

    private var binaryInput: ByteArray? = null

    private val params = LinkedHashMap<String, Any>()

    private val fileParams = HashMap<String, FileModel>()

    private var paramsArray: HashMap<String, List<Any>> = HashMap()

//    private var paramsArray: LinkedHashMap<String, List<Any>>? = LinkedHashMap()
//        set(paramsArray) {
//            if (paramsArray == null) {
//                return
//            }
//            field = paramsArray
//        }

    var type: RequestBodyType? = null
        private set

    private val logHelper = LogHelper(RequestBody::class.java)

    //for post File You should choose Multipart Type
    val requestForm: RequestBody
        get() {
            if (fileParams.size > 0) {
                type = RequestBodyType.MultiPart
            }
            when (type) {
                RequestBodyType.RawTEXTPlain -> return RequestBody.create(MediaType.parse("text/plain; charset=utf-8"), plainText!!)
                RequestBodyType.RawJSON -> {
                    val `object` = JSONObject()
                    try {
                        for (key in params.keys) {
                            `object`.put(key, params[key])
                        }
                        for (key in paramsArray.keys) {
                            for (item in Objects.requireNonNull<List<Any>>(paramsArray.get(key))) {
                                `object`.put(key, item)
                            }
                        }
                    } catch (e: JSONException) {
                        logHelper.e(e)
                    }

                    return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), `object`.toString())
                }
                RequestBodyType.Binary -> return RequestBody.create(MediaType.parse(contentType!!), binaryInput!!)
                RequestBodyType.MultiPart -> {
                    val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
                    for (key in params.keys) {
                        builder.addFormDataPart(key, Objects.requireNonNull<Any>(params[key]) as String)
                    }
                    for (key in paramsArray.keys) {
                        for (item in Objects.requireNonNull<List<Any>>(paramsArray[key])) {
                            builder.addFormDataPart(key, item as String)
                        }
                    }
                    val fileParams = fileParams
                    for (key in fileParams.keys) {
                        val uploadFile = fileParams[key]
                        if (uploadFile != null) {
                            builder.addFormDataPart(key, uploadFile.fileName, RequestBody.create(uploadFile.mimeType, uploadFile.file!!))
                        }
                    }
                    return builder.build()
                }
                RequestBodyType.FormData, RequestBodyType.FormUrlEncode -> {
                    val formBody = FormBody.Builder()
                    for (key in params.keys) {

                        formBody.addEncoded(key, params[key].toString())
                    }
                    for (key in this.paramsArray.keys) {
                        for (item in Objects.requireNonNull<List<Any>>(this.paramsArray[key])) {
                            formBody.addEncoded(key, item.toString())
                        }
                    }
                    return formBody.build()
                }
                else -> {
                    val formBody = FormBody.Builder()
                    for (key in params.keys) {
                        formBody.addEncoded(key, params[key].toString())
                    }
                    for (key in this.paramsArray.keys) {
                        for (item in Objects.requireNonNull<List<Any>>(this.paramsArray[key])) {
                            formBody.addEncoded(key, item.toString())
                        }
                    }
                    return formBody.build()
                }
            }
        }

    constructor() {
        this.type = RequestBodyType.FormUrlEncode
    }

    constructor(type: RequestBodyType) {
        this.type = type
    }

    fun put(plainText: String) {
        this.plainText = plainText
    }

    fun put(contentType: String, binaryInput: ByteArray) {
        this.contentType = contentType
        this.binaryInput = binaryInput
    }

    fun put(key: String, value: Any) {
        if (General.stringIsEmptyOrNull(key)) {
            return
        }
        params[key] = value
    }

    fun put(key: String, value: String) {
        if (General.stringIsEmptyOrNull(key) || General.stringIsEmptyOrNull(value)) {
            return
        }
        params[key] = value
    }

    fun put(key: String, value: Int?) {
        if (General.stringIsEmptyOrNull(key) || value == null) {
            return
        }
        params[key] = value
    }

    fun put(key: String, value: Long?) {
        if (General.stringIsEmptyOrNull(key) || value == null) {
            return
        }
        params[key] = value
    }

    fun put(key: String, value: Float?) {
        if (General.stringIsEmptyOrNull(key) || value == null) {
            return
        }
        params[key] = value
    }

    fun put(key: String, value: Double?) {
        if (General.stringIsEmptyOrNull(key) || value == null) {
            return
        }
        params[key] = value
    }

    fun put(key: String, value: FileModel?) {
        if (General.stringIsEmptyOrNull(key) || value == null) {
            return
        }
        fileParams[key] = value
    }

    fun put(key: String, value: Boolean) {
        if (General.stringIsEmptyOrNull(key)) {
            return
        }
        params[key] = value
    }

    fun put(key: String, value: JSONObject?) {
        if (General.stringIsEmptyOrNull(key) || value == null) {
            return
        }
        params[key] = value
    }

    fun put(key: String, values: MutableList<Any>?) {
        if (General.stringIsEmptyOrNull(key) || values == null) {
            return
        }
        if (this.paramsArray[key] != null) {
            val items = this.paramsArray[key]
            if (items != null)
                values.addAll(items)
        }
        this.paramsArray[key] = values
    }

    fun add(key: String, values: MutableList<Any>?) {
        if (General.stringIsEmptyOrNull(key) || values == null) {
            return
        }
        if (this.paramsArray[key] != null) {
            values.addAll(this.paramsArray[key]!!)
        }
        this.paramsArray[key] = values
    }

    fun putContent(key: String, values: List<Any>?) {
        if (General.stringIsEmptyOrNull(key) || values == null) {
            return
        }
        val items = ArrayList<Any>()
        if (this.paramsArray[key] != null) {
            items.addAll(this.paramsArray[key]!!)
        }
        for (item in values) {
            items.add(item.toString())
        }
        this.paramsArray[key] = items
    }

    fun add(key: String, value: String?) {
        if (General.stringIsEmptyOrNull(key) || value == null) {
            return
        }
        var values: List<Any> = arrayListOf()
        if (this.paramsArray[key] != null) {
            values = this.paramsArray[key] as List<Any>
            value.plus(value)
        } else {
            values = ArrayList()
            values.add(value)
        }
        this.paramsArray[key] = values


    }
}


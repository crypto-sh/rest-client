package com.github.library.helper

import okhttp3.FormBody
import okhttp3.Response

object General {

    fun stringIsEmptyOrNull(string: String?): Boolean {
        return try {
            string == null || string.isEmpty() || string == "null"
        } catch (ex: Exception) {
            false
        }

    }

    fun isJsonResponse(response: Response): Boolean {
        val body = response.body() ?: return false
        val content = body.contentType() ?: return false
        return content.subtype() == "json"
    }

    fun postAddIfValid(formBodyBuilder: FormBody.Builder, params: Map<String, String>?) {
        if (params == null) return
        for ((key, value) in params) {
            if (isValid(value)) {
                formBodyBuilder.add(key, value)
            }
        }
    }

    private fun isValid(s: String?): Boolean {
        return s != null && s.trim { it <= ' ' }.isNotEmpty()
    }
}

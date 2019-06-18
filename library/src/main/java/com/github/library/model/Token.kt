package com.github.library.model

import org.json.JSONException
import org.json.JSONObject

class Token(tokenResult: String) {

    var expires_in: Long? = null
    var token_type: String
    var refresh_token: String
    var access_token: String
    var scope: String

    init {
        val result = parseStringToJsonObject(tokenResult)
        access_token    = parseJsonObjectToString("access_token", result)
        token_type      = parseJsonObjectToString("token_type", result)
        refresh_token   = parseJsonObjectToString("refresh_token", result)
        scope           = parseJsonObjectToString("scope", result)
        expires_in      = parseJsonObjectToLong("expires_in", result)
    }

    private fun parseJsonObjectToString(key: String, value: JSONObject?): String {
        return try {
            if (value!!.isNull(key)) "" else value.getString(key)
        } catch (ex: JSONException) {
            ""
        } catch (ex: Exception) {
            ""
        }

    }

    private fun parseJsonObjectToLong(key: String, value: JSONObject?): Long {
        return try {
            if (value!!.isNull(key)) 0.toLong() else value.getLong(key)
        } catch (ex: JSONException) {
            0.toLong()
        } catch (ex: Exception) {
            0.toLong()
        }

    }

    private fun parseStringToJsonObject(value: String): JSONObject? {
        return try {
            JSONObject(value)
        } catch (ex: JSONException) {
            null
        } catch (ex: Exception) {
            null
        }

    }
}

package com.github.library.model


import androidx.collection.ArrayMap

import com.github.library.enums.AuthType

import okhttp3.Credentials


class AuthModel {

    var clientId        : String = ""
    var clientSecret    : String = ""
    var site            : String = ""

    var token           : String = ""
    var grantType       : String = ""

    var username        : String = ""
    var password        : String = ""

    var baseUrl         : String = ""

    var authType: AuthType = AuthType.NO_AUTH

    var headers: ArrayMap<String, String>? = null

    val basicAuthorization: String
        get() = Credentials.basic(clientId, clientSecret)
}

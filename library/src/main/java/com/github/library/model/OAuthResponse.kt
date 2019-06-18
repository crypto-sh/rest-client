package com.github.library.model


import com.github.library.helper.General
import com.github.library.oauth2.OAuthError
import okhttp3.Response
import java.io.IOException

class OAuthResponse {

    var expiresAt: Long? = null
        private set
    var body: String? = null
        private set
    private var token: Token? = null
    var oAuthError: OAuthError? = null
        private set
    var httpResponse: Response? = null
        private set
    var isJsonResponse: Boolean = false
        private set

    val isSuccessful: Boolean
        get() = httpResponse != null && httpResponse!!.isSuccessful && isJsonResponse

    val code: Int?
        get() = if (httpResponse != null) httpResponse!!.code() else null

    val expiresIn: Long?
        get() = if (token != null) token!!.expires_in else null

    val tokenType: String?
        get() = if (token != null) token!!.token_type else null

    val refreshToken: String?
        get() = if (token != null) token!!.refresh_token else null

    val accessToken: String?
        get() = if (token != null) token!!.access_token else null

    val scope: String?
        get() = if (token != null) token!!.scope else null

    @Throws(IOException::class)
    constructor(response: Response?) {
        this.httpResponse = response
        if (response != null) {
            val body = response.body() ?: return
            this.body = body.string()
            if (General.isJsonResponse(response)) {
                if (response.isSuccessful) {
                    token = Token(this.body!!)
                    isJsonResponse = true
                    if (token!!.expires_in != null)
                        expiresAt = token!!.expires_in!! * 1000 + System.currentTimeMillis()
                } else {
                    try {
                        isJsonResponse = true
                    } catch (e: Exception) {
                        oAuthError = OAuthError(e)
                        isJsonResponse = false

                    }

                }
            }
        }
    }

    constructor(e: Exception) {
        this.httpResponse = null
        this.oAuthError = OAuthError(e)
    }

}

package com.github.library.oauth2


import androidx.collection.ArrayMap
import com.github.library.helper.AuthorizationParams
import com.github.library.enums.AuthType
import com.github.library.model.AuthModel
import com.github.library.model.OAuthResponse
import com.github.library.oauth2.Access.Companion.getAccessToken
import okhttp3.OkHttpClient
import java.io.IOException
import java.util.*

class OAuth2Client(internal val okHttpClient: OkHttpClient, internal val headers: ArrayMap<String, String>, val authModel: AuthModel) {

    val site: String get() = authModel.site

    val clientId: String
        get() = authModel.clientId

    val clientSecret: String
        get() = authModel.clientSecret

    val authType: AuthType get() = authModel.authType

    internal val fieldsAsMap: Map<String, String>
        get() {
            val oAuthParams = HashMap<String, String>()
            oAuthParams[AuthorizationParams.POST_CLIENT_ID] = authModel.clientId
            oAuthParams[AuthorizationParams.POST_CLIENT_SECRET] = authModel.clientSecret
            oAuthParams[AuthorizationParams.POST_GRANT_TYPE] = authModel.grantType
            oAuthParams[AuthorizationParams.POST_USERNAME] = authModel.username
            oAuthParams[AuthorizationParams.POST_PASSWORD] = authModel.password
            oAuthParams[AuthorizationParams.POST_TOKEN] = authModel.token
            return oAuthParams
        }

    @Throws(IOException::class)
    fun refreshAccessToken(refreshToken: String): OAuthResponse {
        this.authModel.grantType = AuthorizationParams.GRANT_TYPE_REFRESH
        return Access.refreshAccessToken(refreshToken, this)
    }

    @Throws(IOException::class)
    private fun requestAccessToken(): OAuthResponse {
        this.authModel.grantType = AuthorizationParams.GRANT_TYPE_PASSWORD
        return getAccessToken(this)
    }

    fun requestAccessToken(callback: (OAuthResponse) -> Unit) {
        var response: OAuthResponse
        try {
            response = requestAccessToken()
            callback(response)
        } catch (e: IOException) {
            response = OAuthResponse(e)
            callback(response)
        }
    }
}

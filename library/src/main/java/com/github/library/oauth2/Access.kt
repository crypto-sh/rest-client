package com.github.library.oauth2


import com.github.library.helper.AuthorizationParams
import com.github.library.enums.AuthType
import com.github.library.helper.General
import com.github.library.model.OAuthResponse
import okhttp3.Credentials
import okhttp3.FormBody
import okhttp3.Request
import java.io.IOException

internal class Access  {

    companion object {
        @Throws(IOException::class)
        fun refreshAccessToken(token: String, oAuth2Client: OAuth2Client): OAuthResponse {
            val formBodyBuilder = FormBody.Builder().add(AuthorizationParams.POST_REFRESH_TOKEN, token)
            General.postAddIfValid(formBodyBuilder, oAuth2Client.fieldsAsMap)
            val request = Request.Builder()
                    .url(oAuth2Client.site)
                    .post(formBodyBuilder.build())
            addHeader(request, oAuth2Client)
            return getTokenFromResponse(oAuth2Client, request.build(), oAuth2Client.authType)
        }

        @Throws(IOException::class)
        fun getAccessToken(oAuth2Client: OAuth2Client): OAuthResponse {
            val formBodyBuilder = FormBody.Builder()
            General.postAddIfValid(formBodyBuilder, oAuth2Client.fieldsAsMap)
            val request = Request.Builder()
                    .url(oAuth2Client.site)
                    .post(formBodyBuilder.build())

            addHeader(request, oAuth2Client)
            return getTokenFromResponse(oAuth2Client, request.build(), oAuth2Client.authType)
        }

        @Throws(IOException::class)
        private fun getTokenFromResponse(oAuth2Client: OAuth2Client, request: Request, type: AuthType): OAuthResponse {
            val response = oAuth2Client.okHttpClient
                    .newBuilder()
                    .build()
                    .newCall(request.newBuilder()
                            .header(
                                    AuthorizationParams.HEADER_AUTHORIZATION,
                                    Credentials.basic(oAuth2Client.clientId, oAuth2Client.clientSecret)
                            )
                            .build())

                    .execute()
            return OAuthResponse(response)
        }

        private fun addHeader(request: Request.Builder, oAuth2Client: OAuth2Client) {
            val header = oAuth2Client.headers
            for (index in 0 until header.size) {
                val key = header.keyAt(index)
                val value = header[key]!!
                request.addHeader(key, value)
            }
            val grantedType = oAuth2Client.authModel.grantType
            request.addHeader("grant_type",grantedType)
        }
    }
}

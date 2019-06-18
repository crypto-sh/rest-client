/**
 * Created by alishatergholi on 2/20/18.
 */
package com.github.library


import android.content.Context

import androidx.collection.ArrayMap

import com.github.library.helper.ResultHandler
import com.github.library.enums.AuthType
import com.github.library.enums.ErrorCode
import com.github.library.requestMethod.DELETE
import com.github.library.requestMethod.GET
import com.github.library.requestMethod.POST
import com.github.library.requestMethod.PUT
import com.github.library.utils.RequestParams

import java.lang.ref.WeakReference


class RestClient constructor(builder: Builder) : BaseClient() {

    class Builder(internal val appContext: Context) {

        internal var site: String = ""

        internal var clientId: String = ""
        internal var clientSecret: String = ""

        internal val token: String = ""

        internal var username: String = ""
        internal var password: String = ""

        internal var grantType: String = ""

        internal var baseUrl  : String = ""

        internal var timeMilliSecond: Int = 60

        internal var debugEnable = true

        internal var authType = AuthType.NO_AUTH

        internal var headers = ArrayMap<String, String>()

        fun setAuthorizationOauth2(authSite: String, clientId: String, clientSecret: String): Builder {
            this.site = authSite
            this.clientId = clientId
            this.clientSecret = clientSecret
            this.authType = AuthType.OAUTH2_AUTH
            return this
        }

        fun setAuthorizationBasic(username: String, password: String): Builder {
            this.username = username
            this.password = password
            this.authType = AuthType.BASIC_AUTH
            return this
        }

        fun setGrantType(grantType: String): Builder {
            this.grantType = grantType
            return this
        }

        fun setConnectionTimeOut(timeMiliSecond: Int?): Builder {
            this.timeMilliSecond = timeMiliSecond!!
            return this
        }

        fun setHeader(headers: ArrayMap<String, String>): Builder {
            this.headers = headers
            return this
        }

        fun setDebugEnable(enable: Boolean): Builder {
            this.debugEnable = enable
            return this
        }

        fun setBaseUrl(baseUrl : String) : Builder{
            this.baseUrl = baseUrl
            return this
        }

        fun build(): RestClient {
            return RestClient(this)
        }
    }

    init {
        this.appContext     = WeakReference(builder.appContext)
        this.clientId       = builder.clientId
        this.clientSecret   = builder.clientSecret
        this.site           = builder.site
        this.baseUrl        = builder.baseUrl
        this.token          = builder.token
        this.grantType      = builder.grantType
        this.username       = builder.username
        this.password       = builder.password
        this.debugEnable    = builder.debugEnable
        this.timeMilliSecond = builder.timeMilliSecond
        this.authType       = builder.authType
        this.headers        = builder.headers
    }

    fun post(
            url: String,
            tag: String,
            params: RequestParams,
            responder: ResultHandler) {
        if (!checkNetworkConnection(this.appContext!!.get())) {
            responder.onFailure(url, ErrorCode.InternetConnectionError)
            return
        }
        //endregion
        if (this.authType === AuthType.OAUTH2_AUTH) {
            POST.basicAuth(getClient(timeMilliSecond, debugEnable)!!, getUrl(url), tag, authModel, params, responder)
        } else {
            POST.noAuth(getClient(timeMilliSecond, debugEnable)!!, getUrl(url), tag, authModel, params, responder)
        }
    }

    fun get(
            url: String,
            tag: String,
            responder: ResultHandler) {
        if (!checkNetworkConnection(this.appContext!!.get())) {
            responder.onFailure(url, ErrorCode.InternetConnectionError)
            return
        }
        //endregion
        if (this.authType === AuthType.OAUTH2_AUTH) {
            GET.oauth2Auth(getClient(timeMilliSecond, debugEnable)!!, getUrl(url), tag, authModel, responder)
        } else {
            GET.noAuth(getClient(timeMilliSecond, debugEnable)!!, getUrl(url), tag, authModel, responder)
        }
    }

    fun delete(
            url: String,
            tag: String,
            params: RequestParams,
            responder: ResultHandler) {

        if (!checkNetworkConnection(this.appContext!!.get())) {
            responder.onFailure(url, ErrorCode.InternetConnectionError)
            return
        }

        if (this.authType === AuthType.OAUTH2_AUTH) {
            DELETE.basicAuth(getClient(timeMilliSecond, debugEnable)!!, getUrl(url), tag, authModel, params, responder)
        } else {
            DELETE.noAuth(getClient(timeMilliSecond, debugEnable)!!, getUrl(url), tag, authModel, params, responder)
        }
    }

    fun put(
            url: String,
            tag: String,
            params: RequestParams,
            responder: ResultHandler) {

        if (!checkNetworkConnection(this.appContext!!.get())) {
            responder.onFailure(url, ErrorCode.InternetConnectionError)
            return
        }

        if (this.authType === AuthType.OAUTH2_AUTH) {
            PUT.basicAuth(getClient(timeMilliSecond, debugEnable)!!, getUrl(url), tag, authModel, params, responder)
        } else {
            PUT.noAuth(getClient(timeMilliSecond, debugEnable)!!, getUrl(url), tag, authModel, params, responder)
        }
    }

    private fun getUrl(url : String) : String = baseUrl + url

}

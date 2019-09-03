/**
 * Created by alishatergholi on 2/20/18.
 */
package com.github.library;

import android.content.Context;

import androidx.collection.ArrayMap;

import com.github.library.Interface.ResultHandler;
import com.github.library.enums.AuthType;
import com.github.library.enums.ErrorCode;
import com.github.library.helper.general;
import com.github.library.requestMethod.DELETE;
import com.github.library.requestMethod.GET;
import com.github.library.requestMethod.POST;
import com.github.library.requestMethod.PUT;
import com.github.library.utils.RequestParams;

import java.lang.ref.WeakReference;

public class RestClient extends BaseClient {

    public static class Builder {

        private final Context appContext;
        private String site;

        private String clientId;
        private String clientSecret;

        private String token;

        private String username;
        private String password;

        private String grantType;

        private Integer timeMilliSecond = 60;

        private Integer timeRead        = 30;

        private boolean debugEnable = true;

        private AuthType authType = AuthType.NO_AUTH;

        private ArrayMap<String, String> headers = new ArrayMap<>();

        private ArrayMap<String, String> CPKArray = new ArrayMap<>();

        public Builder(Context context) {
            this.appContext = context;
        }

        public Builder setAuthorizationOauth2(String authSite, String clientId, String clientSecret) {
            this.site = authSite;
            this.clientId = clientId;
            this.clientSecret = clientSecret;
            this.authType = AuthType.OAUTH2_AUTH;
            return this;
        }

        public Builder setAuthorizationBasic(String username, String password) {
            this.username = username;
            this.password = password;
            this.authType = AuthType.BASIC_AUTH;
            return this;
        }

        public Builder setGrantType(String grantType) {
            this.grantType = grantType;
            return this;
        }

        public Builder setConnectionTimeOut(Integer timeMilliSecond) {
            this.timeMilliSecond = timeMilliSecond;
            return this;
        }

        public Builder setReadTimeOut(Integer timeMilliSecond) {
            this.timeMilliSecond = timeMilliSecond;
            return this;
        }

        public Builder setHeader(ArrayMap<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder setDebugEnable(boolean enable) {
            this.debugEnable = enable;
            return this;
        }

        public Builder setCPKArray(ArrayMap<String, String> CPKArray) {
            this.CPKArray = CPKArray;
            return this;
        }

        public RestClient build() {
            return new RestClient(this);
        }

    }

    private RestClient(Builder builder) {
        this.appContext = new WeakReference<>(builder.appContext);
        this.clientId = builder.clientId;
        this.clientSecret = builder.clientSecret;
        this.site = builder.site;
        this.token = builder.token;
        this.grantType = builder.grantType;
        this.username = builder.username;
        this.password = builder.password;
        this.debugEnable = builder.debugEnable;
        this.timeMilliSecond = builder.timeMilliSecond;
        this.authType = builder.authType;
        this.headers = builder.headers;

        RestClient.certificatePinner = general.getCertificatePinner(builder.CPKArray);
    }

    public void POST(
            String url,
            String tag,
            RequestParams params,
            ResultHandler responder) {
        if (checkNetworkConnection(this.appContext.get())) {
            responder.onFailure(url, ErrorCode.InternetConnectionError);
            return;
        }
        //endregion
        if (this.authType == AuthType.OAUTH2_AUTH) {
            POST.basic_Auth(getClient(timeMilliSecond, timeRead, debugEnable), url, tag, getAuthModel(), params, responder);
        } else {
            POST.no_Auth(getClient(timeMilliSecond, timeRead, debugEnable), url, tag, getAuthModel(), params, responder);
        }
    }

    // POST WITH EXTRA HEADER
    public void POST(
            String url,
            String tag,
            RequestParams params,
            ArrayMap<String, String> extraHeaders,
            ResultHandler responder) {
        if (checkNetworkConnection(this.appContext.get())) {
            responder.onFailure(url, ErrorCode.InternetConnectionError);
            return;
        }
        //endregion
        if (this.authType == AuthType.OAUTH2_AUTH) {
            POST.basic_Auth(getClient(timeMilliSecond, timeRead, debugEnable), url, tag, getAuthModel(extraHeaders), params, responder);
        } else {
            POST.no_Auth(getClient(timeMilliSecond, timeRead, debugEnable), url, tag, getAuthModel(extraHeaders), params, responder);
        }
    }

    public void GET(
            String url,
            String tag,
            ResultHandler responder) {
        if (checkNetworkConnection(this.appContext.get())) {
            responder.onFailure(url, ErrorCode.InternetConnectionError);
            return;
        }

        //endregion
        if (this.authType == AuthType.OAUTH2_AUTH) {
            GET.oauth2_Auth(getClient(timeMilliSecond, timeRead, debugEnable), url, tag, getAuthModel(), responder);
        } else {
            GET.no_Auth(getClient(timeMilliSecond, timeRead, debugEnable), url, tag, getAuthModel(), responder);
        }
    }

    public void DELETE(
            String url,
            String tag,
            RequestParams params,
            ResultHandler responder) {

        if (checkNetworkConnection(this.appContext.get())) {
            responder.onFailure(url, ErrorCode.InternetConnectionError);
            return;
        }

        if (this.authType == AuthType.OAUTH2_AUTH) {
            DELETE.basic_Auth(getClient(timeMilliSecond, timeRead, debugEnable), url, tag, getAuthModel(), params, responder);
        } else {
            DELETE.no_Auth(getClient(timeMilliSecond, timeRead, debugEnable), url, tag, getAuthModel(), params, responder);
        }
    }

    public void PUT(
            String url,
            String tag,
            RequestParams params,
            ResultHandler responder) {

        if (checkNetworkConnection(this.appContext.get())) {
            responder.onFailure(url, ErrorCode.InternetConnectionError);
            return;
        }

        if (this.authType == AuthType.OAUTH2_AUTH) {
            PUT.basic_Auth(getClient(timeMilliSecond, timeRead, debugEnable), url, tag, getAuthModel(), params, responder);
        } else {
            PUT.no_Auth(getClient(timeMilliSecond, timeRead, debugEnable), url, tag, getAuthModel(), params, responder);
        }
    }
}

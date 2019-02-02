package com.github.library.model;


import java.io.IOException;

import com.github.library.helper.general;

import com.github.library.oauth2.OAuthError;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class OAuthResponse {

    private Long expiresAt;
    private String responseBody;
    private Token token;
    private OAuthError error;
    private Response response;
    private boolean jsonParsed;

    public OAuthResponse(Response response) throws IOException {
        this.response = response;
        if (response != null) {
            ResponseBody body = response.body();
            if (body  == null){
                return;
            }
            responseBody = body.string();
            if (general.isJsonResponse(response)) {
                if (response.isSuccessful()) {
                    token = new Token(responseBody);
                    jsonParsed = true;
                    if (token.expires_in != null)
                        expiresAt = (token.expires_in * 1000) + System.currentTimeMillis();
                } else {
                    try {
                        //error = moshi.adapter(OAuthError.class).fromJson(responseBody);
                        jsonParsed = true;
                    } catch (Exception e) {
                        error = new OAuthError(e);
                        jsonParsed = false;

                    }
                }
            }
        }
    }

    public OAuthResponse(Exception e) {
        this.response = null;
        this.error = new OAuthError(e);
    }

    public boolean isSuccessful() {
        return response != null && response.isSuccessful() && jsonParsed;
    }

    public boolean isJsonResponse() {
        return jsonParsed;
    }

    public Integer getCode() {
        return response != null ? response.code(): null;
    }

    public Long getExpiresIn() {
        return token != null ? token.expires_in : null;
    }

    public Long getExpiresAt() {
        return expiresAt;
    }

    public String getTokenType() {
        return token != null ? token.token_type : null;
    }

    public String getRefreshToken() {
        return token != null ? token.refresh_token : null;
    }

    public String getAccessToken() {
        return token != null ? token.access_token : null;
    }

    public String getScope() {
        return token != null ? token.scope : null;
    }

    public String getBody() {
        return responseBody;
    }

    public OAuthError getOAuthError() {
        return error;
    }

    public Response getHttpResponse() {
        return response;
    }

}

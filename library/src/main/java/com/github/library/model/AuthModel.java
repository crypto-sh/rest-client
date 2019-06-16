package com.github.library.model;


import androidx.collection.ArrayMap;

import com.github.library.enums.AuthType;

import okhttp3.Credentials;


public class AuthModel {

    private String clientId;
    private String clientSecret;
    private String site;

    private String token;
    private String grantType;

    private String username;
    private String password;

    private AuthType authType;

    private ArrayMap<String,String> headers;

    public AuthModel(){

    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AuthType getAuthType() {
        return authType;
    }

    public void setAuthType(AuthType authType) {
        this.authType = authType;
    }

    public String getBasicAuthorization() {
        return Credentials.basic(getClientId(), getClientSecret());
    }

    public ArrayMap<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(ArrayMap<String, String> headers) {
        this.headers = headers;
    }
}

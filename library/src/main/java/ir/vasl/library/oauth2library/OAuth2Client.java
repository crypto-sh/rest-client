package ir.vasl.library.oauth2library;


import android.support.v4.util.ArrayMap;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ir.vasl.library.Interface.AuthorizationParams;
import ir.vasl.library.Interface.OAuthResponseCallback;
import ir.vasl.library.enums.AuthType;
import ir.vasl.library.model.AuthModel;
import ir.vasl.library.model.OAuthResponse;
import okhttp3.OkHttpClient;

public class OAuth2Client implements AuthorizationParams {


    private final OkHttpClient okHttpClient;
    private final ArrayMap<String, String> headers;
    private final AuthModel authModel;

    public OAuth2Client(OkHttpClient client, ArrayMap<String, String> headers, AuthModel auth) {
        this.okHttpClient = client;
        this.headers = headers;
        this.authModel = auth;
    }

    public OAuthResponse refreshAccessToken(String refreshToken) throws IOException {
        if (this.authModel.getGrantType() == null)
            this.authModel.setGrantType(GRANT_TYPE_REFRESH);
        return Access.refreshAccessToken(refreshToken, this);
    }

    public void refreshAccessToken(final String refreshToken, final OAuthResponseCallback callback) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                OAuthResponse response;
//                try {
//                    response = refreshAccessToken(refreshToken);
//                    callback.onResponse(response);
//                } catch (Exception e) {
//                    response = new OAuthResponse(e);
//                    callback.onResponse(response);
//                }
//            }
//        }).start();
    }

    public OAuthResponse requestAccessToken() throws IOException {
        if (this.authModel.getGrantType() == null)
            this.authModel.setGrantType(GRANT_TYPE_PASSWORD);
        return Access.getAccessToken(this);
    }

    public void requestAccessToken(OAuthResponseCallback callback) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                OAuthResponse response;
                try {
                    response = requestAccessToken();
                    callback.onResponse(response);
                } catch (IOException e) {
                    response = new OAuthResponse(e);
                    callback.onResponse(response);
                }
            }
        }.start();
    }

    OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public String getSite() {
        return authModel.getSite();
    }

    public String getPassword() {
        return authModel.getPassword();
    }

    public String getUsername() {
        return authModel.getUsername();
    }

    public String getClientId() {
        return authModel.getClientId();
    }

    public String getClientSecret() {
        return authModel.getClientSecret();
    }

    public AuthType getAuthType() {
        return authModel.getAuthType();
    }

    Map<String, String> getFieldsAsMap() {
        Map<String, String> oAuthParams = new HashMap<>();
        oAuthParams.put(POST_CLIENT_ID, authModel.getClientId());
        oAuthParams.put(POST_CLIENT_SECRET, authModel.getClientSecret());
        oAuthParams.put(POST_GRANT_TYPE, authModel.getGrantType());
        oAuthParams.put(POST_SCOPE, authModel.getScope());
        oAuthParams.put(POST_USERNAME, authModel.getUsername());
        oAuthParams.put(POST_PASSWORD, authModel.getPassword());
        return oAuthParams;
    }

    ArrayMap<String, String> getHeaders() {
        return headers;
    }
}

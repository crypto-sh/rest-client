package com.github.library.oauth2;




import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import androidx.collection.ArrayMap;
import com.github.library.Interface.AuthorizationParams;
import com.github.library.Interface.OAuthResponseCallback;
import com.github.library.enums.AuthType;
import com.github.library.model.AuthModel;
import com.github.library.model.OAuthResponse;
import okhttp3.OkHttpClient;

public class OAuth2Client implements AuthorizationParams {

    private final AuthModel authModel;
    private final OkHttpClient okHttpClient;
    private final ArrayMap<String, String> headers;

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

    private OAuthResponse requestAccessToken() throws IOException {
        if (this.authModel.getGrantType() == null)
            this.authModel.setGrantType(GRANT_TYPE_PASSWORD);
        return Access.getAccessToken(this);
    }

    public void requestAccessToken(final OAuthResponseCallback callback) {
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
        oAuthParams.put(POST_CLIENT_ID      , authModel.getClientId());
        oAuthParams.put(POST_CLIENT_SECRET  , authModel.getClientSecret());
        oAuthParams.put(POST_GRANT_TYPE     , authModel.getGrantType());
        oAuthParams.put(POST_USERNAME       , authModel.getUsername());
        oAuthParams.put(POST_PASSWORD       , authModel.getPassword());
        oAuthParams.put(POST_TOKEN          , authModel.getToken());
        return oAuthParams;
    }

    ArrayMap<String, String> getHeaders() {
        return headers;
    }
}

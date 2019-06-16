package com.github.library.oauth2;


import java.io.IOException;

import androidx.collection.ArrayMap;

import com.github.library.Interface.AuthorizationParams;
import com.github.library.enums.AuthType;
import com.github.library.helper.general;
import com.github.library.model.OAuthResponse;

import okhttp3.Credentials;
import okhttp3.FormBody;

import okhttp3.Request;
import okhttp3.Response;

class Access implements AuthorizationParams {

    static OAuthResponse refreshAccessToken(String token, OAuth2Client oAuth2Client) throws IOException {
        FormBody.Builder formBodyBuilder = new FormBody.Builder().add(POST_REFRESH_TOKEN, token);
        general.postAddIfValid(formBodyBuilder, oAuth2Client.getFieldsAsMap());

        Request.Builder request = new Request.Builder()
                .url(oAuth2Client.getSite())
                .post(formBodyBuilder.build());

        addHeader(request, oAuth2Client);

        return getTokenFromResponse(oAuth2Client, request.build(), oAuth2Client.getAuthType());
    }

    static OAuthResponse getAccessToken(OAuth2Client oAuth2Client) throws IOException {
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        //TODO check it later
//        formBodyBuilder.addEncoded("grant_type","client_credentials");
        general.postAddIfValid(formBodyBuilder, oAuth2Client.getFieldsAsMap());
        Request.Builder request = new Request
                .Builder()
                .url(oAuth2Client.getSite())
                .post(formBodyBuilder.build());

        addHeader(request, oAuth2Client);
        return getTokenFromResponse(oAuth2Client, request.build(), oAuth2Client.getAuthType());
    }

    private static OAuthResponse getTokenFromResponse(OAuth2Client oAuth2Client, Request request, AuthType type) throws IOException {

        Response response = oAuth2Client.getOkHttpClient()
                .newBuilder()
                .build()
                .newCall(request.newBuilder()
                        .header(HEADER_AUTHORIZATION, Credentials.basic(oAuth2Client.getClientId(), oAuth2Client.getClientSecret()))
                        .build())

                .execute();

        return new OAuthResponse(response);
    }

    private static void addHeader(Request.Builder request, OAuth2Client oAuth2Client) {
        ArrayMap<String, String> header = oAuth2Client.getHeaders();
        for (int index = 0; index < header.size(); index++) {
            String key = header.keyAt(index);
            String value = header.get(key);
            assert value != null;
            request.addHeader(key, value);
        }
    }
}

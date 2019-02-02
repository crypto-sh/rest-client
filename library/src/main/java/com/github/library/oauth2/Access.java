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

public class Access implements AuthorizationParams {

    protected static OAuthResponse refreshAccessToken(String token, OAuth2Client oAuth2Client) throws IOException {

        FormBody.Builder formBodyBuilder = new FormBody.Builder().add(POST_REFRESH_TOKEN, token);
        general.postAddIfValid(formBodyBuilder, oAuth2Client.getFieldsAsMap());

        Request.Builder request = new Request.Builder()
                .url(oAuth2Client.getSite())
                .post(formBodyBuilder.build());

        addHeader(request, oAuth2Client);

        return getTokenFromResponse(oAuth2Client, request.build(), oAuth2Client.getAuthType());
    }

    protected static OAuthResponse getAccessToken(OAuth2Client oAuth2Client) throws IOException {
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        general.postAddIfValid(formBodyBuilder, oAuth2Client.getFieldsAsMap());

        Request.Builder request = new Request.Builder()
                .url(oAuth2Client.getSite())
                .post(formBodyBuilder.build());
        addHeader(request, oAuth2Client);
        return getTokenFromResponse(oAuth2Client, request.build(), oAuth2Client.getAuthType());
    }

    private static OAuthResponse getTokenFromResponse(final OAuth2Client oAuth2Client,
                                                      final Request request,
                                                      final AuthType type) throws IOException {

        String credential = "";
        switch (type) {
            case BASIC_AUTH:
                credential = Credentials.basic(oAuth2Client.getClientId(), oAuth2Client.getClientSecret());
                break;
            default:
                break;
        }
        Response response = oAuth2Client.getOkHttpClient()
                .newBuilder()
                .build()
                .newCall(request.newBuilder().header(HEADER_AUTHORIZATION, credential).build()).execute();
        return new OAuthResponse(response);
    }
//    protected static Authenticator getAuthenticator(final OAuth2Client oAuth2Client, final AuthType type) {
//        return new Authenticator() {
//            @Override
//            public Request authenticate(Route route, Response response) throws IOException {
//
//                String credential = Credentials.basic(oAuth2Client.getClientId(), oAuth2Client.getClientSecret());
//                return response.request().newBuilder().header(HEADER_AUTHORIZATION, credential).build();
//            }
//        };
//    }

    private static void addHeader(Request.Builder request, OAuth2Client oAuth2Client) {
        ArrayMap<String, String> header = oAuth2Client.getHeaders();
        for (int index = 0; index < header.size(); index++) {
            String key = header.keyAt(index);
            String value = header.get(key);
            request.addHeader(key, value);
        }
    }
}

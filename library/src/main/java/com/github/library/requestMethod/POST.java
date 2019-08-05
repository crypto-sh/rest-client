package com.github.library.requestMethod;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;

import com.github.library.Interface.ResultHandler;
import com.github.library.enums.AuthType;
import com.github.library.enums.ErrorCode;
import com.github.library.enums.RequestBodyType;
import com.github.library.helper.general;
import com.github.library.model.AuthModel;
import com.github.library.oauth2.OAuth2Client;
import com.github.library.response.ResponseJsonHandler;
import com.github.library.response.ResponseTextHandler;
import com.github.library.utils.RequestBodyProgressive;
import com.github.library.utils.RequestParams;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class POST extends baseMethod {

    public synchronized static void no_Auth(
            @NonNull OkHttpClient client,
            @NonNull String url,
            @Nullable String tag,
            @NonNull AuthModel authModel,
            @NonNull RequestParams params,
            @NonNull ResultHandler responder) {
        try {

            RequestBody requestBody = params.getRequestForm();

            if (params.getType() == RequestBodyType.MultiPart) {
                requestBody = new RequestBodyProgressive(requestBody, new RequestBodyProgressive.Listener() {
                    @Override
                    public void onRequestProgress(long bytesWritten, long contentLength) {
                        double percent = (double) bytesWritten / (double) contentLength;
                        percent = Math.floor(percent * 100) / 1;
                        responder.onProgress(percent, bytesWritten, contentLength);
                    }
                });
            }

            Request.Builder request = new Request.Builder()
                    .url(url)
                    .tag(tag)
                    .addHeader("cache-control", "no-cache")
                    .addHeader("os", "android")
                    .post(requestBody);

            if (responder instanceof ResponseTextHandler) {
                request.addHeader("Content-Type", "application/text");
                request.addHeader("Accept", "application/text");
            } else if (responder instanceof ResponseJsonHandler) {
                request.addHeader("Content-Type", "application/json; charset=utf-8");
                request.addHeader("Accept", "application/json");
            } else {
                request.addHeader("Content-Type", "application/x-www-form-urlencoded");
                request.addHeader("Accept", "application/octet-stream");
            }

            if (authModel.getAuthType() == AuthType.BASIC_AUTH) {
                request.addHeader("Authorization", String.format("Basic %", authModel.getBasicAuthorization()));
            }

            ArrayMap<String, String> headers = authModel.getHeaders();
            for (int index = 0; index < headers.size(); index++) {
                try {
                    String key = headers.keyAt(index);
                    String value = headers.get(key);
                    if (value != null) {
                        if (!general.StringIsEmptyOrNull(key) && !general.StringIsEmptyOrNull(value)) {
                            request.addHeader(key, value);
                        }
                    }
                } catch (Exception e) {
                    new Handler(Looper.getMainLooper()).post(() -> responder.onFailure(url, ErrorCode.RuntimeException));
                }
            }
            client.newCall(request.build()).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NotNull IOException e) {
                    responder.onFailure(url, ErrorCode.ServerConnectionError);
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull final Response response) {
                    responder.onResultHandler(response);
                }
            });
        } catch (Exception ex) {
            responder.onFailure(url, ErrorCode.RuntimeException);
        }
    }

    public synchronized static void basic_Auth(
            @NonNull OkHttpClient client,
            @NonNull String url,
            @Nullable String tag,
            @NonNull AuthModel authModel,
            @NonNull RequestParams params,
            @NonNull ResultHandler responder) {
        try {
            final ArrayMap<String, String> headers = authModel.getHeaders();
            OAuth2Client auth = new OAuth2Client(client, headers, authModel);
            auth.requestAccessToken(response -> {
                if (response.isSuccessful()) {
                    switch (authModel.getAuthType()) {
                        case OAUTH2_AUTH:
                            headers.put("Authorization", String.format("Bearer %s", response.getAccessToken()));
                            break;
                    }
                    authModel.setHeaders(headers);
                    no_Auth(client, url, tag, authModel, params, responder);
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> responder.onFailure(url, ErrorCode.AuthorizationException));
                }
            });
        } catch (Exception ex) {
            new Handler(Looper.getMainLooper()).post(() -> responder.onFailure(url, ErrorCode.RuntimeException));
        }
    }
}

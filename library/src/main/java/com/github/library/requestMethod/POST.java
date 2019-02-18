package com.github.library.requestMethod;

import android.os.Handler;
import android.os.Looper;

import com.github.library.Interface.OAuthResponseCallback;
import com.github.library.Interface.ResultHandler;
import com.github.library.enums.ErrorCode;
import com.github.library.helper.general;
import com.github.library.model.AuthModel;
import com.github.library.model.FileModel;
import com.github.library.model.OAuthResponse;
import com.github.library.oauth2.OAuth2Client;
import com.github.library.response.ResponseJsonHandler;
import com.github.library.response.ResponseTextHandler;
import com.github.library.utils.RequestBodyProgressive;
import com.github.library.utils.RequestParams;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class POST extends baseMethod {

    public synchronized static void no_Auth(
            @NonNull OkHttpClient client,
            @NonNull final String url,
            @Nullable String tag,
            @NonNull AuthModel authModel,
            @NonNull RequestParams params,
            @NonNull final ResultHandler responder) {
        final Long startTime = getTimeMillisecond();
        try {
            Request.Builder request = new Request.Builder()
                    .url(url)
                    .tag(tag)
                    .addHeader("cache-control", "no-cache")
                    .addHeader("os", "android")
                    .post(params.getRequestForm());
            switch (authModel.getEncodingType()) {
                case GZIP:
                    request.addHeader("Accept-Encoding", "gzip");
                    break;
            }
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
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            responder.onFailure(url, startTime, ErrorCode.RuntimeException);
                        }
                    });
                }
            }
            client.newCall(request.build()).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, final IOException e) {
                    responder.onFailure(url, startTime, ErrorCode.ServerConnectionError);
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull final Response response) {
                    responder.onResultHandler(startTime, response);
                }
            });
        } catch (Exception ex) {
            responder.onFailure(url, startTime, ErrorCode.RuntimeException);
        }
    }

    public synchronized static void basic_Auth(
            @NonNull final OkHttpClient client,
            @NonNull final String url,
            @Nullable final String tag,
            @NonNull final AuthModel authModel,
            @NonNull final RequestParams params,
            @NonNull final ResultHandler responder) {
        final Long startTime = getTimeMillisecond();
        try {
            final ArrayMap<String, String> headers = authModel.getHeaders();
            OAuth2Client auth = new OAuth2Client(client, headers, authModel);
            auth.requestAccessToken(new OAuthResponseCallback() {
                @Override
                public void onResponse(OAuthResponse response) {
                    if (response.isSuccessful()) {
                        switch (authModel.getAuthType()) {
                            case BASIC_AUTH:
                                headers.put("Authorization", String.format("Bearer %s", response.getAccessToken()));
                                break;
                        }
                        authModel.setHeaders(headers);
                        no_Auth(client, url, tag, authModel, params, responder);
                    } else {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                responder.onFailure(url, startTime, ErrorCode.AuthorizationException);
                            }
                        });
                    }
                }
            });
        } catch (Exception ex) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    responder.onFailure(url, startTime, ErrorCode.RuntimeException);
                }
            });
        }
    }

//    static class LoggingInterceptor implements Interceptor {
//        @Override public Response intercept(Interceptor.Chain chain) throws IOException {
//            Request request = chain.request();
//            long t1 = System.nanoTime();
//            logger.d(String.format("Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));
//            Response response = chain.proceed(request);
//            long t2 = System.nanoTime();
//            logger.d(String.format("Received response for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers()));
//            return response;
//        }
//    }

    public synchronized static void file_no_Auth(
            @NonNull OkHttpClient client,
            @NonNull final String url,
            @Nullable String tag,
            @NonNull AuthModel authModel,
            @NonNull RequestParams params,
            @NonNull final ResultHandler responder) {
        final Long startTime = getTimeMillisecond();
        try {
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            LinkedHashMap<String, Object> parameters = params.getParams();
            for (String key : parameters.keySet()) {
                builder.addFormDataPart(key, (String) Objects.requireNonNull(parameters.get(key)));
            }
            LinkedHashMap<String, FileModel> fileParams = params.getFileParams();
            for (String key : fileParams.keySet()) {
                FileModel uploadFile = fileParams.get(key);
                if (uploadFile != null) {
                    builder.addFormDataPart(key, uploadFile.getFilenName(), RequestBody.create(uploadFile.getMimeType(), uploadFile.getFile()));
                }
            }
            RequestBodyProgressive progressiveBuilder = new RequestBodyProgressive(builder.build(), (bytesWritten, contentLength) -> {
                new Handler(Looper.getMainLooper()).post(() -> {
                    Double percent = (double) bytesWritten / (double) contentLength;
                    percent = Math.floor(percent * 100) / 1;
                    responder.onProgress(percent,(long) bytesWritten,(long)contentLength);
                });
            });
            Request.Builder request = new Request.Builder()
                    .url(url)
                    .addHeader("accept-language"    , "fa")
                    .addHeader("cache-control"      , "no-cache")
                    .addHeader("os"                 , "android")
                    .post(progressiveBuilder);
            switch (authModel.getEncodingType()) {
                case GZIP:
                    request.addHeader("Accept-Encoding", "gzip");
                    break;
            }

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

            client.newCall(request.build()).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    new Handler(Looper.getMainLooper()).post(() -> responder.onFailure(url,startTime,ErrorCode.ServerConnectionError));
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) {
                    responder.onResultHandler(startTime,response);
                }
            });
        } catch (Exception e) {
            new Handler(Looper.getMainLooper()).post(() -> responder.onFailure(url,startTime,ErrorCode.Exception));
        }
    }

    public synchronized static void file_basic_Auth(
            @NonNull final OkHttpClient client,
            @NonNull final String url,
            @Nullable final String tag,
            @NonNull final AuthModel authModel,
            @NonNull final RequestParams params,
            @NonNull final ResultHandler responder) {
        final Long startTime = getTimeMillisecond();
        try {
            final ArrayMap<String, String> headers = authModel.getHeaders();
            OAuth2Client auth = new OAuth2Client(client, headers, authModel);
            auth.requestAccessToken(response -> {
                if (response.isSuccessful()) {
                    switch (authModel.getAuthType()) {
                        case BASIC_AUTH:
                            headers.put("Authorization", String.format("Bearer %s", response.getAccessToken()));
                            break;
                    }
                    authModel.setHeaders(headers);
                    file_no_Auth(client, url, tag, authModel, params, responder);
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        responder.onFailure(url, startTime, ErrorCode.AuthorizationException);
                    });
                }
            });
        } catch (Exception ex) {
            new Handler(Looper.getMainLooper()).post(() -> {
                responder.onFailure(url, startTime, ErrorCode.RuntimeException);
            });
        }
    }

}

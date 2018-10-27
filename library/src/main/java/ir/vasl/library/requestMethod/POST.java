package ir.vasl.library.requestMethod;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

import java.io.IOException;

import ir.vasl.library.Interface.OAuthResponseCallback;
import ir.vasl.library.enums.ErrorCode;
import ir.vasl.library.helper.general;
import ir.vasl.library.model.AuthModel;
import ir.vasl.library.model.OAuthResponse;
import ir.vasl.library.oauth2.OAuth2Client;
import ir.vasl.library.response.ResponseJsonHandler;
import ir.vasl.library.response.ResponseTextHandler;
import ir.vasl.library.utils.RequestParams;
import ir.vasl.library.Interface.ResultHandler;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class POST extends baseMethod {

    public synchronized static void no_Auth(
            @NonNull OkHttpClient client,
            @NonNull final String url,
            @NonNull String tag,
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
            @NonNull final String tag,
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

    //region upload File multipart
//    synchronized public static void PostFile (
//            @NonNull final Context context,
//            @NonNull final ResultType apiType,
//            @NonNull final String url,
//            @NonNull final RequestParams params,
//            @NonNull final ResultHandler responder) {
//        try {
//
//            OAuth2Client client = new OAuth2Client
//                    .Builder(property.getClientId(), property.getclientSecret(), property.getSite())
//                    .appId(property.getAppId())
//                    .username(property.getUserName())
//                    .password(property.getPassword())
//                    .build();
//
//            client.requestAccessToken(response -> {
//
//                if (response.isSuccessful()) {
//                    try {
//                        final Long startTime = getTimeMillisecond();
//                        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
//                        LinkedHashMap<String, Object> parameters = params.getParams();
//                        for (String key : parameters.keySet()) {
//                            builder.addFormDataPart(key, (String) parameters.get(key));
//                        }
//                        LinkedHashMap<String, File> fileParams = params.getFileParams();
//                        for (String key : fileParams.keySet()) {
//                            File uploadFile = fileParams.get(key);
//                            if (uploadFile != null) {
//                                try {
//                                    String filePath = uploadFile.getAbsolutePath();
//                                    final MediaType type = MediaType.parse(getMimeType(context, Uri.fromFile(uploadFile)));
//                                    final byte[] body = ImageToBytesCompressed(uploadFile);
//                                    if (body == null)
//                                        return;
//                                    builder.addFormDataPart(key, getFileName(filePath), RequestBody.create(type, body));
//                                } catch (Exception e) {
//                                    new Handler(Looper.getMainLooper()).post(() -> responder.onFailure(ErrorCode.ERROR_EXCEPTION_FILE_NOT_FOUND));
//                                }
//                            }
//                        }
//                        RequestBodyProgressive progressiveBuilder = new RequestBodyProgressive(builder.build(), (bytesWritten, contentLength) -> {
//                            logHelper.d("bytesWritten : " + bytesWritten, "totalSize : " + contentLength);
//                            new Handler(Looper.getMainLooper()).post(() -> {
//                                Double percent = (double) bytesWritten / (double) contentLength;
//                                percent = Math.floor(percent * 100) / 1;
//                                responder.onProgress(percent);
//
//                            });
//                        });
//                        Request.Builder request = new Request.Builder()
//                                .url(url)
//                                .addHeader("accept-language"    , "fa")
//                                .addHeader("cache-control"      , "no-cache")
//                                .addHeader("os"                 , "android")
//                                .addHeader("Accept-Encoding"    , "gzip")
//                                .addHeader("Authorization"      , String.format("Bearer %s", response.getAccessToken()))
//                                .addHeader("appid"              , property.getAppId())
//                                .post(progressiveBuilder);
//
//                        switch (apiType) {
//                            case Json:
//                                request.addHeader("Content-Type", "application/json");
//                                request.addHeader("Accept"      , "application/json");
//                                break;
//                            default:
//                                request.addHeader("Content-Type", "application/x-www-form-urlencoded");
//                                request.addHeader("Accept"      , "application/octet-stream");
//                                break;
//                        }
//                        getClient().newCall(request.build()).enqueue(new Callback() {
//                            @Override
//                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                                logHelper.d( "url : " + url + " - duration : " + calcTime(startTime) + " second");
//                                new Handler(Looper.getMainLooper()).post(() -> responder.onFailure(ErrorCode.ServerConnectionError));
//                            }
//
//                            @Override
//                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                                if (response.isSuccessful()) {
//                                    ResponseBody body = response.body();
//                                    if (body != null) {
//                                        final byte[] result = body.source().readByteArray();
//                                        logHelper.d("url : " + url + " - duration : " + calcTime(startTime) + " second - size " + getFileSize(result));
//                                        new Handler(Looper.getMainLooper()).post(() -> responder.onSuccess(result));
//                                    }
//                                } else {
//                                    logHelper.d("url : " + url + " - duration : " + calcTime(startTime) + " second");
//                                    new Handler(Looper.getMainLooper()).post(() -> {
//                                        responder.onFailure(ErrorCode.ParseException);
//                                    });
//                                }
//                            }
//                        });
//                    } catch (Exception e) {
//                        new Handler(Looper.getMainLooper()).post(() -> responder.onFailure(ErrorCode.Exception));
//                    }
//                } else {
//                    new Handler(Looper.getMainLooper()).post(() -> responder.onFailure(ErrorCode.AuthorizationException));
//                }
//            });
//        } catch (Exception ex) {
//            logHelper.e("Exception", ex.getMessage());
//            new Handler(Looper.getMainLooper()).post(() -> responder.onFailure(ErrorCode.ParseException));
//        }
//    }
    //endregion

}

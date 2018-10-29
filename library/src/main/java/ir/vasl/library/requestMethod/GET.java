package ir.vasl.library.requestMethod;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;
import ir.vasl.library.Interface.OAuthResponseCallback;
import ir.vasl.library.Interface.ResultHandler;
import ir.vasl.library.enums.ErrorCode;
import ir.vasl.library.helper.general;
import ir.vasl.library.model.AuthModel;
import ir.vasl.library.model.OAuthResponse;
import ir.vasl.library.oauth2.OAuth2Client;
import ir.vasl.library.response.ResponseJsonHandler;
import ir.vasl.library.response.ResponseTextHandler;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GET extends baseMethod {

    public synchronized static void no_Auth(
            @NonNull OkHttpClient client,
            @NonNull final String url,
            @Nullable String tag,
            @NonNull AuthModel authModel,
            @NonNull final ResultHandler responder) {
        final Long startTime = getTimeMillisecond();
        try {
            final Request.Builder request = new Request.Builder()
                    .url(url)
                    .tag(tag)
                    .addHeader("cache-control"  , "no-cache")
                    .addHeader("os", "android")
                    .get();

            switch (authModel.getEncodingType()){
                case GZIP:
                    request.addHeader("Accept-Encoding","gzip");
                    break;
            }
            if (responder instanceof ResponseTextHandler){
                request.addHeader("Content-Type", "application/text");
                request.addHeader("Accept", "application/text");
            }else if (responder instanceof ResponseJsonHandler){
                request.addHeader("Content-Type", "application/json");
                request.addHeader("Accept", "application/json");
            }else {
                request.addHeader("Content-Type", "application/x-www-form-urlencoded");
                request.addHeader("Accept", "application/octet-stream");
            }
            ArrayMap<String, String> headers = authModel.getHeaders();
            for (int index = 0;index < headers.size();index++){
                try {
                    String key   = headers.keyAt(index);
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
                            responder.onFailure(url,startTime,ErrorCode.RuntimeException);
                        }
                    });
                }
            }
            client.newCall(request.build()).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, IOException e) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            responder.onFailure(url,startTime,ErrorCode.ServerConnectionError);
                        }
                    });
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull final Response response) {
                    responder.onResultHandler(startTime,response);
                }
            });
        } catch (Exception ex) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    responder.onFailure(url,startTime,ErrorCode.RuntimeException);
                }
            });
        }
    }

    public synchronized static void basic_Auth(
            @NonNull final OkHttpClient client,
            @NonNull final String url,
            @Nullable final String tag,
            @NonNull final AuthModel authModel,
            @NonNull final ResultHandler responder) {
        final Long startTime = getTimeMillisecond();
        try {
            final ArrayMap<String, String> headers = authModel.getHeaders();
            OAuth2Client auth = new OAuth2Client(client,headers,authModel);
            auth.requestAccessToken(new OAuthResponseCallback() {
                @Override
                public void onResponse(OAuthResponse response) {
                    if (response.isSuccessful()) {
                        switch (authModel.getAuthType()){
                            case BASIC_AUTH:
                                headers.put("Authorization", String.format("Bearer %s", response.getAccessToken()));
                                break;
                        }
                        authModel.setHeaders(headers);
                        no_Auth(client,url,tag,authModel,responder);
                    } else {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                responder.onFailure(url,startTime,ErrorCode.AuthorizationException);
                            }
                        });
                    }
                }
            });
        } catch (Exception ex) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    responder.onFailure(url,startTime,ErrorCode.RuntimeException);
                }
            });
        }
    }

}

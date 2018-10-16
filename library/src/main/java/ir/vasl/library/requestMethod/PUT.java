package ir.vasl.library.requestMethod;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

import java.io.IOException;

import ir.vasl.library.Interface.ResultHandler;
import ir.vasl.library.enums.ErrorCode;
import ir.vasl.library.helper.general;
import ir.vasl.library.model.AuthModel;
import ir.vasl.library.oauth2library.OAuth2Client;
import ir.vasl.library.response.ResponseJsonHandler;
import ir.vasl.library.response.ResponseTextHandler;
import ir.vasl.library.utils.RequestParams;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PUT extends baseMethod {

    public synchronized static void no_Auth(
            @NonNull OkHttpClient client,
            @NonNull String url,
            @NonNull String tag,
            @NonNull AuthModel authModel,
            @NonNull RequestParams params,
            @NonNull ResultHandler responder) {
        final Long startTime = getTimeMillisecond();
        try {
            final Request.Builder request = new Request.Builder()
                    .url(url)
                    .tag(tag)
                    .addHeader("cache-control"  , "no-cache")
                    .addHeader("os"             , "android")
                    .put(params.getRequestForm());

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
                String key   = headers.keyAt(index);
                String value = headers.get(key);
                if (!general.StringIsEmptyOrNull(key) && !general.StringIsEmptyOrNull(value)){
                    request.addHeader(key,value);
                }
            }
            client.newCall(request.build()).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, final IOException e) {
                    new Handler(Looper.getMainLooper()).post(() -> responder.onFailure(url,startTime,ErrorCode.ServerConnectionError));
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull final Response response) {
                    responder.onResultHandler(startTime,response);
                }
            });
        } catch (Exception ex) {
            new Handler(Looper.getMainLooper()).post(() -> responder.onFailure(url,startTime,ErrorCode.RuntimeException));
        }
    }

    public synchronized static void basic_Auth(
            @NonNull OkHttpClient client,
            @NonNull String url,
            @NonNull String tag,
            @NonNull AuthModel authModel,
            @NonNull RequestParams params,
            @NonNull ResultHandler responder) {
        final Long startTime = getTimeMillisecond();
        try {
            ArrayMap<String, String> headers = authModel.getHeaders();
            OAuth2Client auth = new OAuth2Client(client,headers,authModel);
            auth.requestAccessToken(response -> {
                if (response.isSuccessful()) {
                    no_Auth(client,url,tag,authModel,params,responder);
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> responder.onFailure(url,startTime,ErrorCode.AuthorizationException));
                }
            });
        } catch (Exception ex) {
            new Handler(Looper.getMainLooper()).post(() -> responder.onFailure(url,startTime,ErrorCode.RuntimeException));
        }
    }
}

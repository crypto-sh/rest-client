package com.github.library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.collection.ArrayMap;

import com.github.library.enums.AuthType;
import com.github.library.helper.LogHelper;
import com.github.library.model.AuthModel;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

abstract class BaseClient {

    WeakReference<Context> appContext;

    String clientId;
    String clientSecret;
    String site;

    String token;

    String grantType;

    String username;
    String password;

    AuthType authType;

    ArrayMap<String,String> headers;

    int timeMilliSecond = 60;

    boolean debugEnable             = true;

    private static OkHttpClient instance;

    static synchronized OkHttpClient getClient(int timeOut, boolean enableDebug) {
        if (instance == null) {
            instance = new OkHttpClient()
                    .newBuilder()
                    .connectTimeout(timeOut, TimeUnit.MILLISECONDS)
                    .build();

            if (enableDebug) {
                instance.newBuilder()
                        .addInterceptor(new LoggingInterceptor())
                        .build();
            }
        }
        return instance;
    }

    public void cancelAllRequest() {
        for (Call call : getClient(timeMilliSecond,debugEnable).dispatcher().queuedCalls()) {
            call.cancel();
        }
    }

    public void cancelCallWithTag(String tag) {
        for (Call call : getClient(timeMilliSecond,debugEnable).dispatcher().queuedCalls()) {
            try {
                Object item = call.request().tag();
                if (item != null){
                    if (item.equals(tag))
                        call.cancel();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static class LoggingInterceptor implements Interceptor {

        LogHelper logHelper = new LogHelper(RestClient.class);

        @SuppressLint("DefaultLocale")
        @NotNull
        @Override
        public Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
            Request request = chain.request();
            long t1 = System.nanoTime();
            logHelper.d(String.format("Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));
            Response response = chain.proceed(request);
            long t2 = System.nanoTime();
            logHelper.d(String.format("Received response for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers()));
            return response;
        }
    }

    boolean checkNetworkConnection(Context context) {
        try {
            if (context == null) {
                return false;
            }
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager == null) {
                return true;
            }
            NetworkInfo nwInfo = connectivityManager.getActiveNetworkInfo();
            return nwInfo == null || !nwInfo.isConnectedOrConnecting();
        } catch (Exception ex) {
            return true;
        }
    }

    AuthModel getAuthModel(){
        AuthModel auth = new AuthModel();
        auth.setClientId(this.clientId);
        auth.setClientSecret(this.clientSecret);
        auth.setSite(this.site);
        auth.setToken(this.token);
        auth.setGrantType(this.grantType);
        auth.setUsername(this.username);
        auth.setPassword(this.password);
        auth.setAuthType(this.authType);
        auth.setHeaders(this.headers);
        return auth;
    }
}

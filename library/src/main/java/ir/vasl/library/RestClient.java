package ir.vasl.library;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;


import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import ir.vasl.library.Interface.ResultHandler;
import ir.vasl.library.enums.AuthType;
import ir.vasl.library.enums.EncodingType;
import ir.vasl.library.enums.ErrorCode;
import ir.vasl.library.model.AuthModel;
import ir.vasl.library.requestMethod.DELETE;
import ir.vasl.library.requestMethod.GET;
import ir.vasl.library.requestMethod.POST;
import ir.vasl.library.requestMethod.PUT;
import ir.vasl.library.utils.RequestParams;
import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * Created by alishatergholi on 2/20/18.
 */
public class RestClient {

    private static OkHttpClient instance;

    private static synchronized OkHttpClient getClient() {
        if (instance == null) {
            instance = new OkHttpClient()
                    .newBuilder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .build();
        }
        return instance;
    }

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return false;
                }
            });
            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Context appContext;
    private String clientId;
    private String clientSecret;
    private String site;

    private String scope;
    private String grantType;

    private String username;
    private String password;

    private AuthType authType;
    private EncodingType encodingType;
    private ArrayMap<String,String> headers;

    RestClient(Builder builder){
        this.appContext     = builder.appContext;
        this.clientId       = builder.clientId;
        this.clientSecret   = builder.clientSecret;
        this.site           = builder.site;
        this.scope          = builder.scope;
        this.grantType      = builder.grantType;
        this.username       = builder.username;
        this.password       = builder.password;
        this.authType       = builder.authType;
        this.encodingType   = builder.encodingType;
        this.headers        = builder.headers;
    }

    public static class Builder {

        private final Context appContext;
        private String site;

        private String clientId;
        private String clientSecret;

        private String scope;
        private String grantType;

        private String username;
        private String password;

        private AuthType authType               = AuthType.NO_AUTH;
        private EncodingType encodingType       = EncodingType.NO_ENCODING;
        private ArrayMap<String,String> headers = new ArrayMap<>();

        public Builder(Context context){
            this.appContext = context;
        }

        public Builder setAuthorization(String authSite,String clientId,String clientSecret,AuthType type){
            this.site           = authSite;
            this.clientId       = clientId;
            this.clientSecret   = clientSecret;
            this.authType       = type;
            return this;
        }

        public Builder setUserInfo(String username,String password){
            this.username = username;
            this.password = password;
            return this;
        }

        public Builder setGrantType(String grantType) {
            this.grantType = grantType;
            return this;
        }

        public Builder setScope(String scope) {
            this.scope = scope;
            return this;
        }

        public Builder setAcceptEnconding(EncodingType encodingType){
            this.encodingType = encodingType;
            return this;
        }

        public Builder setHeader(ArrayMap<String,String> headers){
            this.headers = headers;
            return this;
        }

        public RestClient build() {
            return new RestClient(this);
        }
    }

    private static boolean checkNetworkConnection(Context context) {
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

    public void POST(
            @NonNull final String url,
            @Nullable final String tag,
            @NonNull final RequestParams params,
            @NonNull final ResultHandler responder){

        if (checkNetworkConnection(this.appContext)) {
            responder.onFailure(url,0,ErrorCode.InternetConnectionError);
            return;
        }
        //region Authorization Model
        AuthModel auth = new AuthModel();
        auth.setClientId(this.clientId);
        auth.setClientSecret(this.clientSecret);
        auth.setSite(this.site);
        auth.setScope(this.scope);
        auth.setGrantType(this.grantType);
        auth.setUsername(this.username);
        auth.setPassword(this.password);
        auth.setAuthType(this.authType);
        auth.setEncodingType(this.encodingType);
        auth.setHeaders(this.headers);
        //endregion
        switch (this.authType){
            case NO_AUTH:
                POST.no_Auth(getClient(), url, tag, auth, params, responder);
                break;
            case BASIC_AUTH:
                POST.basic_Auth(getClient(),url,tag,auth,params,responder);
                break;
        }
    }

    public void GET(
            @NonNull final String url,
            @Nullable final String tag,
            @NonNull final ResultHandler responder){

        if (checkNetworkConnection(this.appContext)) {
            responder.onFailure(url,0,ErrorCode.InternetConnectionError);
            return;
        }
        //region Authorization Model
        AuthModel auth = new AuthModel();
        auth.setClientId(this.clientId);
        auth.setClientSecret(this.clientSecret);
        auth.setSite(this.site);
        auth.setScope(this.scope);
        auth.setGrantType(this.grantType);
        auth.setUsername(this.username);
        auth.setPassword(this.password);
        auth.setAuthType(this.authType);
        auth.setEncodingType(this.encodingType);
        auth.setHeaders(this.headers);
        //endregion
        switch (this.authType){
            case NO_AUTH:
                GET.no_Auth(getClient(), url, tag, auth, responder);
                break;
            case BASIC_AUTH:
                GET.basic_Auth(getClient(),url,tag,auth,responder);
                break;
        }
    }

    public void DELETE(
            @NonNull final String url,
            @Nullable final String tag,
            @NonNull final RequestParams params,
            @NonNull final ResultHandler responder){

        if (checkNetworkConnection(this.appContext)) {
            responder.onFailure(url,0,ErrorCode.InternetConnectionError);
            return;
        }
        //region Authorization Model
        AuthModel auth = new AuthModel();
        auth.setClientId(this.clientId);
        auth.setClientSecret(this.clientSecret);
        auth.setSite(this.site);
        auth.setScope(this.scope);
        auth.setGrantType(this.grantType);
        auth.setUsername(this.username);
        auth.setPassword(this.password);
        auth.setAuthType(this.authType);
        auth.setEncodingType(this.encodingType);
        auth.setHeaders(this.headers);
        //endregion
        switch (this.authType){
            case NO_AUTH:
                DELETE.no_Auth(getClient(), url, tag, auth,params, responder);
                break;
            case BASIC_AUTH:
                DELETE.basic_Auth(getClient(),url,tag,auth,params,responder);
                break;
        }
    }

    public void PUT(
            @NonNull final String url,
            @Nullable final String tag,
            @NonNull final RequestParams params,
            @NonNull final ResultHandler responder){

        if (checkNetworkConnection(this.appContext)) {
            responder.onFailure(url,0,ErrorCode.InternetConnectionError);
            return;
        }
        //region Authorization Model
        AuthModel auth = new AuthModel();
        auth.setClientId(this.clientId);
        auth.setClientSecret(this.clientSecret);
        auth.setSite(this.site);
        auth.setScope(this.scope);
        auth.setGrantType(this.grantType);
        auth.setUsername(this.username);
        auth.setPassword(this.password);
        auth.setAuthType(this.authType);
        auth.setEncodingType(this.encodingType);
        auth.setHeaders(this.headers);
        //endregion
        switch (this.authType){
            case NO_AUTH:
                PUT.no_Auth(getClient(), url, tag, auth,params, responder);
                break;
            case BASIC_AUTH:
                PUT.basic_Auth(getClient(),url,tag,auth,params,responder);
                break;
        }
    }

    public static void cancelAllRequest() {
        for (Call call : getClient().dispatcher().queuedCalls()) {
            call.cancel();
        }
    }

    public static void cancelCallWithTag(String tag) {
        for (Call call : getClient().dispatcher().queuedCalls()) {
            try {
                if (call.request().tag().equals(tag))
                    call.cancel();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

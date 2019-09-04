package com.github.restclient;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArrayMap;

import com.github.library.RestClient;
import com.github.library.enums.RequestBodyType;
import com.github.library.response.ResponseJsonHandler;
import com.github.library.utils.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();

    private RestClient restClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //initRestClientWithCP();
        initRestSamlple();

        findViewById(R.id.buttonSimpleCall).setOnClickListener(view -> {
            callSimpleApi();
        });

        findViewById(R.id.buttonCallWithCP).setOnClickListener(view -> {
            callFourSquare();
        });

    }

    private void initRestClient() {

        ArrayMap<String, String> header = new ArrayMap<>();

        String clientId = "";

        String clientSecret = "";

        String site = "";

        String userName = "";

        String passWord = "";

        header.put("Content-Type", "application/x-www-form-urlencoded");

        restClient = new RestClient
                .Builder(this)
                .setAuthorizationBasic(userName, passWord)
                .setAuthorizationOauth2(site + "/oauth/token", clientId, clientSecret)
                .setDebugEnable(true)
                .setHeader(header)
                .setConnectionTimeOut(15000)
                .setWriteTimeOut(15000)
                .setReadTimeOut(15000)
                .build();
    }

    private void initRestSamlple() {
        ArrayMap<String, String> header = new ArrayMap<>();

        String clientId = "";

        String clientSecret = "";

        String site = "";

        String userName = "";

        String passWord = "";

        header.put("Content-Type", "application/x-www-form-urlencoded");

        restClient = new RestClient
                .Builder(this)
                .setHeader(header)
                .setConnectionTimeOut(15000)
                 .setWriteTimeOut(15000)
                .setReadTimeOut(15000)
                .build();
    }

    private void initRestClientWithCP() {

        ArrayMap<String, String> header = new ArrayMap<>();

        String clientId = "";

        String clientSecret = "";

        String site = "";

        String userName = "";

        String passWord = "";

        header.put("Content-Type", "application/x-www-form-urlencoded");

        ArrayMap<String, String> CPKArray = new ArrayMap<>();
        CPKArray.put("host name", "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=");

        restClient = new RestClient
                .Builder(this)
                .setAuthorizationBasic(userName, passWord)
                .setAuthorizationOauth2(site + "/oauth/token", clientId, clientSecret)
                .setDebugEnable(true)
                .setHeader(header)
                .setCPKArray(CPKArray)
                .setConnectionTimeOut(15000)
                .build();
    }

    private void callSimpleApi() {
        RequestParams params = new RequestParams(RequestBodyType.FormData);
        restClient.GET("http://nestle.koosha.ir/User/UserLogin.ashx?Username=MahdiPiran&Password=12345678&",
                "",
                new ResponseJsonHandler() {
                    @Override
                    protected void onSuccess(JSONObject result) {
                        Log.d(TAG, "response " + result);
                    }

                    @Override
                    protected void onSuccess(JSONArray result) {
                        Log.d(TAG, "response " + result);
                    }

                    @Override
                    protected void onSuccess(String result) {
                        Log.d(TAG, "response " + result);
                    }

                    @Override
                    public void onFailure(int errorCode, String errorMsg) {
                        Log.d(TAG, "onFailure " + errorMsg);
                    }
                });
    }

    private void callFourSquare() {
        RequestParams params = new RequestParams(RequestBodyType.FormData);
        restClient.GET("https://api.foursquare.com/v2/venues/explore?client_id=4MZ41IL2EPQQ5IERZHXKDWV2YGEWOCAFOXNAYOB4C3NIMXPH&client_secret=KPMQITFTATZGBHSXIHBADUKSO1VWEQS1URFN51LMDZTAIQUA&v=20190704&ll=35.703338,51.353264&offset=20&limit=10",
                "",
                new ResponseJsonHandler() {
                    @Override
                    protected void onSuccess(JSONObject result) {
                        Log.d(TAG, "response " + result);
                    }

                    @Override
                    protected void onSuccess(JSONArray result) {
                        Log.d(TAG, "response " + result);
                    }

                    @Override
                    protected void onSuccess(String result) {
                        Log.d(TAG, "response " + result);
                    }

                    @Override
                    public void onFailure(int errorCode, String errorMsg) {
                        Log.d(TAG, "onFailure " + errorMsg);
                    }
                });
    }

    private void callCPApi() {
        RequestParams params = new RequestParams(RequestBodyType.FormData);
        restClient.GET("",
                "",
                new ResponseJsonHandler() {
                    @Override
                    protected void onSuccess(JSONObject result) {
                        Log.d(TAG, "response " + result);
                    }

                    @Override
                    protected void onSuccess(JSONArray result) {
                        Log.d(TAG, "response " + result);
                    }

                    @Override
                    protected void onSuccess(String result) {
                        Log.d(TAG, "response " + result);
                    }

                    @Override
                    public void onFailure(int errorCode, String errorMsg) {
                        Log.d(TAG, "onFailure " + errorMsg);
                    }
                });
    }
}

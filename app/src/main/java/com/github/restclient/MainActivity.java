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

        initRestClient();

        findViewById(R.id.buttonSimpleCall).setOnClickListener(view -> {
            callSimpleApi();
        });

        findViewById(R.id.buttonCallWithCP).setOnClickListener(view -> {
            callCPApi();
        });
    }

    private void initRestClient() {
        ArrayMap<String, String> header = new ArrayMap<>();

        String clientId = "c3bdf6c5-508f-48ae-9af4-243a24072e31";

        String clientSecret = "LnDbEo3yDDcswKMC3h4H";

        String site = "http://sandbox.vaslapp.com";

        String userName = "android-XoaM8ODAYVcKnB16ob8N";

        String passWord = "DOI0qOIa0KT6ViYmS1k6";

        header.put("Content-Type", "application/x-www-form-urlencoded");

        restClient = new RestClient.Builder(this)
                .setAuthorizationOauth2("https://accounts.spotify.com/api/token", clientId, clientSecret)
                .setDebugEnable(true)
                .setHeader(header)
                .build();
    }

    private void callSimpleApi() {
        RequestParams params = new RequestParams(RequestBodyType.FormData);
        restClient.GET("https://api.spotify.com/v1/tracks/2TpxZ7JUBn3uw46aR7qd6V",
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

    }
}

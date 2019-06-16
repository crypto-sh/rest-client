package com.github.restclient;


import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import com.github.library.RestClient;
import com.github.library.enums.AuthType;
import com.github.library.enums.RequestBodyType;
import com.github.library.response.ResponseJsonHandler;
import com.github.library.utils.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArrayMap;


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


        findViewById(R.id.text).setOnClickListener(view -> {
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
        });
    }

    private void initRestClient() {
        ArrayMap<String, String> header = new ArrayMap<>();

        String clientId     = "50ff9f0569f0439087bbc05acb38db43";
        String clientSecret = "785e208d5eef44a28941bf47146c0873";

        header.put("Content-Type", "application/x-www-form-urlencoded");

        restClient = new RestClient.Builder(this)
                .setAuthorizationOauth2("https://accounts.spotify.com/api/token",clientId,clientSecret)
                .setDebugEnable(true)
                .setHeader(header)
                .build();


    }
}

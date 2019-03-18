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

        //{"album":{"album_type":"album","artists":[{"external_urls":{"spotify":"https:\/\/open.spotify.com\/artist\/08td7MxkoHQkXnWAYD8d6Q"},"href":"https:\/\/api.spotify.com\/v1\/artists\/08td7MxkoHQkXnWAYD8d6Q","id":"08td7MxkoHQkXnWAYD8d6Q","name":"Tania Bowra","type":"artist","uri":"spotify:artist:08td7MxkoHQkXnWAYD8d6Q"}],"available_markets":["AD","AE","AR","AT","AU","BE","BG","BH","BO","BR","CA","CH","CL","CO","CR","CY","CZ","DE","DK","DO","DZ","EC","EE","EG","ES","FI","FR","GB","GR","GT","HK","HN","HU","ID","IE","IL","IN","IS","IT","JO","JP","KW","LB","LI","LT","LU","LV","MA","MC","MT","MX","MY","NI","NL","NO","NZ","OM","PA","PE","PH","PL","PS","PT","PY","QA","RO","SA","SE","SG","SK","SV","TH","TN","TR","TW","US","UY","VN","ZA"],"external_urls":{"spotify":"https:\/\/open.spotify.com\/album\/6akEvsycLGftJxYudPjmqK"},"href":"https:\/\/api.spotify.com\/v1\/albums\/6akEvsycLGftJxYudPjmqK","id":"6akEvsycLGftJxYudPjmqK","images":[{"height":640,"url":"https:\/\/i.scdn.co\/image\/a529b65b4bd322b16bee34672ce45278e890e176","width":640},{"height":300,"url":"https:\/\/i.scdn.co\/image\/985cc10acdbbedb6a16d7c74f9e23553e2b28dbc","width":300},{"height":64,"url":"https:\/\/i.scdn.co\/image\/37b46a2662c09502885d1804c1c865b199cc3d67","width":64}],"name":"Place In The Sun","release_date":"2004-02-02","release_date_precision":"day","total_tracks":11,"type":"album","uri":"spotify:album:6akEvsycLGftJxYudPjmqK"},"artists":[{"external_urls":{"spotify":"https:\/\/open.spotify.com\/artist\/08td7MxkoHQkXnWAYD8d6Q"},"href":"https:\/\/api.spotify.com\/v1\/artists\/08td7MxkoHQkXnWAYD8d6Q","id":"08td7MxkoHQkXnWAYD8d6Q","name":"Tania Bowra","type":"artist","uri":"spotify:artist:08td7MxkoHQkXnWAYD8d6Q"}],"available_markets":["AD","AE","AR","AT","AU","BE","BG","BH","BO","BR","CA","CH","CL","CO","CR","CY","CZ","DE","DK","DO","DZ","EC","EE","EG","ES","FI","FR","GB","GR","GT","HK","HN","HU","ID","IE","IL","IN","IS","IT","JO","JP","KW","LB","LI","LT","LU","LV","MA","MC","MT","MX","MY","NI","NL","NO","NZ","OM","PA","PE","PH","PL","PS","PT","PY","QA","RO","SA","SE","SG","SK","SV","TH","TN","TR","TW","US","UY","VN","ZA"],"disc_number":1,"duration_ms":276773,"explicit":false,"external_ids":{"isrc":"AUCR10410001"},"external_urls":{"spotify":"https:\/\/open.spotify.com\/track\/2TpxZ7JUBn3uw46aR7qd6V"},"href":"https:\/\/api.spotify.com\/v1\/tracks\/2TpxZ7JUBn3uw46aR7qd6V","id":"2TpxZ7JUBn3uw46aR7qd6V","is_local":false,"name":"All I Want","popularity":1,"preview_url":"https:\/\/p.scdn.co\/mp3-preview\/12b8cee72118f995f5494e1b34251e4ac997445e?cid=50ff9f0569f0439087bbc05acb38db43","track_number":1,"type":"track","uri":"spotify:track:2TpxZ7JUBn3uw46aR7qd6V"}

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
        header.put("Content-Type", "application/x-www-form-urlencoded");
        restClient = new RestClient.Builder(this)
                .setAuthorization(
                        "",
                        "",
                        "",
                        AuthType.BASIC_AUTH)
                .setDebugEnable(true)
                .setHeader(header)
                .build();


    }
}

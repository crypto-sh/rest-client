package ir.vasl.restclient;

import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import ir.vasl.library.RestClient;

import ir.vasl.library.enums.EncodingType;

import ir.vasl.library.response.ResponseTextHandler;
import ir.vasl.library.utils.RequestParams;


public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private RestClient restClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.text).setOnClickListener(view -> {
            restClient.POST("http://test.vaslapp.com/taniyar/services/api/v1/user/home",
                    "",
                    new RequestParams(),
                    new ResponseTextHandler() {
                        @Override
                        protected void onSuccess(String result) {
                            Log.d(TAG,result);
                        }

                        @Override
                        public void onProgress(double percent, long bytesWritten, long totalSize) {
                            super.onProgress(percent, bytesWritten, totalSize);
                            Log.d(TAG,"percent " + percent);
                        }

                        @Override
                        public void onFailure(int errorCode, String errorMsg) {
                            Log.d(TAG,"onFailure " + errorMsg);
                        }
                    }
            );
        });

        ArrayMap<String,String> header = new ArrayMap<>();
        header.put("appid","0e8f8fd2-1acb-11e7-8ab0-ac162d7938f0");
        header.put("accept-language", "fa");

        restClient = new RestClient.Builder(this)
                .setAcceptEnconding(EncodingType.GZIP)
                .setHeader(header)
                .build();
    }
}

package com.github.library.response;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import com.github.library.Interface.ResultHandler;
import com.github.library.enums.ErrorCode;


/**
 * Created by alishatergholi on 12/16/17.
 */
public abstract class ResponseJsonHandler extends ResultHandler {

    String data = "";

    @Override
    protected void onSuccess(String url,long startTime, byte[] result) {
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                data = new String(result, StandardCharsets.UTF_8);
            }else {
                data = new String(result,"UTF-8");
            }
            logHelper.d("url : " + url + " - time : " + calcTime(startTime) + " size : " + calcFileSize(result));
            try {
                final Object object = new JSONTokener(data).nextValue();
                if (object instanceof JSONObject){
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            onSuccess((JSONObject) object);
                        }
                    });
                }else if (object instanceof JSONArray){
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            onSuccess((JSONArray) object);
                        }
                    });
                }else {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            onSuccess(data);
                        }
                    });
                }
            } catch (JSONException e) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        onSuccess(data);
                    }
                });
            }
        } catch (UnsupportedEncodingException e) {
            this.onFailure(url,startTime,ErrorCode.UnsupportedEncodingException);
        }
    }

    @Override
    public void onProgress(final double percent, final long bytesWritten, final long totalSize) {
        super.onProgress(percent, bytesWritten, totalSize);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                onProgress(percent,bytesWritten,totalSize);
            }
        });
    }

    @Override
    public void onFailure(String url, long startTime, final ErrorCode errorCode) {
        logHelper.d("url : " + url + " - time : " + calcTime(startTime));
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                onFailure(errorCode.getCode(),errorCode.getDescription());
            }
        });
    }

    protected void onSuccess(JSONObject result){}
    protected void onSuccess(JSONArray result){}
    protected abstract void onSuccess(String result);

    public abstract void onFailure(int errorCode,String errorMsg);



}

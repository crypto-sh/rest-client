package ir.vasl.library.response;

import android.os.Handler;
import android.os.Looper;

import java.io.UnsupportedEncodingException;

import ir.vasl.library.Interface.ResultHandler;
import ir.vasl.library.enums.ErrorCode;


/**
 * Created by alishatergholi on 12/16/17.
 */
public abstract class ResponseTextHandler extends ResultHandler {

    @Override
    protected void onSuccess(String url,long startTime, byte[] result) {
        try {
            final String value = new String(result,"UTF-8");
            logHelper.d("url : " + url + " - time : " + calcTime(startTime) + " size : " + calcFileSize(result));
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    onSuccess(value);
                }
            });
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

    protected abstract void onSuccess(String result);

    public abstract void onFailure(int errorCode,String errorMsg);



}

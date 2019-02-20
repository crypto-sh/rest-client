package com.github.library.response;

import android.os.Handler;
import android.os.Looper;

import java.io.UnsupportedEncodingException;

import com.github.library.Interface.ResultHandler;
import com.github.library.enums.ErrorCode;


/**
 * Created by alishatergholi on 12/16/17.
 */
public abstract class ResponseTextHandler extends ResultHandler {

    @Override
    protected void onSuccess(String url, byte[] result) {
        try {
            final String value = new String(result,"UTF-8");
            new Handler(Looper.getMainLooper()).post(() -> onSuccess(value));
        } catch (UnsupportedEncodingException e) {
            this.onFailure(url,ErrorCode.UnsupportedEncodingException);
        }
    }

    @Override
    public void onProgress(final double percent, final long bytesWritten, final long totalSize) {
        super.onProgress(percent, bytesWritten, totalSize);
        new Handler(Looper.getMainLooper()).post(() -> onProgress(percent,bytesWritten,totalSize));
    }

    @Override
    public void onFailure(String url, final ErrorCode errorCode) {
        new Handler(Looper.getMainLooper()).post(() -> onFailure(errorCode.getCode(),errorCode.getDescription()));
    }

    protected abstract void onSuccess(String result);

    public abstract void onFailure(int errorCode,String errorMsg);



}

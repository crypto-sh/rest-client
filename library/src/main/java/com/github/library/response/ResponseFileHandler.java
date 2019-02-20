package com.github.library.response;

import android.os.Handler;
import android.os.Looper;

import com.github.library.Interface.ResultHandler;
import com.github.library.enums.ErrorCode;


/**
 * Created by alishatergholi on 12/16/17.
 */
public abstract class ResponseFileHandler extends ResultHandler {

    @Override
    protected void onSuccess(String url, byte[] result) {
        new Handler(Looper.getMainLooper()).post(() -> onSuccess(result));
    }

    @Override
    public void onProgress(final double percent, final long bytesWritten, final long totalSize) {
        super.onProgress(percent, bytesWritten, totalSize);
        new Handler(Looper.getMainLooper()).post(() -> onProgress(percent, bytesWritten, totalSize));
    }

    @Override
    public void onFailure(String url, final ErrorCode errorCode) {
        new Handler(Looper.getMainLooper()).post(() -> onFailure(errorCode.getCode(), errorCode.getDescription()));
    }

    protected abstract void onSuccess(byte[] result);

    public abstract void onFailure(int errorCode, String errorMsg);


}
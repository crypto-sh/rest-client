package ir.vasl.library.response;

import android.os.Handler;
import android.os.Looper;

import ir.vasl.library.Interface.ResultHandler;
import ir.vasl.library.enums.ErrorCode;


/**
 * Created by alishatergholi on 12/16/17.
 */
public abstract class ResponseFileHandler extends ResultHandler {

    @Override
    protected void onSuccess(String url, long startTime, byte[] result) {
        logHelper.d("url : " + url + " - time : " + calcTime(startTime) + " size : " + calcFileSize(result));
        new Handler(Looper.getMainLooper()).post(() -> this.onSuccess(result));
    }

    @Override
    public void onProgress(double percent, long bytesWritten, long totalSize) {
        super.onProgress(percent, bytesWritten, totalSize);
        new Handler(Looper.getMainLooper()).post(() -> onProgress(percent, bytesWritten, totalSize));
    }

    @Override
    public void onFailure(String url, long startTime, ErrorCode errorCode) {
        logHelper.d("url : " + url + " - time : " + calcTime(startTime));
        new Handler(Looper.getMainLooper()).post(() -> onFailure(errorCode.getCode(), errorCode.getDescription()));
    }

    protected abstract void onSuccess(byte[] result);

    public abstract void onFailure(int errorCode, String errorMsg);


}

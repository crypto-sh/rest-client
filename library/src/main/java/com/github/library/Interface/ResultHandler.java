package com.github.library.Interface;

import com.github.library.enums.ErrorCode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by alishatergholi on 12/16/17.
 */

public abstract class ResultHandler {

    public void onResultHandler(Response response) {
        String url = response.request().url().url().toString();
        if (response.body() != null && response.isSuccessful()) {
            ResponseBody body = response.body();
            InputStream inputStream = body.byteStream();
            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                int next = inputStream.read();
                while (next > -1) {
                    outputStream.write(next);
                    next = inputStream.read();
                }
                outputStream.flush();
                byte[] result = outputStream.toByteArray();
                outputStream.close();
                this.onSuccess(url, result);
            } catch (IOException e) {
                this.onFailure(url, ErrorCode.IOException);
            }
        } else {
            this.onFailure(url, ErrorCode.Parse(response.code()));
        }
    }

    protected abstract void onSuccess(String url, byte[] result);

    public void onProgress(double percent, long bytesWritten, long totalSize) {

    }

    protected void onProgress(final double percent, final long bytesWritten, final long totalSize, final int speed) {
    }

    public abstract void onFailure(String url, ErrorCode errorCode);

    protected static String calcTime(Long startTime) {
        Long duration = getTimeMillisecond() - startTime;
        return String.valueOf(TimeUnit.MILLISECONDS.toSeconds(duration)) + "." + String.valueOf(TimeUnit.MILLISECONDS.toMillis(duration) % 1000).substring(0, 1);
    }

    protected static Long getTimeMillisecond() {
        return System.currentTimeMillis();
    }

    protected static String calcFileSize(byte[] result) {
        String modifiedFileSize = null;
        double fileSize = 0.0;
        fileSize = result.length;//in Bytes
        if (fileSize < 1024) {
            modifiedFileSize = String.valueOf(fileSize).concat(" B");
        } else if (fileSize > 1024 && fileSize < (1024 * 1024)) {
            modifiedFileSize = String.valueOf(Math.round((fileSize / 1024 * 100.0)) / 100.0).concat(" KB");
        } else {
            modifiedFileSize = String.valueOf(Math.round((fileSize / (1024 * 1204) * 100.0)) / 100.0).concat(" MB");
        }
        return modifiedFileSize;
    }

}

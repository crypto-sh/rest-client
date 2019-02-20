package com.github.library.requestMethod;

import com.github.library.RestClient;
import com.github.library.helper.LogHelper;

import java.util.concurrent.TimeUnit;


public abstract class baseMethod {

    LogHelper logHelper = new LogHelper(RestClient.class);

    protected static String calcTime(Long startTime) {
        Long duration = getTimeMillisecond() - startTime;
        return String.valueOf(TimeUnit.MILLISECONDS.toSeconds(duration)) + "." + String.valueOf(TimeUnit.MILLISECONDS.toMillis(duration) % 1000).substring(0, 1);
    }

    protected static Long getTimeMillisecond() {
        return System.currentTimeMillis();
    }



}

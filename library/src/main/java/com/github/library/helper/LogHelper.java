package com.github.library.helper;

import android.util.Log;

import com.github.library.BuildConfig;
import com.github.library.RestClient;


public class LogHelper {

    private String LOG_TAG = "Rest-Client";


    private static final int MAX_LOG_TAG_LENGTH = 23;

    public LogHelper(Class cls) {
        LOG_TAG = cls.getSimpleName();
        if (LOG_TAG.length() > MAX_LOG_TAG_LENGTH) {
            LOG_TAG = LOG_TAG.substring(0, MAX_LOG_TAG_LENGTH - 1);
        }
    }

    public void v(String messages) {
        // Only log VERBOSE if build type is DEBUG
        if (BuildConfig.enableDebugLogging) {
            log(LOG_TAG, Log.VERBOSE, null, messages);
        }
    }

    public void d(String messages) {
        // Only log DEBUG if build type is DEBUG
        if (BuildConfig.enableDebugLogging) {
            log(LOG_TAG, Log.DEBUG, null, messages);
        }
    }

    public void d(String method, String messages) {
        // Only log DEBUG if build type is DEBUG
        if (BuildConfig.enableDebugLogging) {
            log(LOG_TAG, Log.DEBUG, null, method + " : " + messages);
        }
    }

    public void i(String messages) {
        log(LOG_TAG, Log.INFO, null, messages);
    }

    public void w(String messages) {
        log(LOG_TAG, Log.WARN, null, messages);
    }

    public void w(Throwable t, String messages) {
        log(LOG_TAG, Log.WARN, t, messages);
    }

    public void e(Exception e) {
        log(LOG_TAG, Log.ERROR, null, e.getMessage());
    }

    public void e(String method, Exception e) {
        log(LOG_TAG, Log.ERROR, null, method + " : " + e.getMessage());
    }

    public void e(String e) {
        log(LOG_TAG, Log.ERROR, null, e);
    }

    public void e(String method, String e) {
        log(LOG_TAG, Log.ERROR, null, method + " : " + e);
    }

    public void e(Throwable t, String messages) {
        log(LOG_TAG, Log.ERROR, t, messages);
    }

    private void log(String tag, int level, Throwable t, String messages) {
        String msg;
        if (!general.StringIsEmptyOrNull(messages)) {
            if (t == null) {
                msg = messages;
                Log.println(level, tag, msg);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(messages);
                sb.append("\n").append(Log.getStackTraceString(t));
                msg = sb.toString();
                Log.println(level, tag, msg);
            }
        }
    }
}
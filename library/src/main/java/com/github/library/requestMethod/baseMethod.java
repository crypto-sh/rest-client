package com.github.library.requestMethod;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.concurrent.TimeUnit;


import com.github.library.helper.LogHelper;


public abstract class baseMethod {


    protected static String calcTime(Long startTime) {
        Long duration = getTimeMillisecond() - startTime;
        return String.valueOf(TimeUnit.MILLISECONDS.toSeconds(duration)) + "." + String.valueOf(TimeUnit.MILLISECONDS.toMillis(duration) % 1000).substring(0, 1);
    }

    protected static Long getTimeMillisecond() {
        return System.currentTimeMillis();
    }



}

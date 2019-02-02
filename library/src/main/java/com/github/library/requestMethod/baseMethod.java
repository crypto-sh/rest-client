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

    private static LogHelper logHelper = new LogHelper(baseMethod.class);

    protected static String calcTime(Long startTime) {
        Long duration = getTimeMillisecond() - startTime;
        return String.valueOf(TimeUnit.MILLISECONDS.toSeconds(duration)) + "." + String.valueOf(TimeUnit.MILLISECONDS.toMillis(duration) % 1000).substring(0, 1);
    }

    protected static Long getTimeMillisecond() {
        return System.currentTimeMillis();
    }

    protected static String getMimeType(Context context, Uri url) {
        String type = null;
        try {
            if (url.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
                ContentResolver cr = context.getContentResolver();
                type = cr.getType(url);
            } else {
                String fileExtension = MimeTypeMap.getFileExtensionFromUrl(url.toString());
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
            }
        } catch (Exception e) {
            logHelper.e("getMimeType", e.getMessage());
        }
        return type;
    }

    protected static String getFileName(String filepath) {

        try {
            return filepath.substring(filepath.lastIndexOf("/") + 1);
        } catch (Exception e) {
            logHelper.e("getFileName",e);
            return "";
        }
    }

    protected static byte[] ImageToBytesCompressed(File file) {
        try {
            byte[] bytesArray = new byte[(int) file.length()];
            try {
                FileInputStream fis = new FileInputStream(file);
                fis.read(bytesArray); //read file into bytes[]
                fis.close();

            } catch (FileNotFoundException ex) {
                logHelper.e("ImageToBytesCompressed",ex);
                return null;
            } catch (IOException ex) {
                logHelper.e("ImageToBytesCompressed",ex);
                return null;
            }
            return bytesArray;
        } catch (Exception e) {
            logHelper.e("ImageToBytesCompressed",e);
        }
        return null;
    }

}

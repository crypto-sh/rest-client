package com.github.library;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import com.github.library.helper.LogHelper;
import com.github.library.requestMethod.baseMethod;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class RestClientHelper {

    private static LogHelper logHelper = new LogHelper(baseMethod.class);

    public static String getMimeType(WeakReference<Context> context, Uri url) {
        String type = null;
        try {
            if (url.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
                ContentResolver cr = context.get().getContentResolver();
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

    public static String getFileName(String filepath) {

        try {
            return filepath.substring(filepath.lastIndexOf("/") + 1);
        } catch (Exception e) {
            logHelper.e("getFileName", e);
            return "";
        }
    }

    public static byte[] ImageToBytes(File file) throws IOException {
        byte[] bytesArray = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(bytesArray);
        fis.close();
        return bytesArray;
    }
}

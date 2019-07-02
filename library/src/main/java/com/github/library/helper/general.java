package com.github.library.helper;

import androidx.collection.ArrayMap;

import java.util.Map;

import okhttp3.CertificatePinner;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class general {

    public static Boolean StringIsEmptyOrNull(String string) {
        try {
            if (string == null || string.length() == 0 || string.equals("null"))
                return true;
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean isJsonResponse(Response response) {
        ResponseBody body = response.body();
        if (body == null) {
            return false;
        }
        MediaType content = body.contentType();
        if (content == null) {
            return false;
        }
        return content.subtype().equals("json");
    }

    public static void postAddIfValid(FormBody.Builder formBodyBuilder, Map<String, String> params) {
        if (params == null) return;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (isValid(entry.getValue())) {
                formBodyBuilder.add(entry.getKey(), entry.getValue());
            }
        }
    }

    private static boolean isValid(String s) {
        return (s != null && s.trim().length() > 0);
    }

    public static CertificatePinner getCertificatePinner(ArrayMap<String, String> CPKArray) {

        if (CPKArray == null || CPKArray.isEmpty())
            return null;

        CertificatePinner.Builder certificatePinner = new CertificatePinner.Builder();
        for (Map.Entry<String, String> entry : CPKArray.entrySet()) {
            certificatePinner.add(entry.getKey(), entry.getValue());
        }

        return certificatePinner.build();
    }
}

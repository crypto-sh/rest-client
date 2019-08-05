package com.github.library.utils;


import com.github.library.enums.RequestBodyType;
import com.github.library.helper.LogHelper;
import com.github.library.helper.general;
import com.github.library.model.FileModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


/**
 * Created by alishatergholi on 2/21/18.
 */
public class RequestParams {

    private String plainText;

    private String contentType;

    private byte[] binaryInput;

    private LinkedHashMap<String, Object> params = new LinkedHashMap<>();

    private LinkedHashMap<String, FileModel> fileParams = new LinkedHashMap<>();

    private LinkedHashMap<String, List<Object>> paramsArray = new LinkedHashMap<>();

    private RequestBodyType type;

    private LogHelper logHelper = new LogHelper(RequestBody.class);

    public RequestParams() {
        this.type = RequestBodyType.FormUrlEncode;
    }

    public RequestParams(RequestBodyType type) {
        this.type = type;
    }

    public void put(String plainText) {
        this.plainText = plainText;
    }

    public void put(String contentType, byte[] binaryInput) {
        this.contentType = contentType;
        this.binaryInput = binaryInput;
    }

    public void put(String key, Object value) {
        if (general.StringIsEmptyOrNull(key)) {
            return;
        }
        params.put(key, value);
    }

    public void put(String key, String value) {
        if (general.StringIsEmptyOrNull(key) || general.StringIsEmptyOrNull(value)) {
            return;
        }
        params.put(key, value);
    }

    public void put(String key, Integer value) {
        if (general.StringIsEmptyOrNull(key) || value == null) {
            return;
        }
        params.put(key, value);
    }

    public void put(String key, Long value) {
        if (general.StringIsEmptyOrNull(key) || value == null) {
            return;
        }
        params.put(key, value);
    }

    public void put(String key, Float value) {
        if (general.StringIsEmptyOrNull(key) || value == null) {
            return;
        }
        params.put(key, value);
    }

    public void put(String key, Double value) {
        if (general.StringIsEmptyOrNull(key) || value == null) {
            return;
        }
        params.put(key, value);
    }

    public void put(String key, FileModel value) {
        if (general.StringIsEmptyOrNull(key) || value == null) {
            return;
        }
        fileParams.put(key, value);
    }

    public void put(String key, boolean value) {
        if (general.StringIsEmptyOrNull(key)) {
            return;
        }
        params.put(key, value);
    }

    public void put(String key, JSONObject value) {
        if (general.StringIsEmptyOrNull(key) || value == null) {
            return;
        }
        params.put(key, value);
    }

    public void put(String key, List<Object> values) {
        if (general.StringIsEmptyOrNull(key) || values == null) {
            return;
        }
        if (paramsArray.get(key) != null) {
            List<Object> items = paramsArray.get(key);
            if (items != null)
                values.addAll(items);
        }
        paramsArray.put(key, values);
    }

    public void add(String key, List<Object> values) {
        if (general.StringIsEmptyOrNull(key) || values == null) {
            return;
        }
        if (paramsArray.get(key) != null) {
            values.addAll(Objects.requireNonNull(paramsArray.get(key)));
        }
        paramsArray.put(key, values);
    }

    public void putContent(String key, List<Object> values) {
        if (general.StringIsEmptyOrNull(key) || values == null) {
            return;
        }
        ArrayList<Object> items = new ArrayList<>();
        if (paramsArray.get(key) != null) {
            items.addAll(Objects.requireNonNull(paramsArray.get(key)));
        }
        for (Object item : values) {
            items.add(item.toString());
        }
        paramsArray.put(key, items);
    }

    public void add(String key, String value) {
        if (general.StringIsEmptyOrNull(key) || value == null) {
            return;
        }
        ArrayList<Object> values;
        if (paramsArray.get(key) != null) {
            values = (ArrayList<Object>) paramsArray.get(key);
            if (values != null) {
                values.add(value);
            }
        } else {
            values = new ArrayList<>();
            values.add(value);
        }
        paramsArray.put(key, values);
    }

    public RequestBody getRequestForm() {
        if (getFileParams().size() > 0) {
            //for post File You should choose Multipart Type
            type = RequestBodyType.MultiPart;
        }
        switch (type) {
            case RawTEXTPlain:
                return RequestBody.create(MediaType.parse("text/plain; charset=utf-8"), plainText);
            case RawJSON:
                JSONObject object = new JSONObject();
                try {
                    for (String key : getParams().keySet()) {
                        object.put(key, getParams().get(key));
                    }
                    for (String key : getParamsArray().keySet()) {
                        for (Object item : Objects.requireNonNull(getParamsArray().get(key))) {
                            object.put(key, item);
                        }
                    }
                } catch (JSONException e) {
                    logHelper.e(e);
                }
                return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), object.toString());
            case Binary:
                return RequestBody.create(MediaType.parse(contentType), binaryInput);
            case MultiPart:
                MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
                for (String key : getParams().keySet()) {
                    requestBody.addFormDataPart(key, (String) Objects.requireNonNull(getParams().get(key)));
                }
                for (String key : getParamsArray().keySet()) {
                    for (Object item : Objects.requireNonNull(getParamsArray().get(key))) {
                        requestBody.addFormDataPart(key, (String) item);
                    }
                }
                for (String key : getFileParams().keySet()) {
                    FileModel uploadFile = fileParams.get(key);
                    if (uploadFile != null) {
                        requestBody.addFormDataPart(key, uploadFile.getFileName(), RequestBody.create(uploadFile.getMimeType(), uploadFile.getFile()));
                    }
                }
                return requestBody.build();
            case FormData:
            case FormUrlEncode:
            default: {
                FormBody.Builder formBody = new FormBody.Builder();
                for (String key : params.keySet()) {

                    formBody.addEncoded(key, String.valueOf(params.get(key)));
                }
                for (String key : paramsArray.keySet()) {
                    for (Object item : Objects.requireNonNull(paramsArray.get(key))) {
                        formBody.addEncoded(key, item.toString());
                    }
                }
                return formBody.build();
            }
        }
    }

    public RequestBodyType getType() {
        return type;
    }

    private LinkedHashMap<String, Object> getParams() {
        return params;
    }

    private LinkedHashMap<String, FileModel> getFileParams() {
        return fileParams;
    }

    private LinkedHashMap<String, List<Object>> getParamsArray() {
        return paramsArray;
    }

    public void setParamsArray(LinkedHashMap<String, List<Object>> paramsArray) {
        if (paramsArray == null) {
            return;
        }
        this.paramsArray = paramsArray;
    }
}


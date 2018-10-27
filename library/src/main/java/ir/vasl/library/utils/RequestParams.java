package ir.vasl.library.utils;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import ir.vasl.library.enums.RequestBodyType;
import ir.vasl.library.helper.LogHelper;
import ir.vasl.library.helper.general;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;


/**
 * Created by alishatergholi on 2/21/18.
 */
public class RequestParams {

    private String plainText;

    private String contentType;

    private byte[] binaryInput;

    private LinkedHashMap<String, Object> params = new LinkedHashMap<>();

    private LinkedHashMap<String,File> fileParams = new LinkedHashMap<>();

    private LinkedHashMap<String, List<Object>> paramsArray = new LinkedHashMap<>();

    private RequestBodyType type = RequestBodyType.FormData;

    LogHelper logHelper = new LogHelper(RequestBody.class);

    public RequestParams() {

    }

    public RequestParams(RequestBodyType type) {
        this.type = type;
    }

    public void put(String plainText){
        this.plainText = plainText;
    }

    public void put(String contentType,byte[] binaryInput){
        this.contentType = contentType;
        this.binaryInput = binaryInput;
    }

    public void put(String key, String value) {
        if (general.StringIsEmptyOrNull(key) || general.StringIsEmptyOrNull(value)){
            return;
        }
        params.put(key, value);
    }

    public void put(String key, Integer value) {
        if (general.StringIsEmptyOrNull(key) ||  value == null){
            return;
        }
        params.put(key, value);
    }

    public void put(String key, Float value) {
        if (general.StringIsEmptyOrNull(key) ||  value == null){
            return;
        }
        params.put(key, value);
    }

    public void put(String key, Double value) {
        if (general.StringIsEmptyOrNull(key) ||  value == null){
            return;
        }
        params.put(key, value);
    }

    public void put(String key, File value){
        if (general.StringIsEmptyOrNull(key) ||  value == null){
            return;
        }
        fileParams.put(key,value);
    }

    public void put(String key, boolean value) {
        if (general.StringIsEmptyOrNull(key)){
            return;
        }
        params.put(key, value);
    }

    public void put(String key, JSONObject value) {
        if (general.StringIsEmptyOrNull(key) ||  value == null){
            return;
        }
        params.put(key, value);
    }

    public void put(String key, List<Object> values) {
        if (general.StringIsEmptyOrNull(key) ||  values == null){
            return;
        }
        if (paramsArray.get(key) != null) {
            List<Object> items = paramsArray.get(key);
            values.addAll(items);
        }
        paramsArray.put(key, values);
    }

    public void add(String key, List<Object> values){
        if (general.StringIsEmptyOrNull(key) ||  values == null){
            return;
        }
        if (paramsArray.get(key) != null){
            values.addAll(paramsArray.get(key));
        }
        paramsArray.put(key,values);
    }

    public void putContent(String key, List<Object> values){
        if (general.StringIsEmptyOrNull(key) ||  values == null){
            return;
        }
        ArrayList<Object> items = new ArrayList<>();
        if (paramsArray.get(key) != null){
            items.addAll(paramsArray.get(key));
        }
        for (Object item : values){
            items.add(item.toString());
        }
        paramsArray.put(key,items);
    }

    public void add(String key, String value) {
        if (general.StringIsEmptyOrNull(key) ||  value == null){
            return;
        }
        ArrayList<Object> values;
        if (paramsArray.get(key) != null) {
            values = (ArrayList<Object>) paramsArray.get(key);
            values.add(value);
        } else {
            values = new ArrayList<>();
            values.add(value);
        }
        paramsArray.put(key, values);
    }

    public RequestBody getRequestForm() {
        switch (type){
            case RawTEXTPlain:
                return RequestBody.create(MediaType.parse("text/plain; charset=utf-8"), plainText);
            case RawJSON:
                JSONObject object = new JSONObject();
                try {
                    for (String key : params.keySet()) {
                        object.put(key, String.valueOf(params.get(key)));
                    }
                    for (String key : paramsArray.keySet()) {
                        for (Object item : paramsArray.get(key)) {
                            object.put(key, String.valueOf(item));
                        }
                    }
                } catch (JSONException e) {
                    //throw new JSONException("Exception During Create JSON");
                    logHelper.e(e);
                }
                return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), object.toString());
            case Binary:
                return RequestBody.create(MediaType.parse(contentType),(byte[]) binaryInput);
            default: //FormData
                FormBody.Builder formBody = new FormBody.Builder();
                for (String key : params.keySet()) {
                    formBody.addEncoded(key, String.valueOf(params.get(key)));
                }
                for (String key : paramsArray.keySet()) {
                    for (Object item : paramsArray.get(key)) {
                        formBody.addEncoded(key, String.valueOf(item));
                    }
                }
                return formBody.build();
        }
    }

    public LinkedHashMap<String, Object> getParams() {
        return params;
    }

    public LinkedHashMap<String, File> getFileParams() {
        return fileParams;
    }

    public void setParamsArray(LinkedHashMap<String, List<Object>> paramsArray) {
        if (paramsArray == null){
            return;
        }
        this.paramsArray = paramsArray;
    }
}


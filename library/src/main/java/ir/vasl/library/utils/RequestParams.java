package ir.vasl.library.utils;



import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;


/**
 * Created by alishatergholi on 2/21/18.
 */
public class RequestParams {

    LinkedHashMap<String, Object> params = new LinkedHashMap<>();

    LinkedHashMap<String, List<String>> paramsArray = new LinkedHashMap<>();

    LinkedHashMap<String,File> fileParams = new LinkedHashMap<>();

    private String key;
    private List<String> values;

    public RequestParams() {

    }

    public void put(String key, String value) {
        params.put(key, value);
    }

    public void put(String key, Integer value) {
        params.put(key, value);
    }

    public void put(String key, Float value) {
        params.put(key, value);
    }

    public void put(String key, Double value) {
        params.put(key, value);
    }

    public void put(String key, File value){
        fileParams.put(key,value);
    }

    public void put(String key, boolean value) {
        params.put(key, value);
    }

    public void put(String key, JSONObject value) {
        params.put(key, value);
    }

    public void put(String key, List values) {
        if (paramsArray.get(key) != null){
            values.addAll(paramsArray.get(key));
        }
        paramsArray.put(key,values);
    }

    public void add(String key, List<String> values){
        if (paramsArray.get(key) != null){
            values.addAll(paramsArray.get(key));
        }
        paramsArray.put(key,values);
    }

    public void putContent(String key, List<Object> values){
        ArrayList<String> items = new ArrayList<>();
        if (paramsArray.get(key) != null){
            items.addAll(paramsArray.get(key));
        }
        for (Object item : values){
            items.add(item.toString());
        }
        paramsArray.put(key,items);
    }

    public void add(String key, String value) {
        ArrayList<String> values;
        if (paramsArray.get(key) != null) {
            values = (ArrayList<String>) paramsArray.get(key);
            values.add(value);
        } else {
            values = new ArrayList<>();
            values.add(value);
        }
        paramsArray.put(key, values);
    }

    public RequestBody getRequestForm() {
        FormBody.Builder body = new FormBody.Builder();
        for (String key : params.keySet()) {
            body.addEncoded(key, String.valueOf(params.get(key)));
        }
        for (String key : paramsArray.keySet()) {
            for (Object item : paramsArray.get(key)) {
                body.addEncoded(key, String.valueOf(item));
            }
        }
        return body.build();
    }

    public LinkedHashMap<String, Object> getParams() {
        return params;
    }

    public LinkedHashMap<String, File> getFileParams() {
        return fileParams;
    }

    public void setParamsArray(LinkedHashMap<String, List<String>> paramsArray) {
        this.paramsArray = paramsArray;
    }
}


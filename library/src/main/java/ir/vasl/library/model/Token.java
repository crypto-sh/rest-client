package ir.vasl.library.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Token {

    public Long expires_in;
    public String token_type;
    public String refresh_token;
    public String access_token;
    public String scope;

    public Token(String tokenResult){
        JSONObject result = ParseStringToJsonObject(tokenResult);
        access_token      = ParseJsonObjectToString("access_token",result);
        token_type        = ParseJsonObjectToString("token_type",result);
        refresh_token     = ParseJsonObjectToString("refresh_token",result);
        scope             = ParseJsonObjectToString("scope",result);
        expires_in        = ParseJsonObjectToLong("expires_in",result);
    }

    private String ParseJsonObjectToString(String key, JSONObject object) {
        try {
            if (object.isNull(key))
                return "";
            return object.getString(key);
        } catch (JSONException ex) {
            return "";
        } catch (Exception ex) {
            return "";
        }
    }

    private Long ParseJsonObjectToLong(String key, JSONObject value) {
        try {
            if (value.isNull(key))
                return Long.getLong("0");
            return value.getLong(key);
        } catch (JSONException ex) {
            return Long.getLong("0");
        } catch (Exception ex) {
            return Long.getLong("0");
        }
    }

    private JSONObject ParseStringToJsonObject(String value) {
        try {
            return new JSONObject(value);
        } catch (JSONException ex) {
            return null;
        } catch (Exception ex) {
            return null;
        }
    }
}

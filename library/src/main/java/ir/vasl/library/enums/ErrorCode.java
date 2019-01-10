package ir.vasl.library.enums;


/**
 * Created by alishatergholi on 2/17/18.
 */
public enum ErrorCode {

    AuthorizationException(401,"Authorization Exception"),
    SessionExpire(403,"Session Expire Exception"),
    NotFound(404,"Not Found Exception"),
    InternetConnectionError(407,"Timeout Exception Internet connection Problem"),
    ServerConnectionError(500,"Server Connection Error"),
    NullPointerException(1005,"NUll Pointer Exception"),
    IOException(1006,"IO Exception"),
    RuntimeException(1007,"Runtime Exception error"),
    PermissionDenied(1009,"Permission Need for this Operation"),
    ParseException(1020,"Exception while Parse Data"),
    UnsupportedEncodingException(1020,"UnsupportedEncodingException"),
    Exception(9999,"Exception");

    private final int value;
    private String description;

    private ErrorCode(int value) {
        this.value = value;
    }

    private ErrorCode(int value, String description) {
        this.value       = value;
        this.description = description;
    }

    public int getCode() {
        return this.value;
    }

    public String getDescription() {
        return this.description;
    }

    public static ErrorCode Parse(int value) {
        if(value == 1) {
            return Exception;
        } else {
            ErrorCode[] arr$ = values();
            ErrorCode[] var2 = arr$;
            int var3 = arr$.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                ErrorCode val = var2[var4];
                if(val.value == value) {
                    return val;
                }
            }
            return Exception;
        }
    }
}

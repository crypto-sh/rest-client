package com.github.library.enums


/**
 * Created by alishatergholi on 2/17/18.
 */
enum class ErrorCode {

    AuthorizationException(401          , "Authorization Exception"),
    SessionExpire(403                   , "Session Expire Exception"),
    NotFound(404                        , "Not Found Exception"),
    InternetConnectionError(407         , "Timeout Exception Internet connection Problem"),
    ServerConnectionError(500           , "Server Connection Error"),
    NullPointerException(601            , "NUll Pointer Exception"),
    IOException(602                     , "IO Exception"),
    RuntimeException(603                , "Runtime Exception error"),
    PermissionDenied(604                , "Permission Need for this Operation"),
    ParseDataException(605              , "Exception while parse Data"),
    UnsupportedEncodingException(606    , "UnsupportedEncodingException"),
    Exception(608                       , "Exception");

    val code: Int
    var description: String = ""

    private constructor(value: Int) {
        this.code = value
    }

    private constructor(value: Int, description: String) {
        this.code = value
        this.description = description
    }

    companion object {

        fun parse(value: Int): ErrorCode {
            if (value == 1) {
                return Exception
            } else {
                val `arr$` = values()
                val var3 = `arr$`.size

                for (var4 in 0 until var3) {
                    val `val` = `arr$`[var4]
                    if (`val`.code == value) {
                        return `val`
                    }
                }
                return Exception
            }
        }
    }
}

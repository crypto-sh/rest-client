package com.github.library.enums

enum class AuthType(val code: Int) {

    NO_AUTH(0),
    BASIC_AUTH(1),
    BEARER_AUTH(2),
    OAUTH2_AUTH(3);

    companion object {
        fun parse(value: Int): AuthType {
            if (value == 0) {
                return NO_AUTH
            }
            val `arr$` = values()
            for (`val` in `arr$`) {
                if (`val`.code == value) {
                    return `val`
                }
            }
            return NO_AUTH
        }
    }
}

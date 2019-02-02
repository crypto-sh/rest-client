package com.github.library.enums;

public enum AuthType {

    NO_AUTH(0),
    BASIC_AUTH(1);

    private final int value;

    private AuthType(int value) {
        this.value = value;
    }

    public int getCode() {
        return this.value;
    }

    public static AuthType Parse(int value) {
        if (value == 0) {
            return NO_AUTH;
        }
        AuthType[] arr$ = values();
        for (AuthType val : arr$) {
            if (val.value == value) {
                return val;
            }
        }
        return NO_AUTH;
    }
}

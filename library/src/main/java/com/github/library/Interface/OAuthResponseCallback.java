package com.github.library.Interface;

import com.github.library.model.OAuthResponse;

public interface OAuthResponseCallback {
    void onResponse(OAuthResponse response);
}

package ir.vasl.library.Interface;

import ir.vasl.library.model.OAuthResponse;

public interface OAuthResponseCallback {
    void onResponse(OAuthResponse response);
}

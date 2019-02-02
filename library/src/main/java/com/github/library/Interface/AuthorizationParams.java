package com.github.library.Interface;

public interface AuthorizationParams {
    String GRANT_TYPE_REFRESH   = "refresh_token";
    String GRANT_TYPE_PASSWORD  = "password";
    String POST_GRANT_TYPE      = "grant_type";
    String POST_USERNAME        = "username";
    String POST_PASSWORD        = "password";
    String POST_CLIENT_ID       = "client_id";
    String POST_CLIENT_SECRET   = "client_secret";
    String POST_SCOPE           = "scope";
    String POST_REFRESH_TOKEN   = "refresh_token";
    String HEADER_CONTENT_TYPE  = "Content-Type";
    String HEADER_AUTHORIZATION = "Authorization";
}

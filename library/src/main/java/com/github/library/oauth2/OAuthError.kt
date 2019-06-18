package com.github.library.oauth2

open class OAuthError(e: Exception) {

    var error: String? = null
        protected set

    var errorDescription: String? = null
        protected set

    var errorUri: String? = null
        protected set

    @Transient
    var errorException: Exception
        protected set

    init {
        errorException = e
    }
}

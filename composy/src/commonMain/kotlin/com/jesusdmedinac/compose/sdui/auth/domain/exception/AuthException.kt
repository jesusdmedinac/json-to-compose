package com.jesusdmedinac.compose.sdui.auth.domain.exception

class AuthException(override val message: String? = null, override val cause: Throwable? = null) :
    Exception(message, cause) {
    constructor(cause: Throwable) : this(message = cause.message, cause = cause)
}
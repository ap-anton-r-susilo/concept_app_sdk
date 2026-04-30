package com.example.authsdk

interface AuthCallback {
    fun onAuthenticated()
    fun onAuthenticationFailed(reason: String)
    fun onAuthenticationCancelled()
}

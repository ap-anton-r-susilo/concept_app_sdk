package com.example.camerasdk

interface AuthCallback {
    fun onAuthenticated()
    fun onAuthenticationFailed(reason: String)
    fun onAuthenticationCancelled()
}

package com.example.sdkconsumer.data.auth

import android.app.Activity
import com.example.authsdk.SimpleAuthProvider
import com.example.camerasdk.AuthCallback
import com.example.camerasdk.AuthProvider

class AuthProviderAdapter(
    private val delegate: SimpleAuthProvider
) : AuthProvider {

    override fun authenticate(activity: Activity, callback: AuthCallback) {
        delegate.authenticate(
            activity,
            object : com.example.authsdk.AuthCallback {
                override fun onAuthenticated() = callback.onAuthenticated()
                override fun onAuthenticationFailed(reason: String) = callback.onAuthenticationFailed(reason)
                override fun onAuthenticationCancelled() = callback.onAuthenticationCancelled()
            }
        )
    }

    override fun isAuthenticated(): Boolean = delegate.isAuthenticated()
}

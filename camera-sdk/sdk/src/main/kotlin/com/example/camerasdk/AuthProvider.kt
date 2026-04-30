package com.example.camerasdk

import android.app.Activity

interface AuthProvider {
    fun authenticate(activity: Activity, callback: AuthCallback)
    fun isAuthenticated(): Boolean
}

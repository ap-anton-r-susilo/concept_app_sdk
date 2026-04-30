package com.example.sdkconsumer

import android.app.Application
import com.example.authsdk.SimpleAuthProvider
import com.example.camerasdk.MySdk
import com.example.sdkconsumer.data.analytics.AmplitudeAnalyticsProvider
import com.example.sdkconsumer.data.auth.AuthProviderAdapter
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        val authProvider = AuthProviderAdapter(SimpleAuthProvider())
        val analyticsProvider = AmplitudeAnalyticsProvider(
            apiKey = "884aadbe892627fda0c8eeb64a3f58b5",
            context = this
        )
        MySdk.initialize(authProvider, analyticsProvider)
    }
}

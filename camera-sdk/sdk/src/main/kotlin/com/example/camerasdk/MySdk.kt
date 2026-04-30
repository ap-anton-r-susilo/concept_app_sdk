package com.example.camerasdk

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.annotation.VisibleForTesting
import com.example.camerasdk.ui.SdkCameraActivity

object MySdk {
    private var authProvider: AuthProvider? = null
    private var analyticsProvider: AnalyticsProvider? = null

    fun initialize(authProvider: AuthProvider, analyticsProvider: AnalyticsProvider) {
        this.authProvider = authProvider
        this.analyticsProvider = analyticsProvider
    }

    fun getAuthProvider(): AuthProvider {
        return checkNotNull(authProvider) {
            "MySdk not initialized. Call MySdk.initialize(authProvider, analyticsProvider) first."
        }
    }

    fun getAnalyticsProvider(): AnalyticsProvider {
        return checkNotNull(analyticsProvider) {
            "MySdk not initialized. Call MySdk.initialize(authProvider, analyticsProvider) first."
        }
    }

    fun launch(context: Context) {
        getAuthProvider()
        getAnalyticsProvider()
        val intent = Intent(context, SdkCameraActivity::class.java)
        if (context !is Activity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    @VisibleForTesting
    internal fun reset() {
        authProvider = null
        analyticsProvider = null
    }
}

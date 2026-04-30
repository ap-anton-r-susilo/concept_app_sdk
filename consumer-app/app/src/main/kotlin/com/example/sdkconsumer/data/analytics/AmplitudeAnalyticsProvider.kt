package com.example.sdkconsumer.data.analytics

import android.content.Context
import android.util.Log
import com.amplitude.android.Amplitude
import com.amplitude.android.Configuration
import com.amplitude.core.events.Identify
import com.example.camerasdk.AnalyticsProvider

class AmplitudeAnalyticsProvider(
    apiKey: String,
    context: Context
) : AnalyticsProvider {

    private val amplitude: Amplitude = Amplitude(
        Configuration(
            apiKey = apiKey,
            context = context.applicationContext
        )
    )

    override fun sendEvent(name: String, properties: Map<String, Any?>) {
        Log.i("AmplitudeAnalytics", "Sending event: $name, properties: $properties")
        if (properties.isEmpty()) {
            amplitude.track(name)
        } else {
            amplitude.track(name, properties)
        }
    }

    override fun sendUserAttribute(key: String, value: Any) {
        Log.i("AmplitudeAnalytics", "Sending user attribute: $key = $value")
        val identify = Identify()
        identify.set(key, value)
        amplitude.identify(identify)
    }
}

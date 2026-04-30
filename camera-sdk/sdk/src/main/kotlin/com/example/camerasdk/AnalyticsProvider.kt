package com.example.camerasdk

interface AnalyticsProvider {
    fun sendEvent(name: String, properties: Map<String, Any?> = emptyMap())
    fun sendUserAttribute(key: String, value: Any)
}

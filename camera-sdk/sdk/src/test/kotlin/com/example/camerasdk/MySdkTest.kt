package com.example.camerasdk

import org.junit.After
import org.junit.Assert.assertSame
import org.junit.Assert.fail
import org.junit.Test

class MySdkTest {

    private val fakeAnalytics = object : AnalyticsProvider {
        override fun sendEvent(name: String, properties: Map<String, Any?>) = Unit
        override fun sendUserAttribute(key: String, value: Any) = Unit
    }

    @After
    fun tearDown() {
        MySdk.reset()
    }

    @Test
    fun `getAuthProvider throws when not initialized`() {
        MySdk.reset()

        try {
            MySdk.getAuthProvider()
            fail("Expected IllegalStateException when SDK is not initialized")
        } catch (_: IllegalStateException) {
            // Expected.
        }
    }

    @Test
    fun `getAnalyticsProvider throws when not initialized`() {
        MySdk.reset()

        try {
            MySdk.getAnalyticsProvider()
            fail("Expected IllegalStateException when SDK is not initialized")
        } catch (_: IllegalStateException) {
            // Expected.
        }
    }

    @Test
    fun `getAuthProvider returns provider after init`() {
        val provider = FakeAuthProvider("provider-a")

        MySdk.initialize(provider, fakeAnalytics)

        assertSame(provider, MySdk.getAuthProvider())
    }

    @Test
    fun `getAnalyticsProvider returns provider after init`() {
        val auth = FakeAuthProvider("auth")

        MySdk.initialize(auth, fakeAnalytics)

        assertSame(fakeAnalytics, MySdk.getAnalyticsProvider())
    }

    @Test
    fun `initialize replaces previous providers`() {
        val providerA = FakeAuthProvider("provider-a")
        val providerB = FakeAuthProvider("provider-b")
        val analyticsB = object : AnalyticsProvider {
            override fun sendEvent(name: String, properties: Map<String, Any?>) = Unit
            override fun sendUserAttribute(key: String, value: Any) = Unit
        }

        MySdk.initialize(providerA, fakeAnalytics)
        MySdk.initialize(providerB, analyticsB)

        assertSame(providerB, MySdk.getAuthProvider())
        assertSame(analyticsB, MySdk.getAnalyticsProvider())
    }

    private class FakeAuthProvider(
        private val id: String
    ) : AuthProvider {
        override fun authenticate(activity: android.app.Activity, callback: AuthCallback) = Unit

        override fun isAuthenticated(): Boolean = false

        override fun toString(): String = id
    }
}

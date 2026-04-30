package com.example.authsdk

import org.junit.Assert.assertFalse
import org.junit.Test

class SimpleAuthProviderTest {

    @Test
    fun `isAuthenticated returns false by default`() {
        val provider = SimpleAuthProvider()

        assertFalse(provider.isAuthenticated())
    }
}

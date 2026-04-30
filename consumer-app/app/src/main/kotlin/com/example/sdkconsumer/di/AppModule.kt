package com.example.sdkconsumer.di

import android.content.Context
import com.example.authsdk.SimpleAuthProvider
import com.example.camerasdk.AnalyticsProvider
import com.example.camerasdk.AuthProvider
import com.example.sdkconsumer.data.analytics.AmplitudeAnalyticsProvider
import com.example.sdkconsumer.data.auth.AuthProviderAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideAuthProvider(): AuthProvider = AuthProviderAdapter(SimpleAuthProvider())

    @Provides
    @Singleton
    fun provideAnalyticsProvider(
        @ApplicationContext context: Context
    ): AnalyticsProvider = AmplitudeAnalyticsProvider(
        apiKey = "YOUR_AMPLITUDE_API_KEY",
        context = context
    )
}

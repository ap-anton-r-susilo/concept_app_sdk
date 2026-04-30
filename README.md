# Android SDK PoC Workspace

Proof of concept demonstrating **auth-agnostic** and **analytics-agnostic** SDK architecture on Android. Each SDK defines its own interfaces — the consuming app provides implementations and wires everything together.

## Projects

### auth-sdk/

Standalone auth library. Defines `AuthProvider`/`AuthCallback` interfaces and ships a default `SimpleAuthProvider` (PIN "1234" dialog). Framework-agnostic — no Hilt, no Compose.

### camera-sdk/

QRIS payment camera-capture SDK. Fully independent — defines its **own** `AuthProvider`/`AuthCallback` and `AnalyticsProvider` interfaces. Provides a 4-step flow:

1. **Camera capture** → fires `scan_clicked`
2. **Transaction confirmation** → fires `confirm_clicked`
3. **Authentication** (delegated to provided `AuthProvider`)
4. **Result screen** → fires `payment_success` or `payment_failed`

No auth or analytics implementation — the consuming app provides both via `MySdk.initialize(authProvider, analyticsProvider)`.

### consumer-app/

Sample app that wires everything together:
- `AuthProviderAdapter` — bridges auth-sdk's `SimpleAuthProvider` to camera-sdk's `AuthProvider`
- `AmplitudeAnalyticsProvider` — implements camera-sdk's `AnalyticsProvider` using `com.amplitude:analytics-android`
- Hilt for DI, Gradle composite builds for local SDK resolution

## Dependency Graph

```
consumer-app
├── auth-sdk                        → SimpleAuthProvider
├── camera-sdk                      → MySdk + AuthProvider/AuthCallback/AnalyticsProvider interfaces
└── com.amplitude:analytics-android → wrapped by AmplitudeAnalyticsProvider

auth-sdk ──── (independent)
camera-sdk ── (independent)
```

Both SDKs have **zero dependencies** on each other. The consumer app is the only place where they meet.

## Build

Each project is self-contained with its own Gradle wrapper. JDK 17 required.

```bash
export JAVA_HOME=/path/to/jdk17

# auth-sdk
cd auth-sdk && ./gradlew testDebugUnitTest

# camera-sdk
cd camera-sdk && ./gradlew testDebugUnitTest

# consumer-app
cd consumer-app && ./gradlew assembleDebug
```

## Version Matrix

| Component | Version |
|-----------|---------|
| AGP | 8.13.2 |
| Kotlin | 2.3.0 |
| Gradle | 8.13 |
| Compose BOM | 2025.01.01 |
| Hilt | 2.58 |
| compileSdk / targetSdk | 35 |
| minSdk | 24 |

# AGENTS.md

## What This Is

Multi-project workspace for Android Proof of Concept apps. Each PoC lives in its own subdirectory. No shared Gradle wrapper or root build — every project is fully self-contained.

## Project Lifecycle

### Creating a new PoC
- Create a new subdirectory at the workspace root (e.g., `my-poc/`)
- Scaffold the full Android project inside it — do NOT reuse another project's Gradle wrapper or settings
- Always load the `android-kotlin` skill before scaffolding; it defines the required architecture and Gradle config
- Each project must include its own `gradlew`, `settings.gradle.kts`, root `build.gradle.kts`, and `app/` module

### Improving an existing PoC
- Work entirely within that project's subdirectory
- Run all Gradle commands from the project root (e.g., `./gradlew build` from `my-poc/`)
- Load the `android-kotlin` skill to stay consistent with project conventions

## Architecture (from android-kotlin skill)

All projects follow Clean Architecture with three layers:
- **data/** — Room DAOs, Retrofit/Ktor services, repository implementations
- **domain/** — Models, repository interfaces, use cases
- **ui/** — Compose screens, ViewModels, reusable components, theme

DI: Hilt. UI: Jetpack Compose + Material 3. Async: Coroutines + Flow. Testing: MockK + Turbine.

## Key Commands (per-project)

```bash
./gradlew assembleDebug          # Build debug APK
./gradlew testDebugUnitTest      # Unit tests
./gradlew detekt                 # Static analysis
./gradlew ktlintCheck            # Code style
```

Verification order: `detekt` → `ktlintCheck` → `testDebugUnitTest` → `assembleDebug`

## Gotchas

- JDK 17 required (`compileOptions` target 17, `kotlin { compilerOptions { jvmTarget } }`)
- KSP used for annotation processing (Hilt, Room) — not kapt
- Compose compiler is built into Kotlin 2.3+ — use `org.jetbrains.kotlin.plugin.compose` plugin, NOT the old `composeOptions { kotlinCompilerExtensionVersion }` block
- Compose BOM manages Compose dependency versions — do not pin individual Compose artifacts
- `minSdk = 24`, `targetSdk = 35`, `compileSdk = 35`
- No shared workspace-level Gradle — each PoC is independent
- JAVA_HOME must be set to JDK 17: `export JAVA_HOME=/Users/anton/Library/Java/JavaVirtualMachines/corretto-17.0.16/Contents/Home`
- Default JAVA_HOME points to JDK 11 — Gradle builds will fail without the override
- Kotlin 2.3+ removed `kotlinOptions` DSL — use `kotlin { compilerOptions { ... } }` instead
- Hilt 2.59+ requires AGP 9.0+ — use Hilt 2.58 with AGP 8.x

## Version Matrix

| Component | Version |
|-----------|---------|
| AGP | 8.13.2 |
| Kotlin | 2.3.0 |
| KSP | 2.3.0 |
| Gradle | 8.13 |
| Compose BOM | 2025.01.01 |
| Compose Compiler | Built into Kotlin plugin |
| Hilt | 2.58 |
| compileSdk | 35 |
| targetSdk | 35 |
| minSdk | 24 |

## Existing Projects

### auth-sdk/
Standalone auth interfaces and a default PIN-based implementation. Framework-agnostic (no Hilt/Compose). Fully independent — no dependencies on other projects in this workspace.
- Module: `:sdk` (Android library, package `com.example.authsdk`)
- Gradle coordinates: `com.example.authsdk:sdk:1.0.0`
- Key classes: `AuthProvider` (interface), `AuthCallback` (interface), `SimpleAuthProvider` (PIN "1234" dialog)

### camera-sdk/
QRIS payment camera-capture SDK. Fully independent — defines its own `AuthProvider`/`AuthCallback` and `AnalyticsProvider` interfaces so it stays agnostic of both auth and analytics implementations. Provides a 4-step flow: camera capture → transaction confirmation → auth (delegated) → result screen. Fires analytics events at each step (`scan_clicked`, `confirm_clicked`, `payment_success`, `payment_failed`). No auth or analytics implementation — the consuming app provides both via `MySdk.initialize(authProvider, analyticsProvider)`.
- Module: `:sdk` (Android library, package `com.example.camerasdk`)
- Gradle coordinates: `com.example.camerasdk:sdk:1.0.0`
- No dependencies on other workspace projects
- Key classes: `MySdk` (entry point), `AuthProvider` (interface), `AuthCallback` (interface), `AnalyticsProvider` (interface with `sendEvent`/`sendUserAttribute`)

### consumer-app/
Sample app consuming `auth-sdk` and `camera-sdk` via Gradle composite builds. Bridges auth-sdk's `SimpleAuthProvider` to camera-sdk's `AuthProvider` via `AuthProviderAdapter`. Creates `AmplitudeAnalyticsProvider` (wrapping `com.amplitude:analytics-android:1.27.0`). Injects both into camera-sdk on startup. Uses Hilt for DI.
- Module: `:app` (Android application, package `com.example.sdkconsumer`)
- Depends on: `implementation("com.example.authsdk:sdk:1.0.0")` + `implementation("com.example.camerasdk:sdk:1.0.0")` + `implementation("com.amplitude:analytics-android:1.27.0")` with dependency substitution for auth-sdk and camera-sdk
- Key classes: `AuthProviderAdapter` (bridges auth-sdk → camera-sdk auth interface), `AmplitudeAnalyticsProvider` (implements camera-sdk analytics interface)

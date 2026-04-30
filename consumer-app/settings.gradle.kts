pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "sdk-consumer"
include(":app")

includeBuild("../auth-sdk") {
    dependencySubstitution {
        substitute(module("com.example.authsdk:sdk")).using(project(":sdk"))
    }
}

includeBuild("../camera-sdk") {
    dependencySubstitution {
        substitute(module("com.example.camerasdk:sdk")).using(project(":sdk"))
    }
}

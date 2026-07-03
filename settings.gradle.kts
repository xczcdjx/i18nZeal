import java.util.Properties

val localPublishSecretsFile = file("publish-secrets.properties")
if (localPublishSecretsFile.isFile) {
    val localPublishSecrets = Properties()
    localPublishSecretsFile.inputStream().use(localPublishSecrets::load)
    gradle.beforeProject {
        localPublishSecrets.forEach { key, value ->
            val propertyName = key.toString().trim()
            if (propertyName.isNotEmpty() && !extensions.extraProperties.has(propertyName)) {
                extensions.extraProperties.set(propertyName, value.toString())
            }
        }
    }
}

rootProject.name = "I18nZeal"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("i18n-gradle-plugin")

    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":androidApp")
include(":desktopApp")
include(":shared")
include(":webApp")
include(":i18n-runtime")

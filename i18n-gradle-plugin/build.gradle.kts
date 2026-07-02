plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
}

group = providers.gradleProperty("GROUP")
    .orElse("com.djx.i18nzeal")
    .get()
version = providers.gradleProperty("VERSION_NAME")
    .orElse("0.1.0-SNAPSHOT")
    .get()

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(localGroovy())
    implementation("org.yaml:snakeyaml:2.2")
}

gradlePlugin {
    plugins {
        create("i18nZeal") {
            id = "com.djx.i18nzeal"
            implementationClass = "com.djx.i18nzeal.I18nZealPlugin"
            displayName = "i18nZeal Gradle Plugin"
            description = "Generates Kotlin i18n constants and engine maps from locale files."
        }
    }
}

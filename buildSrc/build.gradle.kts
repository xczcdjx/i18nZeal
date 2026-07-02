plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(localGroovy())
    implementation("org.yaml:snakeyaml:2.2")
}
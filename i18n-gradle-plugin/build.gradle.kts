import java.util.Properties

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    id("com.vanniktech.maven.publish.base") version "0.37.0"
}

val rootGradleProperties = Properties().apply {
    val file = rootProject.file("../gradle.properties")
    if (file.isFile) {
        file.inputStream().use(::load)
    }
}

fun publishingProperty(
    primaryName: String,
    fallbackName: String
): String {
    return providers.gradleProperty(primaryName).orNull
        ?: providers.gradleProperty(fallbackName).orNull
        ?: rootGradleProperties.getProperty(primaryName)
        ?: rootGradleProperties.getProperty(fallbackName)
}

group = publishingProperty(
    primaryName = "PLUGIN_GROUP",
    fallbackName = "GROUP",
)
version = publishingProperty(
    primaryName = "PLUGIN_VERSION_NAME",
    fallbackName = "VERSION_NAME",
)

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
            id = "io.github.xczcdjx.i18nzeal"
            implementationClass = "com.djx.i18nzeal.I18nZealPlugin"
            displayName = "i18nZeal Gradle Plugin"
            description = "Generates Kotlin i18n constants and engine maps from locale files."
        }
    }
}

mavenPublishing {
    configure(
        com.vanniktech.maven.publish.GradlePlugin(
            javadocJar = com.vanniktech.maven.publish.JavadocJar.Empty(),
            sourcesJar = com.vanniktech.maven.publish.SourcesJar.Sources(),
        )
    )
    publishToMavenCentral()
    if (!gradle.startParameter.taskNames.any { it.contains("publishToMavenLocal", ignoreCase = true) }) {
        signAllPublications()
    }

    pom {
        name.set("i18n-gradle-plugin")
        description.set("Gradle plugin for i18nZeal Kotlin Multiplatform i18n.")
        inceptionYear.set("2026")
        url.set("https://github.com/xczcdjx/i18nZeal")

        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }

        developers {
            developer {
                id.set("xczcdjx")
                name.set("Lzq")
                email.set("xczcdjx@126.com")
            }
        }

        scm {
            url.set("https://github.com/xczcdjx/i18nZeal")
            connection.set("scm:git:git://github.com/xczcdjx/i18nZeal.git")
            developerConnection.set("scm:git:ssh://git@github.com:xczcdjx/i18nZeal.git")
        }
    }
}

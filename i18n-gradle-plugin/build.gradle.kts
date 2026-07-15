plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    id("com.vanniktech.maven.publish.base") version "0.37.0"
}

group = providers.gradleProperty("GROUP")
    .orElse("io.github.xczcdjx")
    .get()
version = providers.gradleProperty("VERSION_NAME")
    .orElse("0.1.1")
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
            id = "io.github.xczcdjx.i18nzeal"
            implementationClass = "com.djx.i18nzeal.I18nZealPlugin"
            displayName = "i18nZeal Gradle Plugin"
            description = "Generates typed keys and translation engines from JSON, YAML, Properties, or Kotlin locale files."
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
    signAllPublications()

    pom {
        name.set("i18n-gradle-plugin")
        description.set("Gradle plugin for Kotlin Multiplatform i18n that generates typed keys and translation engines from JSON, YAML, Properties, or Kotlin locale files.")
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

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.androidLint)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.vanniktech.maven.publish)
}

group = providers.gradleProperty("RUNTIME_GROUP").get()
version = providers.gradleProperty("RUNTIME_VERSION_NAME").get()

kotlin {

    // Target declarations - add or remove as needed below. These define
    // which platforms this KMP module supports.
    // See: https://kotlinlang.org/docs/multiplatform-discover-project.html#targets
    android {
        namespace = "com.djx.i18n.runtime"
        compileSdk {
            version = release(36) {
                minorApiLevel = 1
            }
        }
        minSdk = 24

        withHostTestBuilder {
        }

        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    // For iOS targets, this is also where you should
    // configure native binary output. For more information, see:
    // https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#build-xcframeworks

    // A step-by-step guide on how to include this library in an XCode
    // project can be found here:
    // https://developer.android.com/kotlin/multiplatform/migrate
    val xcfName = "i18n-runtimeKit"

    /*iosX64 {
        binaries.framework {
            baseName = xcfName
        }
    }*/

    iosArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    // ⭐ 加这个
    jvm()
    js(IR) {
        browser()
        nodejs()
    }

    wasmJs {
        browser()
    }

    // Source set declarations.
    // Declaring a target automatically creates a source set with the same name. By default, the
    // Kotlin Gradle Plugin creates additional source sets that depend on each other, since it is
    // common to share sources between related targets.
    // See: https://kotlinlang.org/docs/multiplatform-hierarchy.html
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.compose.runtime)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        androidMain {
            dependencies {
                // Add Android-specific dependencies here. Note that this source set depends on
                // commonMain by default and will correctly pull the Android artifacts of any KMP
                // dependencies declared in commonMain.
            }
        }

        getByName("androidDeviceTest") {
            dependencies {
                implementation(libs.androidx.core)
                implementation(libs.androidx.runner)
                implementation(libs.androidx.testExt.junit)
            }
        }

        iosMain {
            dependencies {
                // Add iOS-specific dependencies here. This a source set created by Kotlin Gradle
                // Plugin (KGP) that each specific iOS target (e.g., iosX64) depends on as
                // part of KMP’s default source set hierarchy. Note that this source set depends
                // on common by default and will correctly pull the iOS artifacts of any
                // KMP dependencies declared in commonMain.
            }
        }
    }

}


mavenPublishing {
    publishToMavenCentral()
    if (!gradle.startParameter.taskNames.any { it.contains("publishToMavenLocal", ignoreCase = true) }) {
        signAllPublications()
    }

    pom {
        name.set("i18n-runtime")
        description.set("Runtime library for i18nZeal Kotlin Multiplatform i18n.")
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
            developerConnection.set("scm:git:ssh://git@github.com/xczcdjx/i18nZeal.git")
        }
    }
}

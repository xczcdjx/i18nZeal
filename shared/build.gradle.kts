import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Locale

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    jvm()

    js {
        browser()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    androidLibrary {
        namespace = "com.djx.i18nzeal.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
        androidResources {
            enable = true
        }
        withHostTest {
            isIncludeAndroidResources = true
        }
    }

    sourceSets {
        commonMain {
            kotlin.srcDir(
                layout.buildDirectory.dir("generated/i18nzeal/commonMain/kotlin")
            )
        }
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jsMain.dependencies {
            implementation(libs.wrappers.browser)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}


/*tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask<*>>()
    .configureEach {
        dependsOn(generateI18nKeys)
    }*/
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask<*>>()
    .configureEach {
        dependsOn(generateI18nKt)
    }
/*val generateI18nKeys by tasks.registering(GenerateI18nKeysTask::class) {
    inputFile.set(
        layout.projectDirectory.file(
            "src/commonMain/kotlin/com/djx/i18nzeal/i18n/zh.kt"
        )
    )

    outputDir.set(
        layout.buildDirectory.dir("generated/i18nzeal/commonMain/kotlin")
    )

    packageName.set("com.djx.i18nzeal.i18n")
    className.set("I18nKeys")
}*/

val generateI18nKt by tasks.registering(JsonToI18nKtTask::class) {
    sourceLocales.set(listOf("en", "zh"))
    inputDir.set(
        layout.projectDirectory.dir("src/commonMain/i18n")
    )

    outputDir.set(
        layout.buildDirectory.dir("generated/i18nzeal/commonMain/kotlin")
    )

    packageName.set("com.djx.i18nzeal.i18n")
    objectName.set("I18nZeal")
    fileType.set("json")
}

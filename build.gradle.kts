import java.util.Properties
import org.gradle.api.tasks.Exec

plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidMultiplatformLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.androidLint) apply false
}

fun loadLocalPublishPropertyArgs(): List<String> {
    val secretsFile = rootProject.file("publish-secrets.properties")
    if (!secretsFile.isFile) return emptyList()

    val properties = Properties()
    secretsFile.inputStream().use(properties::load)
    val excludedPropertyNames = setOf(
        "mavenCentralPublishing",
        "signAllPublications",
        "mavenCentralAutomaticPublishing",
    )

    return properties.entries
        .mapNotNull { (key, value) ->
            val propertyName = key.toString().trim()
            val propertyValue = value.toString().trim()
            if (
                propertyName.isEmpty() ||
                propertyValue.isEmpty() ||
                propertyName in excludedPropertyNames
            ) {
                null
            } else {
                "-P$propertyName=$propertyValue"
            }
        }
}

val publishRuntimeToMavenCentral by tasks.registering(Exec::class) {
    group = "publishing"
    description = "Publishes i18n-runtime to Maven Central."
    workingDir = rootProject.projectDir
    notCompatibleWithConfigurationCache("Delegates to a nested Gradle invocation with dynamic -P publishing properties.")
    doFirst {
        commandLine(
            rootProject.file("gradlew").absolutePath,
            ":i18n-runtime:publishToMavenCentral",
            "--no-configuration-cache",
            *loadLocalPublishPropertyArgs().toTypedArray(),
        )
    }
}

val publishPluginToMavenCentral by tasks.registering(Exec::class) {
    group = "publishing"
    description = "Publishes i18n-gradle-plugin to Maven Central."
    workingDir = rootProject.file("i18n-gradle-plugin")
    notCompatibleWithConfigurationCache("Delegates to a nested Gradle invocation with dynamic -P publishing properties.")
    doFirst {
        commandLine(
            rootProject.file("gradlew").absolutePath,
            "publishToMavenCentral",
            "--no-configuration-cache",
            *loadLocalPublishPropertyArgs().toTypedArray(),
        )
    }
}

val publishRuntimeAndReleaseToMavenCentral by tasks.registering(Exec::class) {
    group = "publishing"
    description = "Publishes and releases i18n-runtime to Maven Central."
    workingDir = rootProject.projectDir
    notCompatibleWithConfigurationCache("Delegates to a nested Gradle invocation with dynamic -P publishing properties.")
    doFirst {
        commandLine(
            rootProject.file("gradlew").absolutePath,
            ":i18n-runtime:publishAndReleaseToMavenCentral",
            "--no-configuration-cache",
            *loadLocalPublishPropertyArgs().toTypedArray(),
        )
    }
}

val publishPluginAndReleaseToMavenCentral by tasks.registering(Exec::class) {
    group = "publishing"
    description = "Publishes and releases i18n-gradle-plugin to Maven Central."
    workingDir = rootProject.file("i18n-gradle-plugin")
    notCompatibleWithConfigurationCache("Delegates to a nested Gradle invocation with dynamic -P publishing properties.")
    doFirst {
        commandLine(
            rootProject.file("gradlew").absolutePath,
            "publishAndReleaseToMavenCentral",
            "--no-configuration-cache",
            *loadLocalPublishPropertyArgs().toTypedArray(),
        )
    }
}

publishPluginToMavenCentral.configure {
    mustRunAfter(publishRuntimeToMavenCentral)
}

publishPluginAndReleaseToMavenCentral.configure {
    mustRunAfter(publishRuntimeAndReleaseToMavenCentral)
}

tasks.register("publishAllToMavenCentral") {
    group = "publishing"
    description = "Publishes i18n-runtime and i18n-gradle-plugin to Maven Central."
    dependsOn(publishRuntimeToMavenCentral)
    dependsOn(publishPluginToMavenCentral)
}

tasks.register("publishAllAndReleaseToMavenCentral") {
    group = "publishing"
    description = "Publishes and releases i18n-runtime and i18n-gradle-plugin to Maven Central."
    dependsOn(publishRuntimeAndReleaseToMavenCentral)
    dependsOn(publishPluginAndReleaseToMavenCentral)
}

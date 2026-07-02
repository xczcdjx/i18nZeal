package com.djx.i18nzeal

import org.gradle.api.Plugin
import org.gradle.api.Project
class I18nZealPlugin : Plugin<Project> {

    override fun apply(project: Project) {

        val extension = project.extensions.create(
            "i18nZeal",
            I18nZealExtension::class.java,
            project
        )

        val task = project.tasks.register(
            "generateI18nKt",
            JsonToI18nKtTask::class.java
        )

        task.configure {
            require(extension.sourceLocales.isNotEmpty()) {
                "sourceLocales cannot be empty"
            }
            require(extension.packageName!=null) {
                "packageName cannot be empty"
            }
            fileType.set(extension.fileType.name.lowercase())

            inputDir.set(project.layout.projectDirectory.dir(extension.inputDir))
            outputDir.set(project.layout.buildDirectory.dir(extension.outputDir))
            packageName.set(extension.packageName)
            objectName.set(extension.objectName)
            sourceLocales.set(extension.sourceLocales)
        }
    }
}
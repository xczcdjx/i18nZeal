package com.djx.i18nzeal

import groovy.json.JsonSlurper
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.util.Locale

abstract class JsonToI18nKtTask : DefaultTask() {

    @get:Input
    abstract val sourceLocales: ListProperty<String>

    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val inputDir: DirectoryProperty

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @get:Input
    abstract val packageName: Property<String>

    @get:Input
    abstract val objectName: Property<String>

    @get:Input
    abstract val fileType: Property<String>

    @TaskAction
    fun run() {
        val input = inputDir.get().asFile
        val output = outputDir.get().asFile

        output.deleteRecursively()
        output.mkdirs()

        val type = fileType.get().lowercase()?: "json"
        // validate
        val supported = setOf("json", "yaml", "yml", "kt", "properties")

        require(type in supported) {
            """
Unsupported fileType: $type

Supported types:
${supported.joinToString(", ")}
""".trimIndent()
        }

        val files = input
            .listFiles()
            ?.asSequence()
            ?.filter { it.isFile }
            ?.filter { it.extension.equals(type, ignoreCase = true) }
            ?.sortedBy { it.nameWithoutExtension }
            ?.toList()
            .orEmpty()

        if (files.isEmpty()) {
            logger.warn("No json files found in: ${input.absolutePath}")
            return
        }

        val translations = files.associate { file ->
            val locale = file.nameWithoutExtension
            val raw = parseFile(file)
            locale to flattenJson(raw)
        }

        val baseLocale = sourceLocales.get()
            .firstOrNull { translations.containsKey(it) }
            ?: translations.keys.first()

        val keys = translations[baseLocale]
            ?.keys
            ?.distinct()
            ?.sorted()
            ?: emptyList()

        val pkg = packageName.get()
        val obj = objectName.get()

        val content = buildKtFile(
            packageName = pkg,
            objectName = obj,
            keys = keys,
            translations = translations
        )

        val outputFile = output.resolve("${pkg.replace('.', '/')}/$obj.kt")

        outputFile.parentFile.mkdirs()
        outputFile.writeText(content, Charsets.UTF_8)
    }

    private fun parseFile(file: File): Map<String, Any> {
        return when (file.extension.lowercase()) {

            "json" -> parseJson(file)

            "yml", "yaml" -> parseYaml(file)

            "properties" -> parseProperties(file)

            "kt" -> parseKt(file)

            else -> error("Unsupported file type: ${file.extension}")
        }
    }

    private fun parseJson(file: File): Map<String, Any> {
        return JsonSlurper().parse(file) as Map<String, Any>
    }

    private fun parseYaml(file: File): Map<String, Any> {
        val yaml = Yaml()
        return yaml.load(file.inputStream()) as Map<String, Any>
    }

    private fun parseProperties(file: File): Map<String, Any> {
        val props = java.util.Properties()
        file.inputStream().use { props.load(it) }

        return props.entries.associate {
            it.key.toString() to it.value.toString()
        }
    }

    private fun parseKt(file: File): Map<String, Any> {
        val text = file.readText()

        val mapBlockRegex =
            Regex("map\\s*=\\s*mapOf\\s*\\((.*?)\\)", RegexOption.DOT_MATCHES_ALL)

        val block = mapBlockRegex.find(text)?.groupValues?.get(1)
            ?: return emptyMap()

        val entryRegex =
            Regex("\"([^\"]+)\"\\s*to\\s*\"([^\"]*)\"")

        return entryRegex.findAll(block)
            .associate { it.groupValues[1] to it.groupValues[2] }
    }

    private fun flattenJson(
        map: Map<*, *>,
        prefix: String = ""
    ): Map<String, String> {
        val result = linkedMapOf<String, String>()

        map.forEach { (rawKey, value) ->
            val key = rawKey.toString()
            val fullKey = if (prefix.isBlank()) key else "$prefix.$key"

            when (value) {
                is Map<*, *> -> result += flattenJson(value, fullKey)
                null -> result[fullKey] = ""
                else -> result[fullKey] = value.toString()
            }
        }

        return result
    }

    private fun buildKtFile(
        packageName: String,
        objectName: String,
        keys: List<String>,
        translations: Map<String, Map<String, String>>
    ): String {
        return buildString {
            appendLine("package $packageName")
            appendLine()
            appendLine("import com.djx.i18n.runtime.export.Locale")
            appendLine("import com.djx.i18n.runtime.interfaces.I18nEngine")
            appendLine()
            appendLocaleValues(locales = translations.keys.toList())
            appendLine()
            appendLine("/**")
            appendLine(" * Auto generated by i18nZeal.")
            appendLine(" * Do not edit manually.")
            appendLine(" */")
            appendLine("object I18nKeys {")

            keys.forEach { key ->
                appendLine("    const val ${key.toFieldName()} = \"${escapeKotlinString(key)}\"")
            }

            appendLine("}")
            appendLine()
            appendLine("/**")
            appendLine(" * Auto generated by i18nZeal.")
            appendLine(" * Do not edit manually.")
            appendLine(" */")
            appendLine("object $objectName: I18nEngine {")
            appendLine()
            appendLine("    private val langMap = mapOf(")

            translations.keys.forEach { locale ->
                val langName = locale.toLocaleValueName()
                val providerName = providerObjectName(objectName, locale)

                appendLine("        $langName.code to $providerName.map,")
            }

            appendLine("    )")
            appendLine()
            appendLine("    override fun get(")
            appendLine("        key: String?,")
            appendLine("        locale: Locale,")
            appendLine("        fallback: String,")
            appendLine("        vararg args: Any?,")
            appendLine("    ): String {")
            appendLine()
            appendLine("        if (key.isNullOrBlank()) return \"\"")
            appendLine()
            appendLine("        val text = langMap[locale.code]?.get(key) ?: fallback")
            appendLine()
            appendLine("        if (args.isEmpty()) return text")
            appendLine("        if (!text.contains(\"{\")) return text")
            appendLine()
            appendLine("        return format(text, *args)")
            appendLine("    }")
            appendLine()
            appendLine("    private fun format(")
            appendLine("        text: String,")
            appendLine("        vararg args: Any?")
            appendLine("    ): String {")
            appendLine("        var result = text")
            appendLine()
            appendLine("        args.forEachIndexed { index, value ->")
            appendLine("            result = result.replace(")
            appendLine("                oldValue = \"{\$index}\",")
            appendLine("                newValue = value?.toString().orEmpty()")
            appendLine("            )")
            appendLine("        }")
            appendLine()
            appendLine("        return result")
            appendLine("    }")
            appendLine("}")
            appendLine()

            translations.forEach { (locale, data) ->
                appendLangObject(
                    objectName = objectName,
                    locale = locale,
                    keys = keys,
                    data = data
                )
            }
        }
    }

    private fun StringBuilder.appendLangObject(
        objectName: String,
        locale: String,
        keys: List<String>,
        data: Map<String, String>
    ) {
        val providerName = providerObjectName(objectName, locale)

        // hide private
        appendLine("object $providerName {")
        appendLine("    val map = mapOf(")

        keys.forEach { key ->
            val value = data[key].orEmpty()

            appendLine(
                "        \"${escapeKotlinString(key)}\" to \"${escapeKotlinString(value)}\","
            )
        }

        appendLine("    )")
        appendLine("}")
        appendLine()
    }

    private fun providerObjectName(
        objectName: String,
        locale: String
    ): String {
        return "${objectName}_${locale.toProviderSuffix()}"
    }

    private fun String.toProviderSuffix(): String {
        return replace("-", "_")
            .replace(".", "_")
            .replace(Regex("[^A-Za-z0-9_]"), "_")
    }

    private fun String.toLangName(): String {
        return when (this) {
            "zh" -> "Zh"
            "en" -> "En"
            else -> replace("-", "_")
                .split("_")
                .filter { it.isNotBlank() }
                .joinToString("") { part ->
                    part.replaceFirstChar {
                        it.titlecase(Locale.ROOT)
                    }
                }
        }
    }

    private fun String.toLocaleValueName(): String {
        return "Lang_${toLangName()}"
    }

    private fun String.toFieldName(): String {
        val name = replace(".", "_")
            .replace("-", "_")
            .replace(Regex("[^A-Za-z0-9_]"), "_")
            .replace(Regex("_+"), "_")
            .trim('_')

        return when {
            name.isBlank() -> "_"
            name.first().isDigit() -> "_$name"
            name in kotlinKeywords -> "${name}_"
            else -> name
        }
    }

    private fun escapeKotlinString(value: String): String {
        return value
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t")
    }

    private fun StringBuilder.appendLocaleValues(
        locales: List<String>
    ) {
        appendLine("/**")
        appendLine(" * Auto generated by i18nZeal.")
        appendLine(" * Do not edit manually.")
        appendLine(" */")
        locales.forEach { locale ->
            appendLine("val ${locale.toLocaleValueName()} = Locale(\"${escapeKotlinString(locale)}\")")
        }
    }

    private val kotlinKeywords = setOf(
        "as", "break", "class", "continue", "do", "else", "false",
        "for", "fun", "if", "in", "interface", "is", "null",
        "object", "package", "return", "super", "this", "throw",
        "true", "try", "typealias", "typeof", "val", "var", "when", "while"
    )
}

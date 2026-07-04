package com.djx.i18n.runtime.android

import android.content.Context
import com.djx.i18n.runtime.export.Locale
import com.djx.i18n.runtime.interfaces.I18nEngine
import org.json.JSONObject
import java.io.File
import java.io.StringReader
import java.util.Properties

enum class DebugI18nFileType(
    internal val extension: String
) {
    JSON("json"),
    PROPERTIES("properties")
}

object AndroidDebugI18nLoader {

    fun fromAssets(
        context: Context,
        locales: List<String>,
        assetDir: String = "i18n",
        fileType: DebugI18nFileType = DebugI18nFileType.JSON,
        fallbackEngine: I18nEngine? = null,
        reloadOnEachGet: Boolean = true,
    ): I18nEngine {
        val appContext = context.applicationContext
        return AndroidDebugI18nEngine(
            locales = locales,
            fileType = fileType,
            fallbackEngine = fallbackEngine,
            reloadOnEachGet = reloadOnEachGet,
            readText = { locale, type ->
                runCatching {
                    appContext.assets.open("$assetDir/$locale.${type.extension}")
                        .bufferedReader(Charsets.UTF_8)
                        .use { it.readText() }
                }.getOrNull()
            }
        )
    }

    fun fromFiles(
        directory: File,
        locales: List<String>,
        fileType: DebugI18nFileType = DebugI18nFileType.JSON,
        fallbackEngine: I18nEngine? = null,
        reloadOnEachGet: Boolean = true,
    ): I18nEngine {
        return AndroidDebugI18nEngine(
            locales = locales,
            fileType = fileType,
            fallbackEngine = fallbackEngine,
            reloadOnEachGet = reloadOnEachGet,
            readText = { locale, type ->
                directory.resolve("$locale.${type.extension}")
                    .takeIf { it.isFile }
                    ?.readText(Charsets.UTF_8)
            }
        )
    }
}

private class AndroidDebugI18nEngine(
    private val locales: List<String>,
    private val fileType: DebugI18nFileType,
    private val fallbackEngine: I18nEngine?,
    private val reloadOnEachGet: Boolean,
    private val readText: (locale: String, fileType: DebugI18nFileType) -> String?,
) : I18nEngine {

    private var cachedTranslations: Map<String, Map<String, String>>? = null

    override fun get(
        key: String?,
        locale: Locale,
        fallback: String,
        vararg args: Any?,
    ): String {
        if (key.isNullOrBlank()) return ""

        val translations = if (reloadOnEachGet) {
            loadTranslations()
        } else {
            cachedTranslations ?: loadTranslations().also { cachedTranslations = it }
        }

        val text = translations[locale.code]?.get(key)
            ?: return fallbackEngine?.get(key, locale, fallback, *args) ?: fallback

        if (args.isEmpty() || !text.contains("{")) return text
        return format(text, *args)
    }

    private fun loadTranslations(): Map<String, Map<String, String>> {
        return locales.associateWith { locale ->
            val text = readText(locale, fileType).orEmpty()
            runCatching {
                parseText(text, fileType)
            }.getOrDefault(emptyMap())
        }
    }

    private fun parseText(
        text: String,
        fileType: DebugI18nFileType,
    ): Map<String, String> {
        if (text.isBlank()) return emptyMap()

        return when (fileType) {
            DebugI18nFileType.JSON -> flattenJson(JSONObject(text))
            DebugI18nFileType.PROPERTIES -> parseProperties(text)
        }
    }

    private fun flattenJson(
        jsonObject: JSONObject,
        prefix: String = "",
    ): Map<String, String> {
        val result = linkedMapOf<String, String>()
        val keys = jsonObject.keys()

        while (keys.hasNext()) {
            val key = keys.next()
            val fullKey = if (prefix.isBlank()) key else "$prefix.$key"
            val value = jsonObject.opt(key)

            when (value) {
                is JSONObject -> result += flattenJson(value, fullKey)
                null, JSONObject.NULL -> result[fullKey] = ""
                else -> result[fullKey] = value.toString()
            }
        }

        return result
    }

    private fun parseProperties(text: String): Map<String, String> {
        val properties = Properties()
        StringReader(text).use(properties::load)
        return properties.entries.associate { (key, value) ->
            key.toString() to value.toString()
        }
    }

    private fun format(
        text: String,
        vararg args: Any?,
    ): String {
        var result = text

        args.forEachIndexed { index, value ->
            result = result.replace(
                oldValue = "{$index}",
                newValue = value?.toString().orEmpty()
            )
        }

        return result
    }
}

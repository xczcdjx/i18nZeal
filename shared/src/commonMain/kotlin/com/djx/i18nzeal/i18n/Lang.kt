package com.djx.i18nzeal.i18n

import androidx.compose.runtime.Composable

enum class Lang(val code: String) {
    Zh("zh"),
    En("en")
}

object I18nZeal {

    private val langMap = mapOf(
        Lang.Zh to zhLang.map,
        Lang.En to enLang.map,
    )

    fun get(
        key: String?,
        lang: Lang,
        vararg args: Any?,
        fallback: String = key.orEmpty()
    ): String {
        if (key.isNullOrBlank()) return ""

        val text = langMap[lang]?.get(key) ?: fallback

        if (args.isEmpty()) return text
        if (!text.contains("{")) return text

        return format(text, *args)
    }

    private fun format(
        text: String,
        vararg args: Any?
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
@Composable
fun tr(key: String?,vararg args: Any?): String {
    val lang = AppLocalLangProvider.current

    return I18nZeal.get(
        key = key,
        lang = lang,
        *args,
        fallback = key.orEmpty()
    )
}
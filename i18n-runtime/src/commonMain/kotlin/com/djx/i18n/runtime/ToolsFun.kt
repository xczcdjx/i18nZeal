package com.djx.i18n.runtime


import androidx.compose.runtime.Composable

@Composable
fun tr(key: String?, vararg args: Any?): String {
    if (key.isNullOrBlank()) return ""

    val lang = AppLocalLangProvider.current

    return I18nRuntime.get(key, lang, key, *args)
}

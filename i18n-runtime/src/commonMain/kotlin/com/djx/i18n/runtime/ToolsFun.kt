package com.djx.i18n.runtime


import androidx.compose.runtime.Composable

@Composable
fun tr(key: String?, vararg args: Any?): String {

    val lang = AppLocalLangProvider.current

    if (key.isNullOrBlank()) return ""

    return I18nRuntime.get(
        key = key,
        *args,
        locale = lang,
        fallback = key
    )
}
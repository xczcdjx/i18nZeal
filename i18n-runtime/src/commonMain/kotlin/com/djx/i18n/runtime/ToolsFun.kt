package com.djx.i18n.runtime

import androidx.compose.runtime.Composable
import com.djx.i18nzeal.i18n.I18nZeal

@Composable
fun tr(key: String?, vararg args: Any?): String {

    val lang = AppLocalLangProvider.current

    if (key.isNullOrBlank()) return ""

    return I18nZeal.get(
        key = key,
        lang = lang,
        args = args,
        fallback = key
    )
}
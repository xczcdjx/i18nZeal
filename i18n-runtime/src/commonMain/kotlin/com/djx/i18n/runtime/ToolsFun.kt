package com.djx.i18n.runtime

import androidx.compose.runtime.Composable

@Composable
fun WithLangProvider(block: @Composable () -> Unit) {
    AppLocalLangProvider.current
    return block()
}

@Composable
fun tr(key: String?, vararg args: Any?): String {
    if (key.isNullOrBlank()) return ""

    val lang = AppLocalLangProvider.current

    return I18nRuntime.get(key, lang, key, *args)
}

@Composable
fun String?.tri18n(vararg args: Any?): String = tr(this,args)

fun trn(key: String?, vararg args: Any?): String {

    if (key.isNullOrBlank()) return ""

    return I18nRuntime.get(key = key, fallback = key, args = args)
}

fun String?.trnI18n(vararg args: Any?): String = trn(this,args)
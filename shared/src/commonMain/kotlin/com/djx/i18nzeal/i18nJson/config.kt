package com.djx.i18nzeal.i18nJson

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import com.djx.i18nzeal.i18n.I18nZeal
import com.djx.i18nzeal.i18n.Lang

object AppLangState {
    val current = mutableStateOf(Lang.Zh)

    fun change(lang: Lang) {
        current.value = lang
    }
}

val AppLocalLangProvider = compositionLocalOf {
    AppLangState.current.value
}

@Composable
fun tr(key: String?, vararg args: Any?): String {
    val lang = AppLocalLangProvider.current

    return I18nZeal.get(
        key = key,
        lang = lang,
        *args,
        fallback = key.orEmpty()
    )
}
package com.djx.i18nzeal.i18n

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf

object AppLangState {
    val current = mutableStateOf(Lang.Zh)

    fun change(lang: Lang) {
        current.value = lang
    }
}

val AppLocalLangProvider = compositionLocalOf {
    AppLangState.current.value
}
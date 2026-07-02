package com.djx.i18n.runtime

import androidx.compose.runtime.mutableStateOf
import com.djx.i18n.runtime.export.Locale
import com.djx.i18n.runtime.export.systemLocale

object AppLangState {
    val current = mutableStateOf<Locale>(systemLocale())

    fun change(lang: Locale) {
        current.value = lang
    }
}
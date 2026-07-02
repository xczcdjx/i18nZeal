package com.djx.i18n.runtime

import androidx.compose.runtime.mutableStateOf
import com.djx.i18nzeal.i18n.Lang

object AppLangState {
    val current = mutableStateOf<Lang?>(null)

    fun change(lang: Lang) {
        current.value = lang
    }
}
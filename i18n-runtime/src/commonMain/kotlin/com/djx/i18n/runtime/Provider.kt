package com.djx.i18n.runtime
import androidx.compose.runtime.compositionLocalOf
import com.djx.i18nzeal.i18n.Lang

val AppLocalLangProvider = compositionLocalOf<Lang> {
    error("No language provided")
}
package com.djx.i18n.runtime
import androidx.compose.runtime.compositionLocalOf
import com.djx.i18n.runtime.export.Locale
import com.djx.i18n.runtime.export.systemLocale

val AppLocalLangProvider = compositionLocalOf<Locale> {
    systemLocale()
}
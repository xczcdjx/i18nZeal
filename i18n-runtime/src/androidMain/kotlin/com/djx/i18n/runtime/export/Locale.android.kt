package com.djx.i18n.runtime.export


actual fun systemLocale(): Locale {
    val locale = java.util.Locale.getDefault()
    return Locale(locale.language)
}
package com.djx.i18n.runtime.export


actual fun systemLocale(): Locale {
    val lang = js("navigator.language") as String
    return Locale(lang)
}
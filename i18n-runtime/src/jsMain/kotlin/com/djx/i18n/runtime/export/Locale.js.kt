package com.djx.i18n.runtime.export


actual fun systemLocale(): Locale {
    return Locale(
        js("navigator.language") as String
    )
}
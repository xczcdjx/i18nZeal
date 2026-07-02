package com.djx.i18n.runtime.export

import platform.Foundation.currentLocale
import platform.Foundation.languageCode


actual fun systemLocale(): Locale {
    val locale = platform.Foundation.NSLocale.currentLocale.languageCode ?: "en"
    return Locale(locale)
}
package com.djx.i18n.runtime.export

data class Locale(val code: String)

expect fun systemLocale(): Locale
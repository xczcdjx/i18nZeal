package com.djx.i18n.runtime.export


@OptIn(kotlin.js.ExperimentalWasmJsInterop::class)
private val browserLanguage: String = js("navigator.language")

actual fun systemLocale(): Locale = Locale(browserLanguage)

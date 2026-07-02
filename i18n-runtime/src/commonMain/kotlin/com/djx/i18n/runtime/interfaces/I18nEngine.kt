package com.djx.i18n.runtime.interfaces

import com.djx.i18n.runtime.export.Locale


fun interface I18nEngine {
    fun get(
        key: String?,
        locale: Locale,
        fallback: String,
        vararg args: Any?
    ): String
}
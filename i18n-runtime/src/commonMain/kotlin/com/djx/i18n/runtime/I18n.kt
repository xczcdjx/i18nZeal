package com.djx.i18n.runtime

import com.djx.i18n.runtime.export.Locale
import com.djx.i18n.runtime.interfaces.I18nEngine

object I18nRuntime {

    lateinit var engine: I18nEngine

    fun init(engine: I18nEngine, initialLocale: Locale? = null) {
        this.engine = engine
        initialLocale?.let { AppLangState.change(it) }
    }

    fun get(
        key: String?,
        locale: Locale = AppLangState.current.value,
        fallback: String = key.orEmpty(),
        vararg args: Any? = emptyArray()
    ): String {
        return engine.get(
            key,
            locale,
            fallback,
            *args
        )
    }
}

package com.djx.i18n.runtime

import com.djx.i18n.runtime.export.Locale
import com.djx.i18n.runtime.export.systemLocale
import com.djx.i18n.runtime.interfaces.I18nEngine

object I18nRuntime {

    lateinit var engine: I18nEngine

    fun init(engine: I18nEngine) {
        this.engine = engine
    }

    fun get(
        key: String?,
        locale: Locale = systemLocale(),
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

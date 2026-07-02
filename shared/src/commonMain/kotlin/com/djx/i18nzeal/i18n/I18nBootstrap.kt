package com.djx.i18nzeal.i18n

import com.djx.i18n.runtime.I18nRuntime

object I18nBootstrap {

    fun init() {
        I18nRuntime.init { key, lang, args, fallback ->
            I18nZeal.get(
                key = key,
                lang = lang,
                args = args,
                fallback = fallback
            )
        }
    }
}
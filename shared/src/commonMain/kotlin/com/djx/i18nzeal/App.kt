package com.djx.i18nzeal

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.djx.i18n.runtime.AppLangState
import com.djx.i18n.runtime.AppLocalLangProvider
import com.djx.i18n.runtime.I18nRuntime
import com.djx.i18nzeal.components.MainContent
import com.djx.i18nzeal.constants.I18nZeal


@Composable
@Preview
fun App() {
    MaterialTheme {
  /*      I18nRuntime.init { key, locale, fallback, args ->
            I18nZeal.get(
                key = key,
                locale = locale,
                args = args,
                fallback = fallback
            )
        }*/
        CompositionLocalProvider(
            AppLocalLangProvider provides AppLangState.current.value
        ) {
            MainContent()
        }
    }
}
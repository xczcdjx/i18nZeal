package com.djx.i18nzealkmpdemo

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import com.djx.i18n.runtime.AppLangState
import com.djx.i18n.runtime.AppLocalLangProvider
import com.djx.i18n.runtime.I18nRuntime
import com.djx.i18nzealkmpdemo.components.I18nTest
import com.djx.i18nzealkmpdemo.i18n.I18nZeal


@Composable
@Preview
fun App() {
    I18nRuntime.init(I18nZeal)
    MaterialTheme {
        CompositionLocalProvider(
            AppLocalLangProvider provides AppLangState.current.value
        ){
            I18nTest()
        }
    }
}
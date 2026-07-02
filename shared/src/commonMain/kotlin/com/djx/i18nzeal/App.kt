package com.djx.i18nzeal

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.djx.i18nzeal.components.MainContent
import com.djx.i18nzeal.i18nJson.AppLangState
import com.djx.i18nzeal.i18nJson.AppLocalLangProvider


@Composable
@Preview
fun App() {
    MaterialTheme {
        CompositionLocalProvider(
            AppLocalLangProvider provides AppLangState.current.value
        ){
            MainContent()
        }
    }
}
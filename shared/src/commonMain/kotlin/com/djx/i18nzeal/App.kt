package com.djx.i18nzeal

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.djx.i18nzeal.components.MainContent
import com.djx.i18nzeal.i18n.AppLangState
import com.djx.i18nzeal.i18n.AppLocalLangProvider
import org.jetbrains.compose.resources.painterResource

import i18nzeal.shared.generated.resources.Res
import i18nzeal.shared.generated.resources.compose_multiplatform

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
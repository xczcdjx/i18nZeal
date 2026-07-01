package com.djx.i18nzeal

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "I18nZeal",
    ) {
        App()
    }
}
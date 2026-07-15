package com.djx.i18nzealandroiddemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.djx.composerespect.i18n.I18nZeal
import com.djx.i18n.runtime.AppLangState
import com.djx.i18n.runtime.AppLocalLangProvider
import com.djx.i18n.runtime.I18nRuntime
import com.djx.i18nzealandroiddemo.components.I18nTest
import com.djx.i18nzealandroiddemo.ui.theme.I18nZealAndroidDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        I18nRuntime.init(I18nZeal)
        setContent {
            I18nZealAndroidDemoTheme {
                CompositionLocalProvider(
                    AppLocalLangProvider provides AppLangState.current.value
                ) {
                    I18nTest()
                }
            }
        }
    }
}
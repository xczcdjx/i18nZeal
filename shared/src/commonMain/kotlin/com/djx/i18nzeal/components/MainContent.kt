package com.djx.i18nzeal.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.djx.i18n.runtime.AppLangState
import com.djx.i18n.runtime.WithLangProvider
import com.djx.i18n.runtime.tr
import com.djx.i18n.runtime.tri18n
import com.djx.i18n.runtime.trn
import com.djx.i18n.runtime.trnI18n
import com.djx.i18nzeal.i18n.I18nKeys
import com.djx.i18nzeal.i18n.Lang_En
import com.djx.i18nzeal.i18n.Lang_Zh

@Composable
fun MainContent(modifier: Modifier = Modifier) {
    var count by remember {
        mutableIntStateOf(1)
    }
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text("app.name".tri18n())
            })
        }
    ) { paddingValues ->
        Column(modifier.padding(paddingValues)) {
            Text(tr(I18nKeys.hello, "count" to count, "name" to "ZhangSan"))
            Text(I18nKeys.hello.tri18n(mapOf("count" to count, "name" to "ZhangSan")))
            Text(trn(I18nKeys.hello, "ZhangSan", count))
            Text(I18nKeys.hello.trnI18n())
            Text(tr(I18nKeys.count, count))
            TextButton({
                count += 1
            }) {
                Text("Count++")
            }
            WithLangProvider {
                Column {
                    Text(trn(I18nKeys.lang_current))
                    Text(I18nKeys.lang_system.trnI18n())
                }
            }
            Row {
                Button(
                    onClick = {
                        AppLangState.change(Lang_En)
                    }
                ) {
                    Text("English")
                }

                Button(
                    onClick = {
                        AppLangState.change(Lang_Zh)
                    }
                ) {
                    Text("中文")
                }
            }
        }
    }
}

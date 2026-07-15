package com.djx.i18nzealkmpdemo.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.djx.i18nzealkmpdemo.i18n.I18nKeys
import com.djx.i18n.runtime.AppLangState
import com.djx.i18n.runtime.WithLangProvider
import com.djx.i18n.runtime.export.Locale
import com.djx.i18n.runtime.export.systemLocale
import com.djx.i18n.runtime.tr
import com.djx.i18n.runtime.tri18n
import com.djx.i18n.runtime.trn
import com.djx.i18n.runtime.trnI18n

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun I18nTest() {
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(tr(I18nKeys.app_name))
            },
            actions = {
                LangSwitch()
            })
    }) { paddingValues ->
        Column(
            Modifier
                .padding(top = paddingValues.calculateTopPadding(), start = 10.dp)
                .then(Modifier)
        ) {
            CountTest()
            Spacer(Modifier.height(15.dp))
            Text(
                I18nKeys.poem.tri18n(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic
            )
        }
    }
}

@Composable
fun CountTest(){
    var count by remember {
        mutableIntStateOf(0)
    }
    Text(I18nKeys.count.tri18n(count))
    Text(tr(I18nKeys.count, count))
    Text(tr(I18nKeys.hello, "count" to count, "name" to "ZhangSan"))
    WithLangProvider {
        Text(I18nKeys.count.trnI18n(count))
        Text(trn(I18nKeys.count, count))
    }
    TextButton({
        count += 1
    }) {
        Text("Count ++")
    }
}
@Composable
private fun LangSwitch(modifier: Modifier = Modifier) {
    var lang by remember {
        mutableStateOf("system")
    }
    val langList = listOf(
        "system" to I18nKeys.lang_system,
        "zh" to I18nKeys.lang_zh,
        "en" to I18nKeys.lang_en,
    )
    var showDrop by remember {
        mutableStateOf(false)
    }
    val currentLangText = langList
        .firstOrNull { it.first == lang }
        ?.second
        ?: lang
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box() {
            TextButton(
                {
                    showDrop = true
                }
            ) {
                Text(tr(currentLangText))
            }
            DropdownMenu(showDrop, {
                showDrop = false
            }) {
                langList.forEach { (k, v) ->
                    DropdownMenuItem(text = {
                        Text(
                            tr(v),
                            color = if (k == lang) MaterialTheme.colorScheme.primary else Color.Unspecified
                        )
                    }, onClick = {
                        lang = k
                        AppLangState.change(Locale(getSwitchSupportLanguage(k)))
                        showDrop = false
                    })
                }
            }
        }
    }
}

private fun getSwitchSupportLanguage(lang: String) = when (lang) {
    "system" -> systemLocale().code
    "zh" -> "zh"
    "en" -> "en"
    else -> "en"
}
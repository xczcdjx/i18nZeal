package com.djx.i18nzeal.font

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontFamily
import i18nzeal.shared.generated.resources.Res
import i18nzeal.shared.generated.resources.ft
import org.jetbrains.compose.resources.Font as ResourceFont
// 修复webApp 中文乱码
@Composable
fun appTypography(): Typography {
    val appFontFamily = FontFamily(
        ResourceFont(Res.font.ft)
    )

    return remember(appFontFamily) {
        val base = androidx.compose.material3.Typography()
        Typography(
            displayLarge = base.displayLarge.copy(fontFamily = appFontFamily),
            displayMedium = base.displayMedium.copy(fontFamily = appFontFamily),
            displaySmall = base.displaySmall.copy(fontFamily = appFontFamily),
            headlineLarge = base.headlineLarge.copy(fontFamily = appFontFamily),
            headlineMedium = base.headlineMedium.copy(fontFamily = appFontFamily),
            headlineSmall = base.headlineSmall.copy(fontFamily = appFontFamily),
            titleLarge = base.titleLarge.copy(fontFamily = appFontFamily),
            titleMedium = base.titleMedium.copy(fontFamily = appFontFamily),
            titleSmall = base.titleSmall.copy(fontFamily = appFontFamily),
            bodyLarge = base.bodyLarge.copy(fontFamily = appFontFamily),
            bodyMedium = base.bodyMedium.copy(fontFamily = appFontFamily),
            bodySmall = base.bodySmall.copy(fontFamily = appFontFamily),
            labelLarge = base.labelLarge.copy(fontFamily = appFontFamily),
            labelMedium = base.labelMedium.copy(fontFamily = appFontFamily),
            labelSmall = base.labelSmall.copy(fontFamily = appFontFamily),
        )
    }
}
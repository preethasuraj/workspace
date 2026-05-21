package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.example.ui.tokens.RealtorTypography
import com.example.ui.tokens.realtorColors

val LightColrs = lightColorScheme(

)
@Composable
fun RealtorTheme(
    dark: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalRealtorColor provides realtorColors(dark)
    ) {
        val colors = LightColrs
        MaterialTheme(
            colorScheme = colors,
            typography = RealtorTypography,
            content = content
        )
    }
}
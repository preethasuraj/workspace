package com.example.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import com.example.ui.tokens.RealtorColors

object RealtorTokens{
    val colors: RealtorColors
        @Composable get() = LocalRealtorColor.current
}

val LocalRealtorColor = staticCompositionLocalOf<RealtorColors> { error("") }


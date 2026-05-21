package com.example.ui.tokens

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


data class RealtorColors(
    val rating: Color ,
    val error : Color ,
)

data class RealtorType(
    val priceType: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    )
)

private val darkColor = RealtorColors(
    rating = Color.Yellow,
    error = Color.Red
)

internal fun realtorColors(dark: Boolean) = darkColor
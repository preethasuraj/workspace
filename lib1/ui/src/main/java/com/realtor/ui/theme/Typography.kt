package com.realtor.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.font.FontWeight.Companion.Normal
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.sp

internal val RealtorTypography = Typography(
    titleLarge = TextStyle(fontSize = 28.sp, fontWeight = SemiBold, lineHeight = 20.sp),
    bodyLarge = TextStyle(fontSize = 24.sp, fontWeight = Normal, lineHeight = 16.sp),
    labelLarge =  TextStyle(fontSize = 20.sp, fontWeight = Medium, lineHeight = 14.sp),
)
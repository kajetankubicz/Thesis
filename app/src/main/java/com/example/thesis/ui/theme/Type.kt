package com.example.thesis.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.thesis.R

private val ArialFontFamily = FontFamily(
    fonts = listOf(
        Font(resId = R.font.arial_narrow, FontWeight.W300),
        Font(resId = R.font.arial_th, FontWeight.W400),
        Font(resId = R.font.arial_mt_black, FontWeight.W500),
        Font(resId = R.font.arial_geo_bold, FontWeight.W600),
    )
)

private val HelveticaFontFamily = FontFamily(
    fonts = listOf(
        Font(resId = R.font.helvetica_light, FontWeight.W300),
        Font(resId = R.font.helvetica_oblique, FontWeight.W400),
        Font(resId = R.font.helvetica, FontWeight.W500),
        Font(resId = R.font.helvetica_bold, FontWeight.W600),
        Font(resId = R.font.helvetica_bold_oblique, FontWeight.W700),
    )
)

private val CourierFontFamily = FontFamily(
    fonts = listOf(
        Font(resId = R.font.courier, FontWeight.W300),
    )
)

private val OpenDyslexicFontFamily = FontFamily(
    fonts = listOf(
        Font(resId = R.font.open_dyslexic3_regular, FontWeight.W300),
        Font(resId = R.font.open_dyslexic3_bold, FontWeight.W600),
    )
)

private val VerdanaFontFamily = FontFamily(
    fonts = listOf(
        Font(resId = R.font.verdana, FontWeight.W300),
        Font(resId = R.font.verdana_bold, FontWeight.W600),)
)

val appFontFamilyTypography = Typography(
    displayMedium = TextStyle(fontFamily = OpenDyslexicFontFamily, fontWeight = FontWeight.Normal, fontSize = 30.sp),
    displayLarge = TextStyle(fontFamily = OpenDyslexicFontFamily, fontWeight = FontWeight.Bold, fontSize = 30.sp),
    bodyLarge = TextStyle(fontFamily = HelveticaFontFamily, fontWeight = FontWeight.W500, fontSize = 30.sp)
)
package com.example.storeaccounting.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.storeaccounting.R

val persian_font_black = Font(R.font.vazirmatn_black)
val persian_font_bold= Font(R.font.vazirmatn_bold)
val persian_font_light = Font(R.font.vazirmatn_light)
val persian_font_extra_bold = Font(R.font.vazirmatn_extra_bold)
val persian_font_medium = Font(R.font.vazirmatn_medium)
val persian_font_extra_light = Font(R.font.vazirmatn_extra_light)
val persian_font_regular = Font(R.font.vazirmatn_regular)
val persian_font_semi_bold = Font(R.font.vazirmatn_semi_bold)
val persian_font_thin = Font(R.font.vazirmatn_thin)


// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)
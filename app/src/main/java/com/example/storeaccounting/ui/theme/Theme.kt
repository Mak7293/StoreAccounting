package com.example.storeaccounting.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = custom_blue_8,
    primaryVariant = Color.White,
    secondary = Teal200,
    surface = Color.Yellow,
    background = custom_blue_2,
    onSurface = custom_blue_0,
    secondaryVariant = custom_gray_4,
    onPrimary = Color.Black
)

private val LightColorPalette = lightColors(
    primary = custom_blue_2,
    primaryVariant = Color.Black,
    secondary = custom_blue_5,
    surface = custom_blue_1,
    background = custom_blue_8,
    onSurface = custom_gray_4,
    secondaryVariant = custom_gray_0,
    onPrimary = Color.White


    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun StoreAccountingTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
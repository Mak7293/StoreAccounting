package com.example.storeaccounting.presentation.util

sealed class ThemeState(val theme: String){
    object ThemeDay: ThemeState(theme = "day")
    object ThemeNight: ThemeState(theme = "night")
    object ThemeDefault: ThemeState(theme = "default")
}

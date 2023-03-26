package com.example.storeaccounting.presentation.view_model.setting

sealed class SettingEvent {
    data class SaveTheme(val theme: String): SettingEvent()
}
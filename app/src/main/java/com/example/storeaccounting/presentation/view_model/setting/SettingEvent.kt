package com.example.storeaccounting.presentation.view_model.setting

import android.content.Context

sealed class SettingEvent {
    data class SaveTheme(val theme: String): SettingEvent()
    object CreateBackup: SettingEvent()
    object RestoreBackup: SettingEvent()
}
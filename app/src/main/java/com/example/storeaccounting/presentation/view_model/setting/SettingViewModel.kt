package com.example.storeaccounting.presentation.view_model.setting

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeaccounting.presentation.util.Constants.THEME_KEY
import com.example.storeaccounting.presentation.util.ThemeState
import com.example.storeaccounting.presentation.view_model.general.GeneralViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val applicationContext: Application,
    private val dataStore: DataStore<Preferences>
):ViewModel() {


    fun onEvent(event: SettingEvent){
        when(event){
            is SettingEvent.SaveTheme  ->  {
                saveCurrentTheme(event.theme)
            }
        }
    }
    private fun saveCurrentTheme(theme: String){
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[THEME_KEY] = theme
            }
        }
    }
    fun readCurrentThemeForDataStore(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[THEME_KEY] ?: ThemeState.ThemeDefault.theme
        }
    }
}
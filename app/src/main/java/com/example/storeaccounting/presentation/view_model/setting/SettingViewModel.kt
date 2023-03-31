package com.example.storeaccounting.presentation.view_model.setting

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeaccounting.data.data_source.Database
import com.example.storeaccounting.presentation.MainActivity
import com.example.storeaccounting.presentation.util.Constants.THEME_KEY
import com.example.storeaccounting.presentation.util.ThemeState
import com.example.storeaccounting.presentation.view_model.general.GeneralViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import de.raphaelebner.roomdatabasebackup.core.RoomBackup
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import kotlin.system.exitProcess

@HiltViewModel
class SettingViewModel @Inject constructor(
    val applicationContext: Application,
    private val dataStore: DataStore<Preferences>,
    val database: Database
):ViewModel() {


    fun onEvent(event: SettingEvent){
        when(event){
            is SettingEvent.SaveTheme  ->  {
                saveCurrentTheme(event.theme)
            }
            is SettingEvent.CreateBackup  ->  {
                applicationContext.createBackup()
            }
            is SettingEvent.RestoreBackup   ->  {
                applicationContext.restoreBackup()
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
    fun Application.createBackup(){
        val sDir = File("${Environment.getExternalStorageDirectory()}")
        val fileName = "StoreStorageBackUp"
        val sfPath = sDir.path + File.separator + fileName
        if (!sDir.exists()) {
            sDir.mkdirs()
        }
        val db = getDatabasePath(Database.DATABASE_NAME).absolutePath
        val wal = getDatabasePath("${Database.DATABASE_NAME}-wal").absolutePath
        val shm = getDatabasePath("${Database.DATABASE_NAME}-shm").absolutePath
        File(db).copyTo(File(sfPath, Database.DATABASE_NAME), true)
        File(wal).copyTo(File(sfPath, "${Database.DATABASE_NAME}-wal"), true)
        File(shm).copyTo(File(sfPath, "${Database.DATABASE_NAME}-shm"), true)
        Toast.makeText(this,"فایل های بک آپ با موفقیت ذخیره شد:$sfPath ",Toast.LENGTH_LONG).show()
    }
    fun Application.restoreBackup(){

        val sDir = File("${Environment.getExternalStorageDirectory()}")
        val fileName = "StoreStorageBackUp"
        val sfPath = sDir.path + File.separator + fileName
        if (!File(sfPath).exists()) {
            Toast.makeText(this,"فایل بک آپ در مسیر مورد نظر پیدا نشد،" +
                    " لطفا ابتدا اقدام به گرفتن بک آپ نمایید.",Toast.LENGTH_LONG).show()
            return
        }
        val dbExternal = "$sfPath/${Database.DATABASE_NAME}"
        val walExternal = "$sfPath/${Database.DATABASE_NAME}-wal"
        val shmExternal = "$sfPath/${Database.DATABASE_NAME}-shm"

        val db = getDatabasePath("${Database.DATABASE_NAME}").absolutePath
        val wal = getDatabasePath("${Database.DATABASE_NAME}-wal").absolutePath
        val shm = getDatabasePath("${Database.DATABASE_NAME}-shm").absolutePath
        File(dbExternal).copyTo(File(db), true)
        File(walExternal).copyTo(File(wal), true)
        File(shmExternal).copyTo(File(shm), true)
        triggerRebirth(MainActivity::class.java)
    }
    fun Application.triggerRebirth(myClass: Class<*>?) {
        val intent = Intent(this, myClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        this.startActivity(intent)
        exitProcess(0)
    }
}
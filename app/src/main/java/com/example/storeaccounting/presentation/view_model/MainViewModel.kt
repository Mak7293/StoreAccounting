package com.example.storeaccounting.presentation.view_model

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import saman.zamani.persiandate.PersianDate
import javax.inject.Inject

@HiltViewModel
class MainViewModel@Inject constructor():ViewModel() {

    fun getPersianDate(): PersianDate{
        return PersianDate()
    }
}
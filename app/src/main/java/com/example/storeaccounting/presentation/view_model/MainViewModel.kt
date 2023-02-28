package com.example.storeaccounting.presentation.view_model

import androidx.lifecycle.ViewModel
import com.example.storeaccounting.domain.use_case.TransactionUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import saman.zamani.persiandate.PersianDate
import javax.inject.Inject

@HiltViewModel
class MainViewModel@Inject constructor(
    private val transactionUseCases: TransactionUseCases):ViewModel() {

    fun getPersianDate(): PersianDate{
        return PersianDate()
    }
}
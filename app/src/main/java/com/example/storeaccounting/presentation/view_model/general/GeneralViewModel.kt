package com.example.storeaccounting.presentation.view_model.general

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.storeaccounting.domain.use_case.general_use_case.GeneralUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GeneralViewModel@Inject constructor(
    applicationContext: Application,
    generalUseCases: GeneralUseCases
): ViewModel(){


}
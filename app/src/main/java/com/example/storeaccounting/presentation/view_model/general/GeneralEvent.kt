package com.example.storeaccounting.presentation.view_model.general

import android.graphics.Bitmap
import com.example.storeaccounting.domain.model.CreditCard

sealed class GeneralEvent {
    data class CreateCreditCard(val creditCard: CreditCard):GeneralEvent()
    data class UpdateCreditCard(val creditCard: CreditCard):GeneralEvent()
    data class DeleteCreditCard(val creditCard: CreditCard):GeneralEvent()
    data class FilterCreditCard(val query: String):GeneralEvent()
    data class SaveFactorInPdf(val bitmap: Bitmap?):GeneralEvent()
}
package com.example.storeaccounting.presentation.view_model.general

import com.example.storeaccounting.domain.model.CreditCard

sealed class GeneralEvent {
    data class CreateCreditCard(val creditCard: CreditCard):GeneralEvent()
    data class UpdateCreditCard(val creditCard: CreditCard):GeneralEvent()
    data class DeleteCreditCard(val creditCard: CreditCard):GeneralEvent()
    data class FilterCreditCard(val query: String):GeneralEvent()
}
package com.example.storeaccounting.presentation.view_model.general

import com.example.storeaccounting.domain.model.CreditCard
import com.example.storeaccounting.domain.model.History
import com.example.storeaccounting.domain.model.InventoryEntity

data class GeneralViewModelState (
    val creditCard: List<CreditCard> = emptyList(),
    var filteredCreditCard: List<CreditCard> = emptyList()
)
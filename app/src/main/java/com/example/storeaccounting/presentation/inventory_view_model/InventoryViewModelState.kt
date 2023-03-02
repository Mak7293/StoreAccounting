package com.example.storeaccounting.presentation.inventory_view_model

import com.example.storeaccounting.domain.model.Transaction

data class InventoryViewModelState (
    val inventory: List<Transaction> = emptyList()
)
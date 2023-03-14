package com.example.storeaccounting.presentation.view_model

import com.example.storeaccounting.domain.model.History
import com.example.storeaccounting.domain.model.InventoryEntity

data class ViewModelState (
    val inventory: List<InventoryEntity> = emptyList(),
    val history: List<History> = emptyList(),
    var filteredInventory: List<InventoryEntity> = emptyList(),
    var filteredHistory: List<History> = emptyList()
)
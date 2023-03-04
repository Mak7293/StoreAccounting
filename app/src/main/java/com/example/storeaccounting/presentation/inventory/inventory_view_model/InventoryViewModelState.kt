package com.example.storeaccounting.presentation.inventory.inventory_view_model

import com.example.storeaccounting.domain.model.InventoryEntity

data class InventoryViewModelState (
    val inventory: List<InventoryEntity> = emptyList()
)
package com.example.storeaccounting.presentation.inventory_view_model

import com.example.storeaccounting.domain.model.Transaction

sealed class InventoryEvent {
    data class InsertInventory(val transaction:Transaction): InventoryEvent()
    data class UpdateInventory(val transaction:Transaction): InventoryEvent()
    data class DeleteInventory(val transaction:Transaction): InventoryEvent()
}
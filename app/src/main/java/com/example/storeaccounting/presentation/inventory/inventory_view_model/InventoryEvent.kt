package com.example.storeaccounting.presentation.inventory.inventory_view_model

import com.example.storeaccounting.domain.model.History
import com.example.storeaccounting.domain.model.InventoryEntity

sealed class InventoryEvent {
    data class InsertInventory(val inventoryEntity:InventoryEntity): InventoryEvent()
    data class UpdateInventory(val inventoryEntity:InventoryEntity): InventoryEvent()
    data class DeleteInventory(val inventoryEntity:InventoryEntity): InventoryEvent()
}
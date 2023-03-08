package com.example.storeaccounting.presentation.view_model

import com.example.storeaccounting.domain.model.InventoryEntity

sealed class Event {
    data class InsertInventory(val inventoryEntity:InventoryEntity): Event()
    data class UpdateInventory(val inventoryEntity:InventoryEntity): Event()
    data class DeleteInventory(val inventoryEntity:InventoryEntity): Event()
    data class SaleInventory(
        val newInventoryEntity: InventoryEntity, val previousInventory:InventoryEntity): Event()
}
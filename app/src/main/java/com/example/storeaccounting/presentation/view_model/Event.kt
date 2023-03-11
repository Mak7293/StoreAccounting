package com.example.storeaccounting.presentation.view_model

import com.example.storeaccounting.domain.model.History
import com.example.storeaccounting.domain.model.InventoryEntity

sealed class Event {
    data class InsertInventory(val inventoryEntity:InventoryEntity): Event()
    data class UpdateInventory(val inventoryEntity:InventoryEntity): Event()
    data class DeleteInventory(val inventoryEntity:InventoryEntity): Event()
    data class SaleInventory(
        val inventoryEntity: InventoryEntity, val history: History): Event()
    data class UpdateSaleTransaction(val inventoryEntity: InventoryEntity,
                                     val newHistory: History, val oldHistory: History): Event()
    data class DeleteSaleHistory(val history: History): Event()
}
package com.example.storeaccounting.presentation.view_model.inventory_sale

import com.example.storeaccounting.domain.model.History
import com.example.storeaccounting.domain.model.InventoryEntity
import saman.zamani.persiandate.PersianDate

sealed class InventorySaleEvent {
    data class InsertInventory(val inventoryEntity:InventoryEntity): InventorySaleEvent()
    data class UpdateInventory(val inventoryEntity:InventoryEntity): InventorySaleEvent()
    data class DeleteInventory(val inventoryEntity:InventoryEntity): InventorySaleEvent()
    data class SaleInventory(
        val inventoryEntity: InventoryEntity,
        val history: History): InventorySaleEvent()
    data class UpdateSaleTransaction(val inventoryEntity: InventoryEntity,
                                     val newHistory: History, val oldHistory: History): InventorySaleEvent()
    data class DeleteSaleHistory(val history: History): InventorySaleEvent()
    data class FilterSaleHistory(val map: Map<String,PersianDate>?): InventorySaleEvent()
    data class FilterInventory(val query:String ): InventorySaleEvent()
}
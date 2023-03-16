package com.example.storeaccounting.presentation.view_model.inventory_sale

import com.example.storeaccounting.domain.model.History
import com.example.storeaccounting.domain.model.InventoryEntity
import saman.zamani.persiandate.PersianDate

sealed class InvetorySaleEvent {
    data class InsertInventory(val inventoryEntity:InventoryEntity): InvetorySaleEvent()
    data class UpdateInventory(val inventoryEntity:InventoryEntity): InvetorySaleEvent()
    data class DeleteInventory(val inventoryEntity:InventoryEntity): InvetorySaleEvent()
    data class SaleInventory(
        val inventoryEntity: InventoryEntity, val history: History): InvetorySaleEvent()
    data class UpdateSaleTransaction(val inventoryEntity: InventoryEntity,
                                     val newHistory: History, val oldHistory: History): InvetorySaleEvent()
    data class DeleteSaleHistory(val history: History): InvetorySaleEvent()
    data class FilterSaleHistory(val map: Map<String,PersianDate>?): InvetorySaleEvent()
    data class FilterInventory(val query:String ): InvetorySaleEvent()
}
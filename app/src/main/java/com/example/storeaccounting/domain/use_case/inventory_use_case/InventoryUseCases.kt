package com.example.storeaccounting.domain.use_case.inventory_use_case

data class InventoryUseCases (
    val addInventory: AddInventory,
    val getInventory: GetInventory,
    val deleteInventory: DeleteInventory,
    val updateInventory: UpdateInventory,
    val getHistoryListForSpecificInventory: GetHistoryListForSpecificInventory,
    val getHistory: GetHistory,
    val saleInventory: SaleInventory,
    val updateSaleHistory: UpdateSaleHistory,
    val deleteSaleHistory: DeleteSaleHistory
)
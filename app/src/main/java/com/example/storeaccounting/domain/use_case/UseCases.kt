package com.example.storeaccounting.domain.use_case

import com.example.storeaccounting.presentation.inventory.inventory_view_model.InventoryEvent

data class UseCases (
    val addInventory: AddInventory,
    val getInventory: GetInventory,
    val deleteInventory: DeleteInventory,
    val updateInventory: UpdateInventory,
    val getHistoryListForSpecificInventory: GetHistoryListForSpecificInventory
)
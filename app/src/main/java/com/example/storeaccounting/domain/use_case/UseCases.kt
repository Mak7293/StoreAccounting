package com.example.storeaccounting.domain.use_case

data class UseCases (
    val addInventory: AddInventory,
    val getInventory: GetInventory,
    val deleteInventory: DeleteInventory,
)
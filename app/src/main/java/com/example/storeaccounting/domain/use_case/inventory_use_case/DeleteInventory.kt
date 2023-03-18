package com.example.storeaccounting.domain.use_case.inventory_use_case

import com.example.storeaccounting.domain.model.InventoryEntity
import com.example.storeaccounting.domain.repository.Repository

class DeleteInventory(private val repository: Repository) {

    suspend operator fun invoke(inventoryEntity: InventoryEntity){

        val inventoryWithHistory = repository.getHistoriesByInventoryTimeStamp(inventoryEntity.createdTimeStamp)
        inventoryWithHistory.history.forEach {
            repository.deleteHistory(it)
        }
        repository.deleteInventory(inventoryEntity)

    }
}
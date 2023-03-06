package com.example.storeaccounting.domain.use_case

import android.util.Log
import com.example.storeaccounting.domain.model.InventoryEntity
import com.example.storeaccounting.domain.repository.InventoryRepository
import kotlinx.coroutines.flow.collect

class DeleteInventory(private val repository: InventoryRepository) {

    suspend operator fun invoke(inventoryEntity: InventoryEntity){

        val inventoryWithHistory = repository.getHistoriesByInventoryTimeStamp(inventoryEntity.createdTimeStamp)
        inventoryWithHistory.history.forEach {
            repository.deleteHistory(it)
        }
        repository.deleteInventory(inventoryEntity)

    }
}
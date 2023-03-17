package com.example.storeaccounting.domain.use_case.inventory_use_case

import com.example.storeaccounting.domain.model.InventoryEntity
import com.example.storeaccounting.domain.model.InventoryWithHistory
import com.example.storeaccounting.domain.repository.InventoryRepository
import kotlinx.coroutines.flow.Flow

class GetHistoryListForSpecificInventory(private val repository: InventoryRepository) {

    suspend operator fun invoke(timeStamp: Long): InventoryWithHistory {
        return repository.getHistoriesByInventoryTimeStamp(timeStamp)
    }
}
package com.example.storeaccounting.domain.use_case.inventory_use_case

import com.example.storeaccounting.domain.model.InventoryWithHistory
import com.example.storeaccounting.domain.repository.Repository

class GetHistoryListForSpecificInventory(private val repository: Repository) {

    suspend operator fun invoke(timeStamp: Long): InventoryWithHistory {
        return repository.getHistoriesByInventoryTimeStamp(timeStamp)
    }
}
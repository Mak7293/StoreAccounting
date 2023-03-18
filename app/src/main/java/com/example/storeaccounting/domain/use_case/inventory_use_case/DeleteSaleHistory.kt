package com.example.storeaccounting.domain.use_case.inventory_use_case

import com.example.storeaccounting.domain.model.History
import com.example.storeaccounting.domain.repository.Repository

class DeleteSaleHistory(private val repository: Repository) {

    suspend operator fun invoke(history: History){
        val rollbackInventory = repository.getHistoriesByInventoryTimeStamp(history.createdTimeStamp)
            .inventory.copy(number = (history.remainingInventory + history.saleNumber.toInt()).toString())
        repository.updateInventory(rollbackInventory)
        repository.deleteHistory(history)
    }
}
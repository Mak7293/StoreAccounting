package com.example.storeaccounting.domain.use_case

import com.example.storeaccounting.domain.model.InventoryEntity
import com.example.storeaccounting.domain.repository.InventoryRepository
import kotlinx.coroutines.flow.Flow


class GetInventory(private val repository: InventoryRepository) {

    operator fun invoke(): Flow<List<InventoryEntity>> {
        return repository.fetchAllInventory()
    }
}
package com.example.storeaccounting.domain.use_case.inventory_use_case

import com.example.storeaccounting.domain.model.InventoryEntity
import com.example.storeaccounting.domain.repository.Repository
import kotlinx.coroutines.flow.Flow


class GetInventory(private val repository: Repository) {

    operator fun invoke(): Flow<List<InventoryEntity>> {
        return repository.fetchAllInventory()
    }
}
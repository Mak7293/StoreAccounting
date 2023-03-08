package com.example.storeaccounting.domain.use_case

import com.example.storeaccounting.domain.model.History
import com.example.storeaccounting.domain.repository.InventoryRepository
import kotlinx.coroutines.flow.Flow

class GetHistory(private val repository: InventoryRepository) {

    operator fun invoke(): Flow<List<History>> {
        return repository.fetchAllHistory()
    }
}
package com.example.storeaccounting.domain.use_case.inventory_use_case

import com.example.storeaccounting.domain.model.History
import com.example.storeaccounting.domain.repository.Repository
import kotlinx.coroutines.flow.Flow

class GetHistory(private val repository: Repository) {

    operator fun invoke(): Flow<List<History>> {
        return repository.fetchAllHistory()
    }
}
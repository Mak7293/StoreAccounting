package com.example.storeaccounting.domain.use_case

import com.example.storeaccounting.domain.model.Transaction
import com.example.storeaccounting.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow


class GetInventoryTransaction(private val repository: TransactionRepository) {

    operator fun invoke(): Flow<List<Transaction>> {
        return repository.fetchAllInventoryTransaction()
    }
}
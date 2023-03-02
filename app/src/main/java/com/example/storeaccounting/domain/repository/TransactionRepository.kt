package com.example.storeaccounting.domain.repository

import com.example.storeaccounting.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {

    suspend fun insertTransaction(transaction: Transaction)

    suspend fun deleteTransaction(transaction: Transaction)

    suspend fun updateTransaction(transaction: Transaction)

    fun fetchAllTransaction(): Flow<List<Transaction>>
    fun fetchAllInventoryTransaction():Flow<List<Transaction>>
}
package com.example.storeaccounting.data.repository

import com.example.storeaccounting.data.data_source.TransactionDao
import com.example.storeaccounting.domain.model.Transaction
import com.example.storeaccounting.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow

class TransactionRepositoryImp(private val dao: TransactionDao): TransactionRepository {
    override suspend fun insertTransaction(transaction: Transaction) {
        dao.insertTransaction(transaction)
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        dao.deleteTransaction(transaction)
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        dao.updateTransaction(transaction)
    }

    override fun fetchAllTransaction(): Flow<List<Transaction>> {
        return dao.fetchAllData()
    }
}
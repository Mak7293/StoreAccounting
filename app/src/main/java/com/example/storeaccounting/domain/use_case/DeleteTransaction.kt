package com.example.storeaccounting.domain.use_case

import com.example.storeaccounting.domain.model.Transaction
import com.example.storeaccounting.domain.repository.TransactionRepository

class DeleteTransaction(private val repository: TransactionRepository) {

    suspend operator fun invoke(transaction: Transaction){
        repository.deleteTransaction(transaction)
    }
}
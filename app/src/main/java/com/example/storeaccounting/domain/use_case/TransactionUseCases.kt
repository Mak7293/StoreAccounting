package com.example.storeaccounting.domain.use_case

data class TransactionUseCases (
    val addTransaction: AddTransaction,
    val getInventoryTransaction: GetInventoryTransaction,
    val deleteTransaction: DeleteTransaction,
)
package com.example.storeaccounting.domain.util

sealed class TransactionState (val state: String) {
    object Inventory: TransactionState("inventory")
    object Sale: TransactionState("sale")
}
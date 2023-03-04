package com.example.storeaccounting.domain.util

sealed class TransactionState (val state: String) {
    object Create: TransactionState("create")
    object Sale: TransactionState("Sale")
    object Edit: TransactionState("Edit")

}
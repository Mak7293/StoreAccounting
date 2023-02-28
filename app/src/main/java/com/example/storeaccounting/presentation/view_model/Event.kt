package com.example.storeaccounting.presentation.view_model

import com.example.storeaccounting.domain.model.Transaction

sealed class Event {
    data class InsertInventory(val transaction:Transaction): Event()
    data class UpdateInventory(val transaction:Transaction): Event()
    data class DeleteInventory(val transaction:Transaction): Event()
}
package com.example.storeaccounting.presentation.view_model

import com.example.storeaccounting.domain.model.Transaction

data class ViewModelState (
    val inventory: List<Transaction> = emptyList(),
    val sale: List<Transaction> = emptyList(),
)
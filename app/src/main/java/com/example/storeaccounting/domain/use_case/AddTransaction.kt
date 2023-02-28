package com.example.storeaccounting.domain.use_case

import android.content.res.Resources
import com.example.storeaccounting.R
import com.example.storeaccounting.domain.custom_exception.InvalidTransactionException
import com.example.storeaccounting.domain.model.Transaction
import com.example.storeaccounting.domain.repository.TransactionRepository

class AddTransaction(private val repository: TransactionRepository, private val resource: Resources) {
    suspend operator fun invoke(transaction: Transaction){
        if(transaction.title.isBlank()){
            throw InvalidTransactionException(resource.getString(R.string.blank_title_exception))
        }
        if(transaction.title.isBlank()){
            throw InvalidTransactionException(resource.getString(R.string.blank_number_exception))
        }
        repository.insertTransaction(transaction)
    }
}
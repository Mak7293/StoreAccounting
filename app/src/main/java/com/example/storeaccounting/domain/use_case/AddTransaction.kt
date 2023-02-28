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
        if(transaction.number.isBlank()){
            throw InvalidTransactionException(resource.getString(R.string.blank_number_exception))
        }else if(checkNumberIsNotNegative(transaction.number)){
            throw InvalidTransactionException(resource.getString(R.string.negative_number_exception))
        }else if (checkNumberIsDigit(transaction.number)){
            throw InvalidTransactionException(resource.getString(R.string.digit_number_exception))
        }else if (transaction.number.toInt() == 0){
            throw InvalidTransactionException(resource.getString(R.string.zero_number_exception))
        }
        if(transaction.buyPrice.isBlank()){
            throw InvalidTransactionException(resource.getString(R.string.blank_buy_price_exception))
        }else if(checkNumberIsNotNegative(transaction.buyPrice)){
            throw InvalidTransactionException(resource.getString(R.string.negative_buy_price_exception))
        }else if (checkNumberIsDigit(transaction.buyPrice)){
            throw InvalidTransactionException(resource.getString(R.string.digit_buy_price_exception))
        }else if (transaction.buyPrice.toInt() == 0){
            throw InvalidTransactionException(resource.getString(R.string.zero_buy_price_exception))
        }
        if(transaction.sellPrice.isBlank()){
            throw InvalidTransactionException(resource.getString(R.string.blank_sell_price_exception))
        }else if(checkNumberIsNotNegative(transaction.sellPrice)){
            throw InvalidTransactionException(resource.getString(R.string.negative_sell_price_exception))
        }else if (checkNumberIsDigit(transaction.sellPrice)){
            throw InvalidTransactionException(resource.getString(R.string.digit_sell_price_exception))
        }else if (transaction.sellPrice.toInt() == 0){
            throw InvalidTransactionException(resource.getString(R.string.zero_sell_price_exception))
        }
        repository.insertTransaction(transaction)
    }
    private fun checkNumberIsDigit(string: String):Boolean{
        for(i in string){
            if (!i.isDigit()){
                return true
            }
        }
        return false
    }
    private fun checkNumberIsNotNegative(string: String):Boolean{
        for(i in string){
            if (i == '-'){
                return true
            }
        }
        return false
    }
}
package com.example.storeaccounting.domain.use_case

import android.content.res.Resources
import com.example.storeaccounting.R
import com.example.storeaccounting.domain.custom_exception.InvalidTransactionException
import com.example.storeaccounting.domain.model.History
import com.example.storeaccounting.domain.model.InventoryEntity
import com.example.storeaccounting.domain.repository.InventoryRepository
import com.example.storeaccounting.domain.util.TransactionState

class AddInventory(private val repository: InventoryRepository, private val resource: Resources) {
    suspend operator fun invoke(inventoryEntity: InventoryEntity){
        if(inventoryEntity.title.isBlank()){
            throw InvalidTransactionException(resource.getString(R.string.blank_title_exception))
        }
        if(inventoryEntity.number.isBlank()){
            throw InvalidTransactionException(resource.getString(R.string.blank_number_exception))
        }else if(checkNumberIsNotNegative(inventoryEntity.number)){
            throw InvalidTransactionException(resource.getString(R.string.negative_number_exception))
        }else if (checkNumberIsDigit(inventoryEntity.number)){
            throw InvalidTransactionException(resource.getString(R.string.digit_number_exception))
        }else if (inventoryEntity.number.toInt() == 0){
            throw InvalidTransactionException(resource.getString(R.string.zero_number_exception))
        }
        if(inventoryEntity.buyPrice.isBlank()){
            throw InvalidTransactionException(resource.getString(R.string.blank_buy_price_exception))
        }else if(checkNumberIsNotNegative(inventoryEntity.buyPrice)){
            throw InvalidTransactionException(resource.getString(R.string.negative_buy_price_exception))
        }else if (checkNumberIsDigit(inventoryEntity.buyPrice)){
            throw InvalidTransactionException(resource.getString(R.string.digit_buy_price_exception))
        }else if (inventoryEntity.buyPrice.toInt() == 0){
            throw InvalidTransactionException(resource.getString(R.string.zero_buy_price_exception))
        }
        if(inventoryEntity.sellPrice.isBlank()){
            throw InvalidTransactionException(resource.getString(R.string.blank_sell_price_exception))
        }else if(checkNumberIsNotNegative(inventoryEntity.sellPrice)){
            throw InvalidTransactionException(resource.getString(R.string.negative_sell_price_exception))
        }else if (checkNumberIsDigit(inventoryEntity.sellPrice)){
            throw InvalidTransactionException(resource.getString(R.string.digit_sell_price_exception))
        }else if (inventoryEntity.sellPrice.toInt() == 0){
            throw InvalidTransactionException(resource.getString(R.string.zero_sell_price_exception))
        }
        repository.insertInventory(inventoryEntity)
        val history = History(
            transaction = TransactionState.Create.state,
            title = inventoryEntity.title,
            number = inventoryEntity.number,
            sellPrice = inventoryEntity.sellPrice,
            buyPrice = inventoryEntity.buyPrice,
            date = inventoryEntity.date,
            timeStamp = inventoryEntity.timeStamp
        )
        repository.insertHistory(history)
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
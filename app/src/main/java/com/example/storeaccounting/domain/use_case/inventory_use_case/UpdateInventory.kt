package com.example.storeaccounting.domain.use_case.inventory_use_case

import android.content.res.Resources
import com.example.storeaccounting.R
import com.example.storeaccounting.domain.custom_exception.InvalidTransactionException
import com.example.storeaccounting.domain.model.History
import com.example.storeaccounting.domain.model.InventoryEntity
import com.example.storeaccounting.domain.repository.Repository
import com.example.storeaccounting.domain.util.TransactionState

class UpdateInventory(private val repository: Repository, private val resource: Resources) {

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
        }else if (inventoryEntity.number.toLong() == 0L){
            throw InvalidTransactionException(resource.getString(R.string.zero_number_exception))
        }
        if(inventoryEntity.buyPrice.isBlank()){
            throw InvalidTransactionException(resource.getString(R.string.blank_buy_price_exception))
        }else if(checkNumberIsNotNegative(inventoryEntity.buyPrice)){
            throw InvalidTransactionException(resource.getString(R.string.negative_buy_price_exception))
        }else if (checkNumberIsDigit(inventoryEntity.buyPrice)){
            throw InvalidTransactionException(resource.getString(R.string.digit_buy_price_exception))
        }else if (inventoryEntity.buyPrice.toLong() == 0L){
            throw InvalidTransactionException(resource.getString(R.string.zero_buy_price_exception))
        }
        if(inventoryEntity.sellPrice.isBlank()){
            throw InvalidTransactionException(resource.getString(R.string.blank_sell_price_exception))
        }else if(checkNumberIsNotNegative(inventoryEntity.sellPrice)){
            throw InvalidTransactionException(resource.getString(R.string.negative_sell_price_exception))
        }else if (checkNumberIsDigit(inventoryEntity.sellPrice)){
            throw InvalidTransactionException(resource.getString(R.string.digit_sell_price_exception))
        }else if (inventoryEntity.sellPrice.toLong() == 0L){
            throw InvalidTransactionException(resource.getString(R.string.zero_sell_price_exception))
        }
        val history = History(
            createdTimeStamp = inventoryEntity.createdTimeStamp ,
            transaction = TransactionState.Edit.state,
            title = inventoryEntity.title,
            saleNumber = "0",
            buyPrice = inventoryEntity.buyPrice,
            sellPrice = inventoryEntity.sellPrice,
            timeStamp = inventoryEntity.timeStamp,
            date = inventoryEntity.date,
            remainingInventory = inventoryEntity.number.toLong()
        )
        repository.insertHistory(history)
        repository.updateInventory(inventoryEntity)
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
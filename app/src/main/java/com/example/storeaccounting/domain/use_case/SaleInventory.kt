package com.example.storeaccounting.domain.use_case

import android.content.res.Resources
import com.example.storeaccounting.R
import com.example.storeaccounting.domain.custom_exception.InvalidTransactionException
import com.example.storeaccounting.domain.model.History
import com.example.storeaccounting.domain.model.InventoryEntity
import com.example.storeaccounting.domain.repository.InventoryRepository
import com.example.storeaccounting.domain.util.TransactionState

class SaleInventory(private val repository: InventoryRepository, private val resource: Resources) {

    suspend operator fun invoke (
        newInventoryEntity: InventoryEntity,previousInventoryEntity: InventoryEntity){
        if(newInventoryEntity.number.isBlank()){
            throw InvalidTransactionException(resource.getString(R.string.blank_number_exception))
        }else if(checkNumberIsNotNegative(newInventoryEntity.number)){
            throw InvalidTransactionException(resource.getString(R.string.negative_number_exception))
        }else if (checkNumberIsDigit(newInventoryEntity.number)){
            throw InvalidTransactionException(resource.getString(R.string.digit_number_exception))
        }else if (newInventoryEntity.number.toInt() == 0){
            throw InvalidTransactionException(resource.getString(R.string.zero_number_exception))
        }else if (newInventoryEntity.number.toInt() > previousInventoryEntity.number.toInt()){
            throw InvalidTransactionException(resource.getString(R.string.sale_number_exception))
        }
        val finalInventoryEntity = newInventoryEntity.copy(
            number = (previousInventoryEntity.number.toInt() - newInventoryEntity.number.toInt()).toString()
        )
        val history = History(
            createdTimeStamp = newInventoryEntity.createdTimeStamp,
            transaction = TransactionState.Sale.state,
            title = newInventoryEntity.title,
            number = newInventoryEntity.number,
            buyPrice = newInventoryEntity.buyPrice,
            sellPrice = newInventoryEntity.sellPrice,
            date = newInventoryEntity.date,
            timeStamp = newInventoryEntity.timeStamp
        )
        repository.updateInventory(finalInventoryEntity)
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
package com.example.storeaccounting.domain.use_case.inventory_use_case

import android.content.res.Resources
import com.example.storeaccounting.R
import com.example.storeaccounting.domain.custom_exception.InvalidTransactionException
import com.example.storeaccounting.domain.model.History
import com.example.storeaccounting.domain.model.InventoryEntity
import com.example.storeaccounting.domain.repository.InventoryRepository

class SaleInventory(private val repository: InventoryRepository, private val resource: Resources) {

    suspend operator fun invoke (
        inventoryEntity: InventoryEntity, 
        history: History
    ){
        if(inventoryEntity.number.isBlank()){
            throw InvalidTransactionException(resource.getString(R.string.blank_number_exception))
        }else if(checkNumberIsNotNegative(inventoryEntity.number)){
            throw InvalidTransactionException(resource.getString(R.string.negative_number_exception))
        }else if (checkNumberIsDigit(inventoryEntity.number)){
            throw InvalidTransactionException(resource.getString(R.string.digit_number_exception))
        }else if (history.saleNumber.toInt() == 0){
            throw InvalidTransactionException(resource.getString(R.string.zero_number_exception))
        }else if (inventoryEntity.number.toInt() < 0){
            throw InvalidTransactionException(resource.getString(R.string.sale_number_exception))
        }
        repository.updateInventory(inventoryEntity)
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
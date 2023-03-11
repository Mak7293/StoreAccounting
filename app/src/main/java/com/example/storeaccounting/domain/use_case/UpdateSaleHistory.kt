package com.example.storeaccounting.domain.use_case

import android.content.res.Resources
import com.example.storeaccounting.R
import com.example.storeaccounting.domain.custom_exception.InvalidTransactionException
import com.example.storeaccounting.domain.model.History
import com.example.storeaccounting.domain.model.InventoryEntity
import com.example.storeaccounting.domain.repository.InventoryRepository

class UpdateSaleHistory(private val repository: InventoryRepository,private val resource: Resources) {

    suspend operator fun invoke(
        inventoryEntity: InventoryEntity,
        newHistory: History,
        oldHistory: History
    ){
        if(newHistory.saleNumber.isBlank()){
            throw InvalidTransactionException(resource.getString(R.string.blank_number_exception))
        }else if(checkNumberIsNotNegative(newHistory.saleNumber)){
            throw InvalidTransactionException(resource.getString(R.string.negative_number_exception))
        }else if (checkNumberIsDigit(newHistory.saleNumber)){
            throw InvalidTransactionException(resource.getString(R.string.digit_number_exception))
        }else if (newHistory.saleNumber.toInt() == 0){
            throw InvalidTransactionException(resource.getString(R.string.zero_number_exception))
        }else if (newHistory.remainingInventory < 0){
            throw InvalidTransactionException(resource.getString(R.string.sale_number_exception))
        }

        if(oldHistory.createdTimeStamp == newHistory.createdTimeStamp){
            val rollbackInventory = inventoryEntity.copy(number = (inventoryEntity.number.toInt() +
                    oldHistory.saleNumber.toInt()).toString())
            repository.updateInventory(rollbackInventory)
            repository.updateHistory(newHistory)
        }else{
            val getOldInventoryByHistory = repository.getHistoriesByInventoryTimeStamp(oldHistory.createdTimeStamp)
            val oldInventory = getOldInventoryByHistory.inventory
            val rollbackOldInventory = oldInventory.copy(number = (oldInventory.number.toInt() +
                    oldHistory.saleNumber.toInt()).toString())
            repository.updateInventory(rollbackOldInventory)


            repository.updateInventory(inventoryEntity)
            repository.updateHistory(newHistory)

        }
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
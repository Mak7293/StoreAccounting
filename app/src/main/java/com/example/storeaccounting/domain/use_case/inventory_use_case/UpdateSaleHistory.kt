package com.example.storeaccounting.domain.use_case.inventory_use_case

import android.content.res.Resources
import com.example.storeaccounting.R
import com.example.storeaccounting.domain.custom_exception.InvalidTransactionException
import com.example.storeaccounting.domain.model.History
import com.example.storeaccounting.domain.model.InventoryEntity
import com.example.storeaccounting.domain.repository.Repository

class UpdateSaleHistory(private val repository: Repository) {

    suspend operator fun invoke(
        inventoryEntity: InventoryEntity,
        newHistory: History,
        oldHistory: History
    ){
        if(newHistory.saleNumber.isBlank()){
            throw InvalidTransactionException("تعداد کالا را وارد کنید.")
        }else if(checkNumberIsNotNegative(newHistory.saleNumber)){
            throw InvalidTransactionException("تعداد کالا نمیتواند منفی باشد.")
        }else if (checkNumberIsDigit(newHistory.saleNumber)){
            throw InvalidTransactionException("تعداد کالا تنها میتواند عدد باشد.")
        }else if (newHistory.saleNumber.toLong() == 0L){
            throw InvalidTransactionException("تعداد کالا نمیتواند صفر باشد.")
        }else if (newHistory.remainingInventory < 0){
            throw InvalidTransactionException("تعداد کالای فروخته شده نمیتواند بیشتر از تعداد کالای موجود باشد.")
        }

        if(oldHistory.createdTimeStamp == newHistory.createdTimeStamp){
            val rollbackInventory = inventoryEntity.copy(number = (inventoryEntity.number.toLong() +
                    oldHistory.saleNumber.toLong()).toString())
            repository.updateInventory(rollbackInventory)
            repository.updateHistory(newHistory)
        }else{
            val getOldInventoryByHistory = repository.getHistoriesByInventoryTimeStamp(oldHistory.createdTimeStamp)
            val oldInventory = getOldInventoryByHistory.inventory
            val rollbackOldInventory = oldInventory.copy(number = (oldInventory.number.toLong() +
                    oldHistory.saleNumber.toLong()).toString())
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
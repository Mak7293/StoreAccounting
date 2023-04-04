package com.example.storeaccounting.domain.use_case.inventory_use_case

import android.content.res.Resources
import com.example.storeaccounting.R
import com.example.storeaccounting.domain.custom_exception.InvalidTransactionException
import com.example.storeaccounting.domain.model.History
import com.example.storeaccounting.domain.model.InventoryEntity
import com.example.storeaccounting.domain.repository.Repository

class SaleInventory(private val repository: Repository) {

    suspend operator fun invoke (
        inventoryEntity: InventoryEntity, 
        history: History,
    ){
        if(history.saleNumber.isBlank()){
            throw InvalidTransactionException("تعداد کالا را وارد کنید.")
        }else if(checkNumberIsNotNegative(history.saleNumber)){
            throw InvalidTransactionException("تعداد کالا نمیتواند منفی باشد.")
        }else if (checkNumberIsDigit(history.saleNumber)){
            throw InvalidTransactionException("تعداد کالا تنها میتواند عدد باشد. همچنین نباید از فاصله خالی (white space) در این قسمت استفاده کرد.")
        }else if (history.saleNumber.toLong() == 0L){
            throw InvalidTransactionException("تعداد کالا نمیتواند صفر باشد.")
        }else if (inventoryEntity.number.toLong() < 0L){
            throw InvalidTransactionException("تعداد کالای فروخته شده نمیتواند بیشتر از تعداد کالای موجود باشد.")
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
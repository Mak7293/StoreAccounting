package com.example.storeaccounting.domain.use_case.inventory_use_case


import com.example.storeaccounting.domain.custom_exception.InvalidTransactionException
import com.example.storeaccounting.domain.model.History
import com.example.storeaccounting.domain.model.InventoryEntity
import com.example.storeaccounting.domain.repository.Repository
import com.example.storeaccounting.domain.util.TransactionState

class AddInventory(private val repository: Repository) {
    suspend operator fun invoke(inventoryEntity: InventoryEntity){
        if(inventoryEntity.title.isBlank()){
            throw InvalidTransactionException("نام کالا را وارد کنید.")
        }
        if(inventoryEntity.number.isBlank()){
            throw InvalidTransactionException("تعداد کالا را وارد کنید.")
        }else if(checkNumberIsNotNegative(inventoryEntity.number)){
            throw InvalidTransactionException("تعداد کالا نمیتواند منفی باشد.")
        }else if (checkNumberIsDigit(inventoryEntity.number)){
            throw InvalidTransactionException("تعداد کالا تنها میتواند عدد باشد. همچنین نباید از فاصله خالی (white space) در این قسمت استفاده کرد.")
        }else if (inventoryEntity.number.toLong() == 0L){
            throw InvalidTransactionException("تعداد کالا نمیتواند صفر باشد.")
        }
        if(inventoryEntity.buyPrice.isBlank()){
            throw InvalidTransactionException("قیمت خرید کالا را وارد کنید.")
        }else if(checkNumberIsNotNegative(inventoryEntity.buyPrice)){
            throw InvalidTransactionException("قیمت خرید کالا نمیتواند منفی باشد.")
        }else if (checkNumberIsDigit(inventoryEntity.buyPrice)){
            throw InvalidTransactionException("قیمت خرید کالا تنها میتواند عدد باشد. همچنین نباید از فاصله خالی (white space) در این قسمت استفاده کرد.")
        }else if (inventoryEntity.buyPrice.toLong() == 0L){
            throw InvalidTransactionException("قیمت خرید کالا نمیتواند صفر باشد.")
        }
        if(inventoryEntity.sellPrice.isBlank()){
            throw InvalidTransactionException("قیمت فروش کالا را وارد کنید.")
        }else if(checkNumberIsNotNegative(inventoryEntity.sellPrice)){
            throw InvalidTransactionException("قیمت فروش کالا نمیتواند منفی باشد.")
        }else if (checkNumberIsDigit(inventoryEntity.sellPrice)){
            throw InvalidTransactionException("قیمت فروش کالا تنها میتواند عدد باشد. همچنین نباید از فاصله خالی (white space) در این قسمت استفاده کرد.")
        }else if (inventoryEntity.sellPrice.toLong() == 0L){
            throw InvalidTransactionException("قیمت فروش کالا نمیتواند صفر باشد.")
        }
        repository.insertInventory(inventoryEntity)
        val history = History(
            createdTimeStamp = inventoryEntity.timeStamp,
            remainingInventory = inventoryEntity.number.toLong(),
            transaction = TransactionState.Create.state,
            title = inventoryEntity.title,
            saleNumber = "0",
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
package com.example.storeaccounting.domain.use_case.general_use_case

import com.example.storeaccounting.domain.model.CreditCard
import com.example.storeaccounting.domain.repository.InventoryRepository

class CreateCreditCard(private val repository: InventoryRepository) {
    suspend operator fun invoke(creditCard: CreditCard){

        repository.createCreditCard(creditCard)
    }

}
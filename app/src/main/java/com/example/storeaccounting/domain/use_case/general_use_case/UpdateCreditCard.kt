package com.example.storeaccounting.domain.use_case.general_use_case

import com.example.storeaccounting.domain.model.CreditCard
import com.example.storeaccounting.domain.repository.Repository

class UpdateCreditCard(private val repository: Repository) {

    suspend operator fun invoke(creditCard: CreditCard){
        repository.updateCreditCard(creditCard)
    }
}
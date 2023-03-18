package com.example.storeaccounting.domain.use_case.general_use_case

import com.example.storeaccounting.domain.model.CreditCard
import com.example.storeaccounting.domain.repository.Repository
import kotlinx.coroutines.flow.Flow

class GetAllCreditCardList(private val repository: Repository) {

    operator fun invoke(): Flow<List<CreditCard>> {
        return repository.fetchAllCreditCard()
    }
}
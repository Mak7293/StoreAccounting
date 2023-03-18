package com.example.storeaccounting.domain.use_case.general_use_case

data class GeneralUseCases (
    val createUseCases: CreateCreditCard,
    val getAllCreditCardList: GetAllCreditCardList,
    val deleteCreditCard: DeleteCreditCard,
    val updateCreditCard: UpdateCreditCard
)
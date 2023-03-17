package com.example.storeaccounting.data.data_source

import androidx.room.*
import com.example.storeaccounting.domain.model.CreditCard

@Dao
interface CreditCardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createCreditCard(creditCard: CreditCard)

    @Delete
    suspend fun deleteCreditCard(creditCard: CreditCard)

    @Update
    suspend fun updateCreditCard(creditCard: CreditCard)
}
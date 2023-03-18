package com.example.storeaccounting.data.data_source

import androidx.room.*
import com.example.storeaccounting.domain.model.CreditCard
import com.example.storeaccounting.domain.model.InventoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CreditCardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createCreditCard(creditCard: CreditCard)
    @Delete
    suspend fun deleteCreditCard(creditCard: CreditCard)
    @Update
    suspend fun updateCreditCard(creditCard: CreditCard)
    @Query("SELECT * FROM credit_card_table")
    fun fetchAllCreditCard(): Flow<List<CreditCard>>
    @Query("SELECT * FROM credit_card_table WHERE id = :id ")
    suspend fun fetchSpecificCreditCard(id: Int): CreditCard

}